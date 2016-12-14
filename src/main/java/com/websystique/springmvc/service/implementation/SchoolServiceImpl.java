package com.websystique.springmvc.service.implementation;

import com.websystique.springmvc.model.School;
import com.websystique.springmvc.model.SchoolSuper;
import com.websystique.springmvc.repository.SchoolRepository;
import com.websystique.springmvc.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    SchoolRepository schoolRepository;

    @Override
    public School findByName(String name) {
        return schoolRepository.findByName(name);
    }

}
