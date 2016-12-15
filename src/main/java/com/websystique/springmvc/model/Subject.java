package com.websystique.springmvc.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by kevin on 2016/12/4.
 */
@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subject")
    private List<Article> articles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subject")
    private List<SchoolVideo> schoolVideos;

    public Subject() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SchoolVideo> getSchoolVideos() {
        return schoolVideos;
    }

    public void setSchoolVideos(List<SchoolVideo> schoolVideos) {
        this.schoolVideos = schoolVideos;
    }
}
