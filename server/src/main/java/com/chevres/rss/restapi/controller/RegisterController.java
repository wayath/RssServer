package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorMessageResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessMessageResponse;
import com.chevres.rss.restapi.controller.validators.UserValidator;
import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author anthony
 */
@Controller
public class RegisterController {

    @Autowired
    UserValidator userValidator;

    @CrossOrigin
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<String> register(
            @RequestBody User user,
            BindingResult bindingResult) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("bad_params"),
                    HttpStatus.BAD_REQUEST);
        }

        UserDAO userDAO = context.getBean(UserDAO.class);

        if (userDAO.doesExist(user.getUsername())) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("already_exist"),
                    HttpStatus.BAD_REQUEST);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setType(User.USER_TYPE_LABEL);
        userDAO.create(user);

        context.close();

        return new ResponseEntity(new SuccessMessageResponse("success"),
                HttpStatus.OK);
    }
}
