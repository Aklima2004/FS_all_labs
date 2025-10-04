package com.example.fs_l3.util;

import java.util.Locale;

public class VinService {

    /**
     * Нормализует VIN:
     * - убирает пробелы и дефисы
     * - переводит в верхний регистр
     */
    public String normalize(String input) {
        if (input == null) return null;
        return input
                .replace("-", "")
                .replace(" ", "")
                .toUpperCase(Locale.ROOT);
    }

    /**
     * Базовая валидация VIN:
     * - длина ровно 17
     * - не содержит букв I, O, Q
     * - допускает только символы A-H J-N P R-Z и 0-9
     *
     * @throws IllegalArgumentException при нарушении правил
     */
    public void validate(String vin) {
        if (vin == null) {
            throw new IllegalArgumentException("VIN must not be null");
        }
        String v = vin.toUpperCase(Locale.ROOT);

        if (v.length() != 17) {
            throw new IllegalArgumentException("VIN length must be exactly 17");
        }
        if (v.chars().anyMatch(ch -> ch == 'I' || ch == 'O' || ch == 'Q')) {
            throw new IllegalArgumentException("VIN must not contain I, O, or Q");
        }
        if (!v.matches("^[A-HJ-NPR-Z0-9]{17}$")) {
            throw new IllegalArgumentException("VIN contains invalid characters");
        }
    }
}
