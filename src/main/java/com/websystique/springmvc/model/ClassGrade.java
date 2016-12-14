package com.websystique.springmvc.model;

import java.util.List;
import javax.persistence.*;

/**
 * Created by yangyma on 11/22/16.
 */
@Entity
@Table(name = "class")
public class ClassGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "class")
    private Integer clazz;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher inChargeTeacher;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "classGrade")
    private List<Course> courses;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "classGrade")
    private List<Student> students;

    public ClassGrade() {
    }

    public ClassGrade(Integer grade, Integer clazz, School school) {
        this.grade = grade;
        this.clazz = clazz;
        this.school = school;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getClazz() {
        return clazz;
    }

    public void setClazz(Integer clazz) {
        this.clazz = clazz;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Teacher getInChargeTeacher() {
        return inChargeTeacher;
    }

    public void setInChargeTeacher(Teacher inChargeTeacher) {
        this.inChargeTeacher = inChargeTeacher;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
