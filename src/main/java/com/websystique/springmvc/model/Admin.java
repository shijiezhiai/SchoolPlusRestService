package com.websystique.springmvc.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.websystique.springmvc.types.BaseUser;

/**
 * Created by kevin on 2016/11/22.
 */
@Entity
@Table(name = "admin")
public class Admin extends BaseUser implements Serializable {

    @GeneratedValue
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "passwd_md5")
    private String passwdMd5;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "privilege")
    private Short privilege;

    @Column(name = "audited")
    private Boolean isAudited;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "auditor_id")
    private  SuperAdmin auditor;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auditor")
    List<SchoolSuper> schoolSupers;

    public Admin() {
    }

    public Admin(String username, String passwdMd5, String mobilePhoneNumber, String emailAddress, Short privilege) {
        this.username = username;
        this.passwdMd5 = passwdMd5;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.emailAddress = emailAddress;
        this.privilege = privilege;
        this.isAudited = false;
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

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Short getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Short privilege) {
        this.privilege = privilege;
    }

}
