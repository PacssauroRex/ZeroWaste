package com.zerowaste.zerowaste.config.validation.ValidEnum;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    List<String> valueList = null;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        valueList = Stream.of(constraintAnnotation.targetClassType().getEnumConstants())
                .map(e -> ((Enum<?>) e).name())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return valueList.contains(value);
    }
}