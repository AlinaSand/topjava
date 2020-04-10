package ru.javawebinar.topjava.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class BindingResultUtil {

    private BindingResultUtil() {
    }

    public static ResponseEntity<String> bindErrors(BindingResult bindingResult){
        StringJoiner stringJoiner = new StringJoiner("<br>");
        bindingResult.getFieldErrors().forEach(
                fieldError -> stringJoiner.add(String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
        );
        return ResponseEntity.unprocessableEntity().body(stringJoiner.toString());
    }
}
