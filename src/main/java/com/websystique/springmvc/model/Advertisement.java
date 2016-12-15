package com.websystique.springmvc.model;

import javax.persistence.*;

/**
 * Created by kevin on 2016/11/22.
 */
@Entity
@Table(name = "ad")
public class Advertisement {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pic_id")
    private AdvertisementPicture picture;

    @Column(name = "description")
    private String description;

    @Column(name = "advertiser")
    private String advertiser;

    @Column(name = "type")
    private String type;

    public Advertisement() {
    }

    public Long getId() {
        return id;
    }

    public AdvertisementPicture getPicture() {
        return picture;
    }

    public void setPicId(AdvertisementPicture picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
