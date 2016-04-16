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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zanchi_r
 */
public class RssHandler extends DefaultHandler {
    
    private List<Article> articles;
    private Article currentArticle;
    private Feed feed;
    private ArticleState state;
    private StringBuilder stringBuilder;
    private boolean checkType;
    
    public RssHandler(Feed f, ArticleState s) {
        this.feed = f;
        this.state = s;
        this.checkType = true;
    }
    
    @Override
    public void startDocument() {
        this.articles = new ArrayList<Article>();
    }
    
    public List<Article> getResult() {
        return (articles);
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (this.checkType == true && qName.length() != 0 && qName.equals("rss")) {
            this.checkType = false;
        }
        else if (this.checkType == true && qName.length() != 0) {
            throw new RuntimeException();
        }
            
        this.stringBuilder = new StringBuilder();
        if (qName.equals("item") && this.articles != null) {
            this.currentArticle = new Article();
            this.currentArticle.setFeed(this.feed);
            this.currentArticle.setStatus(this.state);
            this.articles.add(this.currentArticle);
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) {
        this.stringBuilder.append(ch, start, length);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (this.currentArticle != null && this.articles != null) {
            try {
                if (qName.equals("content:encoded"))
                    qName = "content";
                switch (qName.toLowerCase()) {
                    case "title":
                        String title = this.stringBuilder.toString();
                        this.currentArticle.setTitle(title);
                        break;
                    case "pubdate":
                        String pubDate = this.stringBuilder.toString();
                        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");//TODO a ameliorer
                        Date date = formatter.parse(pubDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.set(Calendar.MILLISECOND, 0);
                        this.currentArticle.setPubDate(new java.sql.Timestamp(date.getTime()));
                        break;
                    case "link":
                        String link = this.stringBuilder.toString();
                        this.currentArticle.setLink(link);
                        break;
                    case "description":
                        String description = this.stringBuilder.toString();
                        this.currentArticle.setPreviewContent(description);
                        break;
                    case "content":
                        String content = this.stringBuilder.toString();
                        this.currentArticle.setFullContent(content);
                        break;
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
