/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller.validators;

import com.chevres.rss.restapi.controller.jsonobjects.UserJson;
import com.chevres.rss.restapi.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author anthony
 */
@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return UserJson.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.username", "username is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.password", "password is required.");
    }

}
