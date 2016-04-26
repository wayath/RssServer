package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorMessageResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessLoginResponse;
import com.chevres.rss.restapi.controller.validators.UserValidator;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import com.chevres.rss.restapi.utils.TokenGenerator;
import java.sql.Timestamp;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class LoginController {

    @Autowired
    UserValidator userValidator;

    @CrossOrigin
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(
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
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        User foundUser = userDAO.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (foundUser == null) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("bad_credentials"),
                    HttpStatus.BAD_REQUEST);
        }

        TokenGenerator tg = new TokenGenerator();
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        UserAuth userAuth = new UserAuth();
        userAuth.setIdUser(foundUser.getId());
        userAuth.setToken(tg.getToken());
        userAuth.setCreateDate(timestamp);
        userAuthDAO.create(userAuth);

        context.close();

        return new ResponseEntity(new SuccessLoginResponse(userAuth.getToken()),
                HttpStatus.OK);
    }
}
