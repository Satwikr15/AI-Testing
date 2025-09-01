package com.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    public static List<String> readQuestions(String csvFilePath) {
        List<String> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    questions.add(line.trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read questions from CSV", e);
        }
        return questions;
    }
}
