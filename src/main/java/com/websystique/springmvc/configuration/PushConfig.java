package com.websystique.springmvc.configuration;

import com.websystique.springmvc.util.push.PushClient;
import com.websystique.springmvc.util.push.PushUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by kevin on 2016/11/29.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class PushConfig {

    @Autowired
    Environment environment;

    @Bean(name = "pushClient")
    public PushClient pushClient() {
        return new PushClient();
    }

    @Bean(name = "pushUtils")
    public PushUtils getPushUtils() {
        PushUtils pushUtils = new PushUtils();
        pushUtils.setPushClient(pushClient());
        pushUtils.setAppKey(environment.getProperty("umeng_app_key"));
        pushUtils.setAppMasterSecret(environment.getProperty("umeng_app_master_secret"));

        return pushUtils;
    }
}
