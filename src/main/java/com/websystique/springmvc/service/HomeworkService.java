package com.websystique.springmvc.service;

import com.websystique.springmvc.model.Homework;

import java.util.List;

/**
 * Created by yangyma on 11/27/16.
 */
public interface HomeworkService {
    List<Homework> save(List<Homework> homeworkList);

    Homework update(Long id, Homework homework);

    Homework findById(Long id);

    Boolean deleteById(Long id);
}
