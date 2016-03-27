package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorRegisterResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessRegisterResponse;
import com.chevres.rss.restapi.controller.validators.RegisterValidator;
import com.chevres.rss.restapi.dao.UserAuthDAO;
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
    RegisterValidator registerValidator;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<String> register(
            @RequestBody User user,
            BindingResult bindingResult) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        registerValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new ErrorRegisterResponse("bad_params"),
                    HttpStatus.BAD_REQUEST);
        }

        UserDAO userDAO = context.getBean(UserDAO.class);

        if (userDAO.doesExist(user.getUsername())) {
            return new ResponseEntity(new ErrorRegisterResponse("already_exist"),
                    HttpStatus.BAD_REQUEST);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userDAO.create(user);

        context.close();

        return new ResponseEntity(new SuccessRegisterResponse("success"),
                HttpStatus.OK);
    }
}
