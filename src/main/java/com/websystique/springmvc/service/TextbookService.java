package com.websystique.springmvc.service;

import com.websystique.springmvc.model.Teacher;
import com.websystique.springmvc.model.Textbook;

import java.util.List;

/**
 * Created by yangyma on 11/27/16.
 */
public interface TextbookService {

    Textbook findById(Long id);

    List<Textbook> findByTeacher(Teacher teacher);
}
