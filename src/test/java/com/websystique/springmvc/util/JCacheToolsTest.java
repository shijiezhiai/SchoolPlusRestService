package com.websystique.springmvc.util;

import com.websystique.springmvc.util.redis.JCacheTools;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by yangyma on 12/14/16.
 */
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JCacheToolsTest {

    @Autowired
    JCacheTools jCacheTools;

    @Test
    public void testSetGet() {
        jCacheTools.addStringToJedis("test-key", "test-value", 0);
        String value = jCacheTools.getStringFromJedis("test-key");

        Assert.assertTrue(value != null && ! value.isEmpty());
    }
}
