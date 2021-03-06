package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorMessageResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessGetUserResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessGetUserWithIdResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessGetUsersResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessMessageResponse;
import com.chevres.rss.restapi.controller.validators.UserUpdateValidator;
import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
public class UserController {

    @Autowired
    UserUpdateValidator userUpdateValidator;

    @CrossOrigin
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
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            context.close();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        boolean isAdmin = userDAO.isAdmin(userAuth.getIdUser());
        if (!isAdmin && (userAuth.getIdUser() != user.getId())) {
            return new ResponseEntity(new ErrorMessageResponse("admin_required"),
                    HttpStatus.FORBIDDEN);
        }

        context.close();

        return new ResponseEntity(new SuccessGetUserResponse(user.getUsername(), user.getType()),
                HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/user/{username}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> updateUser(
            @RequestHeader(value = "User-token") String userToken,
            @PathVariable String username,
            @RequestBody User userRequest,
            BindingResult bindingResult) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        UserDAO userDAO = context.getBean(UserDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        userUpdateValidator.validate(userRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("bad_params"),
                    HttpStatus.BAD_REQUEST);
        }

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            context.close();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        boolean isAdmin = userDAO.isAdmin(userAuth.getIdUser());
        if ((!isAdmin && (userAuth.getIdUser() != user.getId()))
                || (userRequest.getType() != null && !isAdmin)) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("admin_required"),
                    HttpStatus.FORBIDDEN);
        }

        if (userDAO.doesExist(userRequest.getUsername())
                && !user.getUsername().equalsIgnoreCase(userRequest.getUsername())) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("already_exist"),
                    HttpStatus.BAD_REQUEST);
        }

        userDAO.updateUser(user, userRequest, isAdmin);
        context.close();

        return new ResponseEntity(new SuccessMessageResponse("success"),
                HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/user/{username}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteUser(
            @RequestHeader(value = "User-token") String userToken,
            @PathVariable String username) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        UserDAO userDAO = context.getBean(UserDAO.class);
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            context.close();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        boolean isAdmin = userDAO.isAdmin(userAuth.getIdUser());
        if (!isAdmin && (userAuth.getIdUser() != user.getId())) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("admin_required"),
                    HttpStatus.FORBIDDEN);
        }

        userAuthDAO.removeAllForUser(user);
        feedDAO.removeAllForUser(user);
        userDAO.delete(user);
        context.close();

        return new ResponseEntity(new SuccessMessageResponse("success"),
                HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getUsers(
            @RequestHeader(value = "User-token") String userToken) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        UserDAO userDAO = context.getBean(UserDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        boolean isAdmin = userDAO.isAdmin(userAuth.getIdUser());
        if (!isAdmin) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("admin_required"),
                    HttpStatus.FORBIDDEN);
        }

        List<User> users = userDAO.findEveryone();
        List<SuccessGetUserWithIdResponse> finalList = new ArrayList<>();
        for (User user : users) {
            finalList.add(new SuccessGetUserWithIdResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getType()));
        }

        context.close();

        return new ResponseEntity(new SuccessGetUsersResponse(finalList),
                HttpStatus.OK);
    }

}
