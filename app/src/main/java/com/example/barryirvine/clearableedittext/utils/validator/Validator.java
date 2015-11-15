package com.example.barryirvine.clearableedittext.utils.validator;

public interface Validator<T> {
    boolean isValid(T t);

    ValidatorError[] getErrors();
}