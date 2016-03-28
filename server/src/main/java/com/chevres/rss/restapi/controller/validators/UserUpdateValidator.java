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
public class UserUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return UserJson.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        if (user.getType() != null
                && (!user.getType().equals(User.ADMIN_TYPE_LABEL)
                && !user.getType().equals(User.USER_TYPE_LABEL))) {
            errors.rejectValue("type", "error.type");
        }
        if (user.getUsername() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.username");
        }
        if (user.getPassword() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.password");
        }
    }

}
