package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonobjects.RegisterJsonStatus;
import com.chevres.rss.restapi.controller.validators.RegisterValidator;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import com.chevres.rss.restapi.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
    public RegisterJsonStatus register(
            @RequestBody User user,
            BindingResult bindingResult) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        registerValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return new RegisterJsonStatus("error_object");
        }

        UserDAO userDAO = context.getBean(UserDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        if (userDAO.doesExist(user.getUsername())) {
            return new RegisterJsonStatus("error_username");
        }
        
        userDAO.create(user);

        TokenGenerator tg = new TokenGenerator();
        
        UserAuth userAuth = new UserAuth();
        userAuth.setIdUser(user.getId());
        userAuth.setToken(tg.getToken());
        userAuthDAO.create(userAuth);
        context.close();

        return new RegisterJsonStatus("ok", userAuth.getToken());
    }
}
