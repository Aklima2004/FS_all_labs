package com.example.fs_l3.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VinServiceTest {

    private final VinService vin = new VinService();

    @Test
    void normalize_shouldTrimDashesAndUppercase() {
        // исходник с пробелами и дефисами
        String raw = "  jm1- bk-32  456789012 ";
        String normalized = vin.normalize(raw);
        // ожидаем: без пробелов/дефисов и в верхнем регистре
        assertEquals("JM1BK32456789012", normalized);
    }

    @Test
    void validate_shouldThrowWhenLengthNot17() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> vin.validate("12345678901") // не 17 символов
        );
        assertTrue(ex.getMessage().contains("17"));
    }

    @Test
    void validate_shouldThrowWhenContainsIOQ() {
        // 17-символьные VIN'ы, но с запрещёнными I, O, Q
        assertThrows(IllegalArgumentException.class, () -> vin.validate("1HGCM82633A00I352"));
        assertThrows(IllegalArgumentException.class, () -> vin.validate("1HGCM82633A00O352"));
        assertThrows(IllegalArgumentException.class, () -> vin.validate("1HGCM82633A00Q352"));
    }

    @Test
    void validate_shouldPassForProperVin() {
        // ровно 17 символов, без I/O/Q, допустимые A-H J-N P R-Z и 0-9
        assertDoesNotThrow(() -> vin.validate("JM1BK324567890123"));
    }

    @Test
    void normalize_then_validate_shouldPass() {
        // после нормализации получится 17 символов без запрещённых букв
        String raw = " jm1-bk324-567890123 ";
        String normalized = vin.normalize(raw);
        assertEquals("JM1BK324567890123", normalized);
        assertDoesNotThrow(() -> vin.validate(normalized));
    }
}
