/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorMessageResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessGetArticleWithIdResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessGetFeedArticlesResponse;
import com.chevres.rss.restapi.dao.ArticleDAO;
import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.UserAuth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author anthony
 */
@Controller
public class ArticleController {

    @RequestMapping(path = "/feeds/articles/{pageNumber}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getArticles(
            @RequestHeader(value = "User-token") String userToken,
            @PathVariable int pageNumber) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        ArticleDAO articleDAO = context.getBean(ArticleDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        List<Feed> feeds = feedDAO.findAll(userAuth);
        List<Article> articles = articleDAO.findArticlesByPageId(feeds, pageNumber);

        List<SuccessGetArticleWithIdResponse> finalList = new ArrayList<>();
        for (Article article : articles) {
            finalList.add(new SuccessGetArticleWithIdResponse(
                    article.getId(),
                    article.getFeed().getId(),
                    article.getStatus().getLabel(),
                    article.getLink(),
                    article.getTitle(),
                    article.getPreviewContent(),
                    article.getFullContent()));
        }
        return new ResponseEntity(new SuccessGetFeedArticlesResponse(finalList),
                HttpStatus.OK);
    }

    @RequestMapping(path = "/feed/{feedId}/articles/{pageNumber}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getFeedArticles(
            @RequestHeader(value = "User-token") String userToken,
            @PathVariable int feedId,
            @PathVariable int pageNumber) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        UserAuthDAO userAuthDAO = context.getBean(UserAuthDAO.class);
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        ArticleDAO articleDAO = context.getBean(ArticleDAO.class);

        UserAuth userAuth = userAuthDAO.findByToken(userToken);
        if (userAuth == null) {
            return new ResponseEntity(new ErrorMessageResponse("invalid_token"),
                    HttpStatus.BAD_REQUEST);
        }

        Feed feed = feedDAO.findById(userAuth, feedId);
        if (feed == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        List<Article> articles = articleDAO.findArticlesByFeedAndPageId(feed, pageNumber);

        List<SuccessGetArticleWithIdResponse> finalList = new ArrayList<>();
        for (Article article : articles) {
            finalList.add(new SuccessGetArticleWithIdResponse(
                    article.getId(),
                    article.getFeed().getId(),
                    article.getStatus().getLabel(),
                    article.getLink(),
                    article.getTitle(),
                    article.getPreviewContent(),
                    article.getFullContent()));
        }
        return new ResponseEntity(new SuccessGetFeedArticlesResponse(finalList),
                HttpStatus.OK);
    }
}
