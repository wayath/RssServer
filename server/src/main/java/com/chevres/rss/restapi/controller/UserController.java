package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorMessageResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessGetUserResponse;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
public class UserController {

    @RequestMapping(path = "/user/{username}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getUser(
            @RequestHeader(value = "User-token") String userToken,
            @PathVariable String username) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        UserDAO userDAO = context.getBean(UserDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        boolean isAdmin = userDAO.isAdmin(userAuth.getIdUser());
        if (!isAdmin) {
            return new ResponseEntity(new ErrorMessageResponse("admin_required"),
                    HttpStatus.FORBIDDEN);
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(new SuccessGetUserResponse(user.getUsername(), user.getType()),
                HttpStatus.OK);
    }

}
