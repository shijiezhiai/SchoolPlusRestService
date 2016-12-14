package com.websystique.springmvc.model;

import javax.persistence.*;

/**
 * Created by kevin on 2016/11/23.
 */
@Entity
@Table(name = "textbook_picture")
public class TextbookPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "src")
    private String src;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "textbook_isbn")
    private Textbook textbook;

    @Column(name = "height")
    private Short height;

    @Column(name = "width")
    private Short width;

    public TextbookPicture() {
    }

    public TextbookPicture(String src, Textbook textbook, Short height, Short width) {
        this.src = src;
        this.textbook = textbook;
        this.height = height;
        this.width = width;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Textbook getTextbook() {
        return textbook;
    }

    public void setTextbook(Textbook textbook) {
        this.textbook = textbook;
    }

    public Short getHeight() {
        return height;
    }

    public void setHeight(Short height) {
        this.height = height;
    }

    public Short getWidth() {
        return width;
    }

    public void setWidth(Short width) {
        this.width = width;
    }
}