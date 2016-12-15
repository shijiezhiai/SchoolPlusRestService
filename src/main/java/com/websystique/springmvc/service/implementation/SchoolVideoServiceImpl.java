package com.websystique.springmvc.service.implementation;

import com.google.gson.Gson;
import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.School;
import com.websystique.springmvc.model.SchoolVideo;
import com.websystique.springmvc.model.Subject;
import com.websystique.springmvc.repository.SchoolVideoRepository;
import com.websystique.springmvc.service.SchoolVideoService;
import com.websystique.springmvc.util.redis.JCacheTools;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
@Service
public class SchoolVideoServiceImpl implements SchoolVideoService {

    @Autowired
    SchoolVideoRepository schoolVideoRepository;

    @Autowired
    JCacheTools jCacheTools;

    @Override
    public List<SchoolVideo> findBySchool(School school) {
        String redisKey = RedisKeyUtils.schoolIdSchoolVideoIdsKey(school.getId());
        if (jCacheTools.existKey(redisKey)) {
            List<String> schoolVideoIdStrs = jCacheTools.getListFromJedis(redisKey);
            List<SchoolVideo> schoolVideos = new ArrayList<>(schoolVideoIdStrs.size());
            for (String schoolVideoIdStr : schoolVideoIdStrs) {
                SchoolVideo schoolVideo = findById(Long.parseLong(schoolVideoIdStr));
                schoolVideos.add(schoolVideo);
            }

            return schoolVideos;
        } else {
            List<SchoolVideo> schoolVideos = schoolVideoRepository.findBySchool(school);
            for (SchoolVideo schoolVideo : schoolVideos) {
                addSchoolVideoToCache(schoolVideo);
            }

            return schoolVideos;
        }
    }

    @Override
    public SchoolVideo findById(Long id) {
        return findById(id, false);
    }

    private SchoolVideo findById(Long id, Boolean forDelete) {
        String redisKey = RedisKeyUtils.schoolVideoIdKey(id);
        if (jCacheTools.existKey(redisKey)) {
            return new Gson().fromJson(
                    jCacheTools.getStringFromJedis(redisKey), SchoolVideo.class);
        } else {
            SchoolVideo schoolVideo = schoolVideoRepository.findOne(id);
            if (schoolVideo != null && schoolVideo.getAudited() && !forDelete) {
                addSchoolVideoToCache(schoolVideo);
            }

            return schoolVideo;
        }
    }

    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        try {
            SchoolVideo schoolVideo = findById(id, true);
            schoolVideoRepository.delete(id);
            removeSchoolVideoFromCache(schoolVideo);
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public List<SchoolVideo> findBySchoolNameAndGradeAndSubject(String schoolName, Integer grade, Subject subject) {
        return schoolVideoRepository.findBySchoolNameAndGradeAndSubject(schoolName, grade, subject);
    }

    @Override
    public List<SchoolVideo> findBySchoolNameAndGrade(String schoolName, Integer grade) {
        return schoolVideoRepository.findBySchoolNameAndGrade(schoolName, grade);
    }

    @Override
    public List<SchoolVideo> findBySchoolNameAndSubject(String schoolName, Subject subject) {
        return schoolVideoRepository.findBySchoolNameAndSubjectt(schoolName, subject);
    }

    @Override
    @Transactional
    public SchoolVideo save(SchoolVideo schoolVideo) {
        SchoolVideo savedVideo = schoolVideoRepository.save(schoolVideo);
        // Add the video to cache only when it is audited
        if (savedVideo != null) {
            if (savedVideo.getAudited()) {
                addSchoolVideoToCache(savedVideo);
            }

            return savedVideo;
        }
        return null;
    }

    private void addSchoolVideoToCache(SchoolVideo schoolVideo) {
        Gson gson = new Gson();

        // Add SchoolVideo id -> video to redis
        String redisKey = RedisKeyUtils.schoolVideoIdKey(schoolVideo.getId());
        jCacheTools.addStringToJedis(redisKey, gson.toJson(schoolVideo), Constants.SCHOOL_VIDEO_EXPIRE_TIME);

        // Add id to school id -> SchoolVideo list redis cache
        redisKey = RedisKeyUtils.schoolIdSchoolVideoIdsKey(schoolVideo.getSchool().getId());
        jCacheTools.pushDataToListJedis(redisKey, schoolVideo.getId().toString(), 0);

        // Add id to teacher id -> SchoolVideo list redis cache
        redisKey = RedisKeyUtils.teacherSchoolVideoIdsKey(schoolVideo.getUploader().getId());
        jCacheTools.pushDataToListJedis(redisKey, schoolVideo.getId().toString(), 0);
    }

    private void removeSchoolVideoFromCache(SchoolVideo schoolVideo) {
        // Add SchoolVideo id -> video to redis
        String redisKey = RedisKeyUtils.schoolVideoIdKey(schoolVideo.getId());
        jCacheTools.delKeyFromJedis(redisKey);

        // Add id to school id -> SchoolVideo list redis cache
        redisKey = RedisKeyUtils.schoolIdSchoolVideoIdsKey(schoolVideo.getSchool().getId());
        jCacheTools.delKeyFromJedis(redisKey);

        // Add id to teacher id -> SchoolVideo list redis cache
        redisKey = RedisKeyUtils.teacherSchoolVideoIdsKey(schoolVideo.getUploader().getId());
        jCacheTools.delKeyFromJedis(redisKey);
    }

}
