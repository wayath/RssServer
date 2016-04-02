/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author anthony
 */
@Entity
@Table(name = "article")
public class Article implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(targetEntity = Feed.class)
    @JoinColumn(name = "id_feed", referencedColumnName = "id")
    private Feed feed;
    @Column(name = "link")
    private String link;
    @Column(name = "title")
    private String title;
    @Column(name = "preview")
    private String previewContent;
    @Column(name = "full_content", columnDefinition="TEXT")
    private String fullContent;
    @ManyToOne(targetEntity = ArticleState.class)
    @JoinColumn(name = "status", referencedColumnName = "status")
    private ArticleState status;
    @Column(name = "pub_date")
    private Timestamp pubDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreviewContent() {
        return previewContent;
    }

    public void setPreviewContent(String previewContent) {
        this.previewContent = previewContent;
    }

    public String getFullContent() {
        return fullContent;
    }

    public void setFullContent(String fullContent) {
        this.fullContent = fullContent;
    }

    public ArticleState getStatus() {
        return status;
    }

    public void setStatus(ArticleState status) {
        this.status = status;
    }

    public Timestamp getPubDate() {
        return pubDate;
    }

    public void setPubDate(Timestamp pubDate) {
        this.pubDate = pubDate;
    }
    
    

}
