/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller.validators;

import com.chevres.rss.restapi.controller.jsonobjects.FeedJson;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author anthony
 */
@Component
public class FeedValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return FeedJson.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "error.url", "url is required.");
    }

}
