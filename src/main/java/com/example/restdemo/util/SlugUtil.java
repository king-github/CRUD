package com.example.restdemo.util;

import java.text.Normalizer;

public class SlugUtil {

    public static String slugify(String input) {

        input = input.trim();

        input = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^\\w+]", "-")
                .replaceAll("\\s+", "-")
                .replaceAll("[-]+", "-")
                .replaceAll("^-", "")
                .replaceAll("-$", "");

        input = input.toLowerCase();

        return input;
    }

}