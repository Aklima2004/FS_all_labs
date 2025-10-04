package com.example.fs_l3.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестируем контроллер /cars в защищённом режиме:
 * - не отключаем фильтры безопасности
 * - используем @WithMockUser с ролью ADMIN
 * - добавляем CSRF для write-запросов
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = "ADMIN")
class CarRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void testCreateCar() throws Exception {
        // Поля под твою сущность Car:
        // brand, model, color, registrationNumber, modelYear, price
        String payload = """
          {"brand":"Toyota","model":"Camry","color":"Black","registrationNumber":"XYZ-1234","modelYear":2023,"price":30000}
        """;

        mockMvc.perform(post("/cars")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brand").value("Toyota"))   // было $.make
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetAllCars() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteCar() throws Exception {
        // 1) сначала создаём запись корректными полями
        String createPayload = """
          {"brand":"Ford","model":"Focus","color":"Blue","registrationNumber":"DEL-0001","modelYear":2021,"price":15000}
        """;

        MvcResult res = mockMvc.perform(post("/cars")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = om.readTree(res.getResponse().getContentAsString());
        long id = json.get("id").asLong();
        assertThat(id).isPositive();

        // 2) удаляем по полученному id
        mockMvc.perform(delete("/cars/{id}", id).with(csrf()))
                .andExpect(status().isNoContent());
    }
}
