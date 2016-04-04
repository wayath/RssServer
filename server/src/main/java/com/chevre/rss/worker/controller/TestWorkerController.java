/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevre.rss.worker.controller;
import com.chevres.rss.restapi.controller.jsonresponse.SuccessMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author zanchi_r
 */
@Controller
public class TestWorkerController {
    @RequestMapping(path = "/worker/test", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> testWorker() {
        return new ResponseEntity(new SuccessMessageResponse("success"),
                HttpStatus.OK);
    }
}