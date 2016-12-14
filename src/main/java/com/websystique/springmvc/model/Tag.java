package com.websystique.springmvc.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by kevin on 2016/11/23.
 */
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinTable(name = "tag_article_relation",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<Article> articles;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinTable(name = "tag_school_video_relation",
            joinColumns = {@JoinColumn(name = "school_video_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<SchoolVideo> schoolVideos;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinTable(name = "tag_after_school_video_relation",
            joinColumns = {@JoinColumn(name = "after_school_video_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<AfterSchoolVideo> afterSchoolVideos;

    @Column(name = "num_of_entity")
    private Long numEntities;
}
