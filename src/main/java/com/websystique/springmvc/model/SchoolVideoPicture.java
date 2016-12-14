package com.websystique.springmvc.model;

import javax.persistence.*;

/**
 * Created by kevin on 2016/11/23.
 */
@Entity
@Table(name = "school_video_picture")
public class SchoolVideoPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "src")
    private String src;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id")
    private SchoolVideo schoolVideo;

    @Column(name = "height")
    private Short height;

    @Column(name = "width")
    private Short width;

    public SchoolVideoPicture() {
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

    public SchoolVideo getSchoolVideo() {
        return schoolVideo;
    }

    public void setSchoolVideo(SchoolVideo schoolVideo) {
        this.schoolVideo = schoolVideo;
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
