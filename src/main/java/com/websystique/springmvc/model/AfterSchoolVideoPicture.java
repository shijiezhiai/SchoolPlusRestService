package com.websystique.springmvc.model;

import javax.persistence.*;


/**
 * Created by kevin on 2016/11/23.
 */
@Entity
@Table(name = "after_school_video_picture")
public class AfterSchoolVideoPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "src")
    private String src;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id")
    private AfterSchoolVideo video;

    @Column(name = "height")
    private Integer height;

    @Column(name = "width")
    private Integer width;

    public AfterSchoolVideoPicture() {
    }

    public AfterSchoolVideoPicture(String src, AfterSchoolVideo video, Integer height, Integer width) {
        this.src = src;
        this.video = video;
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

    public AfterSchoolVideo getVideo() {
        return video;
    }

    public void setVideo(AfterSchoolVideo video) {
        this.video = video;
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

