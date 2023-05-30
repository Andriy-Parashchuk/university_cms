package com.foxminded.parashchuk.university.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ControllerUtils {
  static Map<String, String> getErrors(BindingResult bindingResult){
    Map<String, String> errorMap = new  HashMap<>();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return errorMap;
  }

  private ControllerUtils() {
    throw new IllegalStateException("Utility class");
  }

}
