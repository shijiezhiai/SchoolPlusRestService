package com.websystique.springmvc.service.implementation;

import com.google.gson.Gson;
import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.Activity;
import com.websystique.springmvc.model.School;
import com.websystique.springmvc.repository.ActivityRepository;
import com.websystique.springmvc.service.ActivityService;
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
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    JCacheTools jCacheTools;


    @Override
    public Activity findById(Long id) {
        String notificationStr = jCacheTools.getStringFromJedis(
                RedisKeyUtils.activityIdKey(id)
        );
        if (notificationStr != null) {
            Gson gson = new Gson();
            return gson.fromJson(notificationStr, Activity.class);
        } else {
            return activityRepository.findById(id);
        }
    }

    @Override
    public List<Activity> findBySchool(School school) {

        List<String> cachedActivityIds = jCacheTools.getListFromJedis(
                RedisKeyUtils.schoolNotificationIdsKey(school.getId()));
        if (cachedActivityIds != null) {
            return getWithIds(cachedActivityIds);
        } else {
            List<Activity> notifications = activityRepository.findBySchool(school);
            addActivitiesToCache(
                    RedisKeyUtils.schoolNotificationIdsKey(school.getId()), notifications);

            return notifications;
        }
    }

    private List<Activity> getWithIds(List<String> ids) {
        List<Activity> activities = new ArrayList<>();
        for (String idStr : ids) {
            Long id = Long.parseLong(idStr);
            Activity activity = findById(id);
            if (activity != null) {
                activities.add(activity);
            }
        }

        return activities;

    }

    private void addActivitiesToCache(String key, List<Activity> activities) {

        List<String> activityIds = new ArrayList<>(activities.size());
        for (Activity activity : activities) {
            activityIds.add(activity.getId().toString());
            Gson gson = new Gson();
            String notificationJson = gson.toJson(activity);
            jCacheTools.addStringToJedis(
                    RedisKeyUtils.activityIdKey(activity.getId()),
                    notificationJson, Constants.NOTIFICATION_REDIS_EXPIRE_TIME);
        }
        jCacheTools.addListToJedis(
                key, activityIds, Constants.NOTIFICATION_REDIS_EXPIRE_TIME);
    }
}
