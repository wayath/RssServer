/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.worker.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorMessageResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessMessageResponse;
import com.chevres.rss.restapi.dao.ArticleDAO;
import com.chevres.rss.restapi.dao.ArticleStateDAO;
import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.UserAuth;
import com.chevres.rss.worker.feedupdater.FeedUpdater;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zanchi_r
 */
@Controller
public class WorkerController {

    @CrossOrigin
    @RequestMapping(path = "/worker/refresh/feed/{feedId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> refreshFeed(
            @RequestHeader(value = "User-token") String userToken,
            @PathVariable int feedId) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        ArticleDAO articleDAO = context.getBean(ArticleDAO.class);
        ArticleStateDAO articleStateDAO = context.getBean(ArticleStateDAO.class);
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }
        Feed feed = feedDAO.findById(userAuth, feedId);
        if (feed == null) {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("bad_params"),
                    HttpStatus.BAD_REQUEST);
        }
        FeedUpdater feedUpdater = new FeedUpdater(feedDAO, articleDAO, articleStateDAO);
        if (feedUpdater.updateFeed(feed)) {
            context.close();
            return new ResponseEntity(new SuccessMessageResponse("success"),
                    HttpStatus.OK);
        } else {
            context.close();
            return new ResponseEntity(new ErrorMessageResponse("error"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void refreshFeedsScheduled() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        ArticleDAO articleDAO = context.getBean(ArticleDAO.class);
        ArticleStateDAO articleStateDAO = context.getBean(ArticleStateDAO.class);

        FeedUpdater feedUpdater = new FeedUpdater(feedDAO, articleDAO, articleStateDAO);
        feedUpdater.updateAll();

        context.close();
    }
}
