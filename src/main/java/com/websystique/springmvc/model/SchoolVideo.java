package com.websystique.springmvc.model;

import javax.persistence.*;
import java.util.List;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Created by kevin on 2016/11/27.
 */
@Entity
@Table(name = "school_video")
public class SchoolVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "src")
    private String src;

    @Column(name = "description")
    private String description;

    @Column(name = "grade")
    private Integer grade;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schoolVideo")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SchoolVideoPicture> screenshots;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tag_video_relation",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = {@JoinColumn(name = "video_id")}
    )
    private List<Tag> tags;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "uploader_id")
    private Teacher uploader;

    @Column(name = "audited")
    private Boolean isAudited;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "auditor_id")
    private SchoolAdmin auditor;

    public SchoolVideo() {
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<SchoolVideoPicture> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<SchoolVideoPicture> screenshots) {
        this.screenshots = screenshots;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Teacher getUploader() {
        return uploader;
    }

    public void setUploader(Teacher uploader) {
        this.uploader = uploader;
    }

    public SchoolAdmin getAuditor() {
        return auditor;
    }

    public void setAuditor(SchoolAdmin auditor) {
        this.auditor = auditor;
    }

    public Boolean getAudited() {
        return isAudited;
    }

    public void setAudited(Boolean audited) {
        isAudited = audited;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
