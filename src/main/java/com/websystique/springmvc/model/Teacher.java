package com.websystique.springmvc.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import com.websystique.springmvc.types.BaseUser;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Created by kevin on 2016/11/22.
 */
@Entity
@Table(name = "teacher")
public class Teacher extends BaseUser implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "passwd_md5")
    private String passwdMd5;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private Boolean isMale;

    @Column(name = "age")
    private Integer age;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Course> teachingCourses;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "emp_number")
    private String employeeNumber;

    // The class that this teacher is in charge
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "inChargeTeacher")
    @JoinColumn(name = "class_id")
    private ClassGrade inChargeClass;

    @Column(name = "is_head")
    private Boolean isHead;

    @Column(name = "email")
    private String emailAddress;

    @Column(name = "is_audited")
    private boolean isAudited;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "auditor_id")
    private SchoolAdmin auditor;

    @Column(name = "privilege")
    private Integer privilege;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uploader")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SchoolVideo> ownedSchoolVideos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<AfterSchoolVideo> ownedAfterSchoolVideos;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "teacher")
    private List<Homework> homeworkList;

    @Column(name = "num_unpaid")
    private Integer unpaid;

    @Column(name = "total_unpaid")
    private Integer totalPaid;

    public Teacher() {
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

    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Course> getTeachingCourses() {
        return teachingCourses;
    }

    public void setCourse(List<Course> teachingCourses) {
        this.teachingCourses = teachingCourses;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public ClassGrade getInChargeClass() {
        return inChargeClass;
    }

    public void setInChargeClass(ClassGrade inChargeClass) {
        this.inChargeClass = inChargeClass;
    }

    public Boolean getHead() {
        return isHead;
    }

    public void setHead(Boolean head) {
        isHead = head;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean getIsAudited() {
        return isAudited;
    }

    public void setIsAudited(boolean isAudited) {
        this.isAudited = isAudited;
    }

    public SchoolAdmin getAuditor() {
        return auditor;
    }

    public void setAuditor(SchoolAdmin auditor) {
        this.auditor = auditor;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    public List<SchoolVideo> getOwnedVideos() {
        return ownedSchoolVideos;
    }

    public void setOwnedVideos(List<SchoolVideo> ownedVideos) {
        this.ownedSchoolVideos = ownedVideos;
    }

    public boolean isAudited() {
        return isAudited;
    }

    public void setAudited(boolean audited) {
        isAudited = audited;
    }

    public List<SchoolVideo> getOwnedSchoolVideos() {
        return ownedSchoolVideos;
    }

    public void setOwnedSchoolVideos(List<SchoolVideo> ownedSchoolVideos) {
        this.ownedSchoolVideos = ownedSchoolVideos;
    }

    public List<AfterSchoolVideo> getOwnedAfterSchoolVideos() {
        return ownedAfterSchoolVideos;
    }

    public void setOwnedAfterSchoolVideos(List<AfterSchoolVideo> ownedAfterSchoolVideos) {
        this.ownedAfterSchoolVideos = ownedAfterSchoolVideos;
    }

    public Integer getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(Integer unpaid) {
        this.unpaid = unpaid;
    }

    public Integer getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Integer totalPaid) {
        this.totalPaid = totalPaid;
    }

    public void setTeachingCourses(List<Course> teachingCourses) {
        this.teachingCourses = teachingCourses;
    }

    public List<Homework> getHomeworkList() {
        return homeworkList;
    }

    public void setHomeworkList(List<Homework> homeworkList) {
        this.homeworkList = homeworkList;
    }
}
