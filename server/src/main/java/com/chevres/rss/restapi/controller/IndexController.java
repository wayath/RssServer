package com.chevres.rss.restapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public class IndexController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(ModelMap map) {
        map.put("msg", "Rss Feed Rest Api");
        return "index";
    }

}
