package com.websystique.springmvc.model;

import javax.persistence.*;
import java.util.List;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Created by kevin on 2016/11/23.
 */
@Entity
@Table(name = "after_school_video")
public class AfterSchoolVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "src")
    private String src;

    @Column(name = "description")
    private String description;

    @Column(name = "grade")
    private Short grade;

    @Column(name = "course")
    private String course;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "video")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<AfterSchoolVideoPicture> screenshots;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tag_video_relation",
        joinColumns = {@JoinColumn(name = "tag_id")},
        inverseJoinColumns = {@JoinColumn(name = "video_id")}
    )
    private List<Tag> tags;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "video_parent_relation",
        joinColumns = {@JoinColumn(name = "parent_id")},
        inverseJoinColumns = {@JoinColumn(name = "video_id")}
    )
    private List<Parent> buyers;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "uploader_id")
    private Admin uploader;

    @Column(name = "is_free")
    private Boolean isFree;

    @Column(name = "audited")
    private Boolean isAudited;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "auditor_id")
    private SchoolAdmin auditor;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Teacher owner;

    public AfterSchoolVideo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getGrade() {
        return grade;
    }

    public void setGrade(Short grade) {
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public List<Parent> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<Parent> buyers) {
        this.buyers = buyers;
    }

    public Teacher getOwner() {
        return owner;
    }

    public void setOwner(Teacher owner) {
        this.owner = owner;
    }

    public List<AfterSchoolVideoPicture> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<AfterSchoolVideoPicture> screenshots) {
        this.screenshots = screenshots;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Admin getUploader() {
        return uploader;
    }

    public void setUploader(Admin uploader) {
        this.uploader = uploader;
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

    public Boolean getAudited() {
        return isAudited;
    }

    public void setAudited(Boolean audited) {
        isAudited = audited;
    }

    public SchoolAdmin getAuditor() {
        return auditor;
    }

    public void setAuditor(SchoolAdmin auditor) {
        this.auditor = auditor;
    }
}
