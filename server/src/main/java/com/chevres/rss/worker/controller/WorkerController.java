/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.worker.controller;

import com.chevres.rss.restapi.controller.jsonresponse.ErrorMessageResponse;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessMessageResponse;
import com.chevres.rss.worker.feedupdater.FeedUpdater;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zanchi_r
 */
@Controller
public class WorkerController {
    @RequestMapping(path = "/worker/refresh/feed/{feedId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> refreshFeed(@PathVariable int feedId) {
        FeedUpdater feedUpdater = new FeedUpdater();
        if (feedUpdater.updateFeedById(feedId)) {
            return new ResponseEntity(new SuccessMessageResponse("success"),
                HttpStatus.OK);
        }
        else {
            return new ResponseEntity(new ErrorMessageResponse("error"),
                HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(path = "/worker/refresh", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> refreshFeeds() {
        FeedUpdater feedUpdater = new FeedUpdater();
        feedUpdater.updateAll();
        return new ResponseEntity(new SuccessMessageResponse("success"),
                HttpStatus.OK);
    }
}