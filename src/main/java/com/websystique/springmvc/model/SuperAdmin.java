package com.websystique.springmvc.model;

import java.io.Serializable;
import javax.persistence.*;
import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.types.BaseUser;

/**
 * Created by yangyma on 11/24/16.
 */
@Entity
@Table(name = "super_admin")
public class SuperAdmin extends BaseUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Integer privilege;

    public SuperAdmin() {
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

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

}
