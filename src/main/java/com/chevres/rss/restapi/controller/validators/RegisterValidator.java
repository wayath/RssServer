/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller.validators;

import com.chevres.rss.restapi.controller.jsonobjects.RegisterJson;
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
public class RegisterValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return RegisterJson.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.username", "username is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.password", "password is required.");
    }
    
    public void validateClient(String client, Errors errors) {
//        int convertedClient = Integer.parseInt(client, 3);
//        if (convertedClient != 1 && convertedClient != 2 && convertedClient != 3) {
//            errors.rejectValue("client", "client is required");
//        }
    }

}
