package com.websystique.springmvc.model;

import javax.persistence.*;

/**
 * Created by kevin on 2016/11/22.
 */
@Entity
@Table(name = "advertisement_picture")
public class AdvertisementPicture {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "src")
    private String src;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id")
    private Advertisement advertisement;

    @Column(name = "height")
    private Integer height;

    @Column(name = "width")
    private Integer width;

    public AdvertisementPicture() {
    }

    public AdvertisementPicture(String src, Advertisement advertisement, Integer height, Integer width) {
        this.src = src;
        this.advertisement = advertisement;
        this.height = height;
        this.width = width;
    }

    public Long getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
