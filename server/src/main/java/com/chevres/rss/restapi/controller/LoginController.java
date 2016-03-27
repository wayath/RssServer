package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonobjects.LoginJsonStatus;
import com.chevres.rss.restapi.controller.validators.LoginValidator;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import com.chevres.rss.restapi.utils.TokenGenerator;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    LoginValidator loginValidator;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(
            @RequestBody User user,
            BindingResult bindingResult) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        loginValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        UserDAO userDAO = context.getBean(UserDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        User foundUser = userDAO.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (foundUser == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
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

        return new ResponseEntity(new LoginJsonStatus("ok", userAuth.getToken()),
                HttpStatus.OK);
    }
}
