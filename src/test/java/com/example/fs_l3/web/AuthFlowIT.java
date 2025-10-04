package com.example.fs_l3.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционный e2e-тест JWT-авторизации:
 *  1) /login -> получить реальный токен (из JSON поля "token")
 *  2) GET /cars с Bearer-токеном -> 200
 *  3) негативы:
 *     - без токена -> 401
 *     - USER не может DELETE /cars/{id} -> 403
 *
 * В этом тесте НЕ используем @WithMockUser и НЕ отключаем security-фильтры —
 * проверяем реальную цепочку безопасности (JWT).
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    // ====== helpers =================================================================

    private String loginAndGetToken(String username, String password) throws Exception {
        String loginJson = """
            {"username":"%s","password":"%s"}
        """.formatted(username, password);

        MvcResult res = mockMvc.perform(
                    post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String body = res.getResponse().getContentAsString();
        JsonNode node = om.readTree(body);

        // твой AuthController кладёт JWT в поле "token"
        assertThat(node.hasNonNull("token")).as("Login response must contain JSON field 'token'").isTrue();

        String token = node.get("token").asText();
        assertThat(token).isNotBlank();
        return token;
    }

    private long createCarAndGetId(String bearerToken) throws Exception {
        String payload = """
          {"brand":"Test","model":"Car","color":"Gray","registrationNumber":"E2E-0001","modelYear":2022,"price":12345}
        """;

        MvcResult res = mockMvc.perform(
                    post("/cars")
                        .header("Authorization", "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        JsonNode json = om.readTree(res.getResponse().getContentAsString());
        long id = json.get("id").asLong();
        assertThat(id).isPositive();
        return id;
    }

    // ====== tests ===================================================================

    @Test
    void admin_can_login_and_access_protected_resource() throws Exception {
        String adminToken = loginAndGetToken("admin", "admin");

        mockMvc.perform(get("/cars")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void without_token_should_be_401() throws Exception {
        mockMvc.perform(get("/cars"))
               .andExpect(status().isUnauthorized()); // 401
    }

    @Test
    void user_cannot_delete_car_should_be_403() throws Exception {
        // создаём машину админом
        String adminToken = loginAndGetToken("admin", "admin");
        long id = createCarAndGetId(adminToken);

        // логинимся пользователем
        String userToken = loginAndGetToken("user", "user");

        // пробуем удалить как USER -> ожидаем 403 (по твоей SecurityConfig)
        mockMvc.perform(delete("/cars/{id}", id)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden()); // 403
    }
}
