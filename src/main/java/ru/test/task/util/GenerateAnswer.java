package ru.test.task.util;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class GenerateAnswer {


    public static String generateAnswer(Errors errors) {
        if (errors.hasErrors()) {
            List<String> listOfErrors = new ArrayList<>();
            for (FieldError error : errors.getFieldErrors()) {
                listOfErrors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return listOfErrors.toString();
        }
        return "";
    }
}
