/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorMessageResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessFeedInfoResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessGetFeedWithIdResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessGetFeedsResponse;
import com.chevres.rss.restapi.controller.validators.FeedValidator;
import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.UserAuth;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author anthony
 */
@Controller
public class FeedController {

    @Autowired
    FeedValidator feedValidator;

    @RequestMapping(path = "/feed", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addFeed(
            @RequestHeader(value = "User-token") String userToken,
            @RequestBody Feed feed,
            BindingResult bindingResult) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        feedValidator.validate(feed, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new ErrorMessageResponse("bad_params"),
                    HttpStatus.BAD_REQUEST);
        }

        if (feedDAO.doesExist(userAuth, feed.getUrl())) {
            return new ResponseEntity(new ErrorMessageResponse("already_exist"),
                    HttpStatus.BAD_REQUEST);
        }

        feed.setIdUser(userAuth.getIdUser());
        feedDAO.create(feed);

        context.close();

        return new ResponseEntity(new SuccessFeedInfoResponse(
                feed.getId(), feed.getName(), feed.getUrl()),
                HttpStatus.OK);

    }

    @RequestMapping(path = "/feeds", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getFeeds(
            @RequestHeader(value = "User-token") String userToken) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        List<Feed> feeds = feedDAO.findAll(userAuth);
        List<SuccessGetFeedWithIdResponse> finalList = new ArrayList<>();
        for (Feed feed : feeds) {
            finalList.add(new SuccessGetFeedWithIdResponse(
                    feed.getId(),
                    feed.getName(),
                    feed.getUrl()));
        }

        context.close();

        return new ResponseEntity(new SuccessGetFeedsResponse(finalList),
                HttpStatus.OK);

    }

    @RequestMapping(path = "/feed/{feedId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getFeedInfos(
            @RequestHeader(value = "User-token") String userToken,
            @PathVariable int feedId) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        Feed feed = feedDAO.findById(userAuth, feedId);
        if (feed == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        context.close();

        return new ResponseEntity(new SuccessFeedInfoResponse(
                feed.getId(), feed.getName(), feed.getUrl()),
                HttpStatus.OK);
    }

    @RequestMapping(path = "/feed/{feedId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> updateFeedInfos(
            @RequestHeader(value = "User-token") String userToken,
            @RequestBody Feed feedRequest,
            @PathVariable int feedId) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(feedRequest.getName())) {
            return new ResponseEntity(new ErrorMessageResponse("bad_params"),
                    HttpStatus.BAD_REQUEST);
        }

        Feed feed = feedDAO.findById(userAuth, feedId);
        if (feed == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        feedDAO.updateName(feed, feedRequest);
        context.close();

        return new ResponseEntity(new SuccessFeedInfoResponse(
                feed.getId(), feed.getName(), feed.getUrl()),
                HttpStatus.OK);
    }
}
