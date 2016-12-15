package com.websystique.springmvc.service.implementation;

import com.google.gson.Gson;
import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.ClassGrade;
import com.websystique.springmvc.model.Notification;
import com.websystique.springmvc.model.School;
import com.websystique.springmvc.model.Student;
import com.websystique.springmvc.repository.NotificationRepository;
import com.websystique.springmvc.service.NotificationService;
import com.websystique.springmvc.util.redis.JCacheTools;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/12/3.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    JCacheTools jCacheTools;


    @Override
    public Notification findById(Long id) {
        String notificationStr = jCacheTools.getStringFromJedis(
                RedisKeyUtils.notificationIdKey(id)
        );
        if (notificationStr != null) {
            Gson gson = new Gson();
            return gson.fromJson(notificationStr, Notification.class);
        } else {
            return notificationRepository.findById(id);
        }
    }

    @Override
    public List<Notification> findByStudent(Student student) {

        List<String> cachedNotificationIds = jCacheTools.getListFromJedis(
                RedisKeyUtils.studentNotificationIdsKey(student.getId()));
        if (cachedNotificationIds != null) {
            return getWithIds(cachedNotificationIds);
        } else {
            List<Notification> notifications = notificationRepository.findByStudent(student);
            Gson gson = new Gson();
            for (Notification notification : notifications) {
                jCacheTools.addStringToJedis(
                        RedisKeyUtils.notificationIdKey(notification.getId()),
                        gson.toJson(notification),
                        Constants.NOTIFICATION_REDIS_EXPIRE_TIME);
            }

            return notifications;
        }
    }

    @Override
    public List<Notification> findByClassGrade(ClassGrade classGrade) {

        List<String> cachedNotificationIds = jCacheTools.getListFromJedis(
                RedisKeyUtils.classGradeNotificationIdsKey(classGrade.getId()));
        if (cachedNotificationIds != null) {
            return getWithIds(cachedNotificationIds);
        } else {
            List<Notification> notifications = notificationRepository.findByClassGrade(classGrade);
            addNotificationsToCache(
                    RedisKeyUtils.classGradeNotificationIdsKey(classGrade.getId()),
                    notifications);

            return notifications;
        }
    }

    @Override
    public List<Notification> findBySchool(School school) {

        List<String> cachedNotificationIds = jCacheTools.getListFromJedis(
                RedisKeyUtils.schoolNotificationIdsKey(school.getId()));
        if (cachedNotificationIds != null) {
            return getWithIds(cachedNotificationIds);
        } else {
            List<Notification> notifications = notificationRepository.findBySchool(school);
            addNotificationsToCache(
                    RedisKeyUtils.schoolNotificationIdsKey(school.getId()), notifications);

            return notifications;
        }
    }

    private List<Notification> getWithIds(List<String> ids) {
        List<Notification> notifications = new ArrayList<>();
        for (String idStr : ids) {
            Long id = Long.parseLong(idStr);
            Notification notification = findById(id);
            if (notification != null) {
                notifications.add(notification);
            }
        }

        return notifications;

    }

    private void addNotificationsToCache(String key, List<Notification> notifications) {

        List<String> notificationIds = new ArrayList<>(notifications.size());
        for (Notification notification : notifications) {
            notificationIds.add(notification.getId().toString());
            Gson gson = new Gson();
            String notificationJson = gson.toJson(notification);
            jCacheTools.addStringToJedis(
                    RedisKeyUtils.notificationIdKey(notification.getId()),
                    notificationJson, Constants.NOTIFICATION_REDIS_EXPIRE_TIME);
        }
        jCacheTools.addListToJedis(
                key, notificationIds, Constants.NOTIFICATION_REDIS_EXPIRE_TIME);
    }
}
