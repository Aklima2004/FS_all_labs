package com.example.fs_l3;

import com.example.fs_l3.web.CarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FsL3ApplicationTests {

    @Autowired
    private CarController controller;

    @Test
    void contextLoads() {
        // Проверяем, что контроллер был внедрён
        assertThat(controller).isNotNull();
    }
}
