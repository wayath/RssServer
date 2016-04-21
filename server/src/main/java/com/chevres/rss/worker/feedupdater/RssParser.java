/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.worker.feedupdater;

import com.chevres.rss.restapi.dao.ArticleStateDAO;
import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.ArticleState;
import com.chevres.rss.restapi.model.Feed;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 *
 * @author zanchi_r
 */
public class RssParser {
    public RssParser() {
        
    }
    
    public List<Article> parseFeed(Feed feed) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        ArticleStateDAO articleStateDAO = context.getBean(ArticleStateDAO.class);
        ArticleState newArticleState = articleStateDAO.findByLabel("new");
        RssHandler rssHandler = new RssHandler(feed, newArticleState);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            
            InputStream in = this.readFeed(feed);
            InputSource input = new InputSource(in);
            
            reader.setContentHandler(rssHandler);
            reader.parse(input);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        finally {
           context.close(); 
        }
        return (rssHandler.getResult());
    }
    
        private InputStream readFeed(Feed feed) {
        try {
            URL url = new URL(feed.getUrl());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.addRequestProperty("User-Agent", "Mozilla/4.0");

            return http.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
