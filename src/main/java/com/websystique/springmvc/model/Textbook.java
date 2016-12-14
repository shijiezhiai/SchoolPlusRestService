package com.websystique.springmvc.model;

import javax.persistence.*;

/**
 * Created by kevin on 2016/11/23.
 */
@Entity
@Table(name = "textbook")
public class Textbook {

    @Id
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Double price;

    @Column(name = "num_pages")
    private Short numPages;

    @Column(name = "press")
    private String press;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_id")
    private TextbookPicture cover;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "uploader_id")
    private Teacher uploader;

    public Textbook() {
    }

    public Textbook(String isbn, String title, Double price, Short numPages, String press,
                    TextbookPicture cover, Teacher uploader) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.numPages = numPages;
        this.press = press;
        this.cover = cover;
        this.uploader = uploader;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Short getNumPages() {
        return numPages;
    }

    public void setNumPages(Short numPages) {
        this.numPages = numPages;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public TextbookPicture getCover() {
        return cover;
    }

    public void setCover(TextbookPicture cover) {
        this.cover = cover;
    }

    public Teacher getUploader() {
        return uploader;
    }

    public void setUploader(Teacher uploader) {
        this.uploader = uploader;
    }
}
