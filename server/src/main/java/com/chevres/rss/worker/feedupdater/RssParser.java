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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author zanchi_r
 */
public class RssParser {
    
    public RssParser() {
    }
    
    public List<Article> parseFeed(Feed feed) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        List<Article> articles = new ArrayList<Article>();
        String title = "";
        String description = "";
        String link = "";
        String pubdate = "";
        
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = this.readFeed(feed);
            XMLEventReader xmlReader = inputFactory.createXMLEventReader(in);   
            
            ArticleStateDAO articleStateDAO = context.getBean(ArticleStateDAO.class);
            ArticleState newArticleState = articleStateDAO.findByLabel("new");
            
            while (xmlReader.hasNext()) {
                XMLEvent event = xmlReader.nextEvent();
                if (event.isStartElement()) {
                    String elemType = event.asStartElement().getName().getLocalPart();
                    System.out.println(elemType);
                    switch (elemType.toLowerCase()) {
                        case "item":
                            event = xmlReader.nextEvent();
                            break;
                        case "title":
                            title = "comming soon";
                            this.getCharacterData(event, xmlReader);
                            System.out.println(title);
                            break;
                        case "description":
                            description = "comming soon";
                            this.getCharacterData(event, xmlReader);
                            System.out.println(description);
                            break;
                        case "link":
                            link = this.getCharacterData(event, xmlReader);
                            System.out.println(link);
                            break;
                        case "pubdate":
                            pubdate = this.getCharacterData(event, xmlReader);
                            System.out.println(pubdate);
                            break;
                    }
                    
                }
                else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().toLowerCase() == "item") {
                        Article article = new Article();
                        article.setFeed(feed);
                        article.setLink(link);
                        article.setTitle(title);
                        article.setPreviewContent(description);
                        article.setFullContent(description);
                        
                        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");//TODO a ameliorer
                        Date date = formatter.parse(pubdate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.set(Calendar.MILLISECOND, 0);
                        article.setPubDate(new java.sql.Timestamp(date.getTime()));
                        
                        article.setStatus(newArticleState);
                        
                        articles.add(article);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return (articles);
    }
    
    private String getCharacterData(XMLEvent event, XMLEventReader xmlReader)
        throws XMLStreamException {
        String result = "";
        event = xmlReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
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

