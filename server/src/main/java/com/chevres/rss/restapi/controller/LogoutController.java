package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorLogoutResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessLogoutResponse;
import com.chevres.rss.restapi.controller.validators.LoginValidator;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.model.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
public class LogoutController {

    @Autowired
    LoginValidator loginValidator;

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> logout(
            @RequestHeader(value = "User-token") String userToken) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            return new ResponseEntity(new ErrorLogoutResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        userAuthDAO.delete(userAuth);

        context.close();

        return new ResponseEntity(new SuccessLogoutResponse("success"),
                HttpStatus.OK);
    }
}
