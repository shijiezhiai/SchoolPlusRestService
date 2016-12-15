package com.websystique.springmvc.service.implementation;

import com.google.gson.Gson;
import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.Teacher;
import com.websystique.springmvc.model.Textbook;
import com.websystique.springmvc.repository.TextbookRepository;
import com.websystique.springmvc.service.TextbookService;
import com.websystique.springmvc.util.redis.JCacheTools;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyma on 11/27/16.
 */
@Service
public class TextbookServiceImpl implements TextbookService {

    @Autowired
    TextbookRepository textbookRepository;

    @Autowired
    JCacheTools jCacheTools;


    @Override
    public Textbook findById(Long id) {
        String textbookIdKey = RedisKeyUtils.textbookIdKey(id);
        Gson gson = new Gson();
        if (jCacheTools.existKey(textbookIdKey)) {
            return gson.fromJson(jCacheTools.getStringFromJedis(textbookIdKey), Textbook.class);
        } else {
            Textbook textbook = textbookRepository.findOne(id);
            if (textbook != null) {
                // Add textbook to textbook id -> textbook cache
                jCacheTools.addStringToJedis(textbookIdKey, gson.toJson(textbook), Constants.TEXTBOOK_EXPIRE_TIME)
                // Add textbook to teacher id -> textbook id list cache
                // Add textbook to all the students of all his/her teaching courses
            }
        }
    }

    @Override
    public List<Textbook> findByTeacher(Teacher teacher) {
        String teacherTextbookIdsKey = RedisKeyUtils.teacherTextbookIdsKey(teacher.getId());
        List<Textbook> textbooks = null;
        if (jCacheTools.existKey(teacherTextbookIdsKey)) {
            List<String> textbookIdStrs = jCacheTools.getListFromJedis(teacherTextbookIdsKey);
            textbooks = new ArrayList<>(textbookIdStrs.size());
            for (String textbookIdStr : textbookIdStrs) {
                Textbook textbook = findById(Long.parseLong(textbookIdStr));
                if (textbook != null) {
                    textbooks.add(textbook);
                }
            }
        }

        return textbooks;
    }
}
