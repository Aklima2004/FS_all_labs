package com.example.fs_l3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TddExampleTest {

    @Test
    void testVinNormalization() {
        // Пишем тест до кода
        String vin = "abCd1234";
        String expected = "ABCD1234";

        // Пишем минимальную реализацию, чтобы тест прошёл
        String result = normalizeVin(vin);

        assertEquals(expected, result);
    }

    String normalizeVin(String vin) {
        // Простой код для нормализации VIN
        return vin.toUpperCase();
    }
}
