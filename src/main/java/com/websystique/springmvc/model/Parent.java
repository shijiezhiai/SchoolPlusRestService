package com.websystique.springmvc.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import com.websystique.springmvc.types.BaseUser;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Created by kevin on 2016/11/23.
 */
@Entity
@Table(name = "parent")
public class Parent extends BaseUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String username;

    @Column(name = "passwd_md5")
    private String passwdMd5;

    @Column(name = "email")
    private String emailAddress;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Student> students;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "after_school_video_parent_relation",
        joinColumns = {@JoinColumn(name = "after_school_video_id")},
        inverseJoinColumns = {@JoinColumn(name = "parent_id")}
    )
    private List<AfterSchoolVideo> boughtVideos;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "school_video_parent_relation",
            joinColumns = {@JoinColumn(name = "school_video_id")},
            inverseJoinColumns = {@JoinColumn(name = "parent_id")}
    )
    private List<SchoolVideo> watchedVideos;

    @Column(name = "can_push")
    private Boolean canPush;

    @Column(name = "coins")
    private Integer numCoins;

    public Parent() {
    }

    public Parent(String username, String passwdMd5, String emailAddress, String mobilePhoneNumber) {
        this.username = username;
        this.passwdMd5 = passwdMd5;
        this.emailAddress = emailAddress;
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswdMd5() {
        return passwdMd5;
    }

    public void setPasswdMd5(String passwdMd5) {
        this.passwdMd5 = passwdMd5;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<AfterSchoolVideo> getBoughtVideos() {
        return boughtVideos;
    }

    public void setBoughtVideos(List<AfterSchoolVideo> boughtVideos) {
        this.boughtVideos = boughtVideos;
    }

    public List<SchoolVideo> getWatchedVideos() {
        return watchedVideos;
    }

    public void setWatchedVideos(List<SchoolVideo> watchedVideos) {
        this.watchedVideos = watchedVideos;
    }

    public Boolean getCanPush() {
        return canPush;
    }

    public void setCanPush(Boolean canPush) {
        this.canPush = canPush;
    }

    public Integer getNumCoins() {
        return numCoins;
    }

    public void setNumCoins(Integer numCoins) {
        this.numCoins = numCoins;
    }
}
