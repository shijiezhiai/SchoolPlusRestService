package com.websystique.springmvc.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import com.websystique.springmvc.types.BaseUser;

/**
 * Created by yangyma on 11/22/16.
 */
@Entity
@Table(name = "school_admin")
public class SchoolAdmin extends BaseUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "passwd_md5")
    private String passwdMd5;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String emailAddress;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "privilege")
    private Integer privilege;

    @Column(name = "audited")
    private Boolean isAudited;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "auditor_id")
    private SchoolSuper auditor;

    public SchoolAdmin() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }
}
