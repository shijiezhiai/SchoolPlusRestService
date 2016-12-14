package com.websystique.springmvc.util.redis;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.websystique.springmvc.configuration.RedisConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Created by yangyma on 11/28/16.
 */
@Component
public class JCacheTools {

    @Autowired
    JedisPool jedisPool;

    @Autowired
    RedisConfig redisConfig;

    /**
     * 默认日志打印LOGGER_DEFAULT
     */
    public static Logger LOGGER_DEFAULT = Logger.getLogger("logger_jCache_default");
    /**
     * 失败日志logger，用于定期del指定的key
     */
    public static Logger LOGGER_FAILURE = Logger.getLogger("logger_jCache_failure");

    public Jedis getJedis() throws JedisException {
        Jedis jedis;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            LOGGER_DEFAULT.warn("getJedis fail: ", e);
            throw e;
        }
        return jedis;
    }

    public void release(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public int getDBIndex() {
        return redisConfig.getRedisDatabase();
    }

    public String addStringToJedis(String key, String value, int cacheSeconds) {
        Jedis jedis = null;
        String lastVal = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            lastVal = jedis.getSet(key, value);
            if(cacheSeconds!=0){
                jedis.expire(key,cacheSeconds);
            }
            LOGGER_DEFAULT.debug("addStringToJedis succeed: " + key + "->" + value);
        } catch (Exception e) {
            LOGGER_FAILURE.warn("addStringToJedis failed: " + key + "->" + value, e);
        } finally {
            release(jedis);
        }
        return lastVal;
    }

    public void addStringToJedis(Map<String,String> batchData, int cacheSeconds) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            Pipeline pipeline = jedis.pipelined();
            for(Map.Entry<String,String> element:batchData.entrySet()){
                if(cacheSeconds!=0){
                    pipeline.setex(element.getKey(),cacheSeconds,element.getValue());
                }else{
                    pipeline.set(element.getKey(),element.getValue());
                }
            }
            pipeline.sync();
            LOGGER_DEFAULT.debug("addStringToJedis succeed: " + batchData.size());
        } catch (Exception e) {
            LOGGER_DEFAULT.debug("addStringToJedis fail: " + batchData.size() + ": ", e);
        } finally {
            release(jedis);
        }
    }

    public void addListToJedis(String key, List<String> values, int cacheSeconds) {
        if (values != null && values.size() > 0) {
            Jedis jedis = null;
            try {
                jedis = this.getJedis();
                jedis.select(getDBIndex());
                if (jedis.exists(key)) {
                    jedis.del(key);
                }
                for (String value : values) {
                    jedis.rpush(key, value);
                }
                if(cacheSeconds != 0){
                    jedis.expire(key, cacheSeconds);
                }
                LOGGER_DEFAULT.debug("addListToJedis succeed: " + key + "->"  + values.size());
            } catch (JedisException e) {
                LOGGER_FAILURE.warn("addListToJedis fail: " + key + "->"  + values.size(), e);
            } catch (Exception e) {
                LOGGER_FAILURE.warn("addListToJedis fail: " + key + "->"  + values.size(), e);
            } finally {
                release(jedis);
            }
        }
    }

    public void addToSetJedis(String key, String[] value) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            jedis.sadd(key,value);
            LOGGER_DEFAULT.debug("addToSetJedis succeed: " + key + "->" + value);
        } catch (Exception e) {
            LOGGER_FAILURE.warn("addToSetJedis failed: " + key + "->" + value + ":", e);
        } finally {
            release(jedis);
        }
    }

    public void removeSetJedis(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            jedis.srem(key,value);
            LOGGER_DEFAULT.debug("removeSetJedis succeed: " + key + "->" + value);
        } catch (Exception e) {
            LOGGER_FAILURE.warn("removeSetJedis failed: " + key + "->" + value + ":", e);
        } finally {
            release(jedis);
        }
    }

    public void pushDataToListJedis(
            String key, String data, int cacheSeconds) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            jedis.rpush(key, data);
            if(cacheSeconds!=0){
                jedis.expire(key,cacheSeconds);
            }
            LOGGER_DEFAULT.debug("pushDataToListJedis succeed: " + key + "->" + data);
        } catch (Exception e) {
            LOGGER_FAILURE.warn("pushDataToListJedis failed: " + key + "->" + data + ":", e);
        } finally {
            release(jedis);
        }
    }
    public void pushDataToListJedis(String key,List<String> batchData, int cacheSeconds) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            jedis.del(key);
            jedis.lpush(key,batchData.toArray(new String[batchData.size()]));
            if(cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            LOGGER_DEFAULT.debug("pushDataToListJedis succeed: " + batchData.size());
        } catch (Exception e) {
            LOGGER_FAILURE.warn("pushDataToListJedis failed: " +
                    (batchData != null ? batchData.size() : 0) + ": ", e);
        } finally {
            release(jedis);
        }
    }

    /**
     * 删除list中的元素
     * @param key
     * @param values
     */
    public void deleteDataFromListJedis(String key,List<String> values) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            Pipeline pipeline = jedis.pipelined();
            if(values!=null && !values.isEmpty()){
                for (String val:values){
                    pipeline.lrem(key,0,val);
                }
            }
            pipeline.sync();
            LOGGER_DEFAULT.debug("deleteDataFromListJedis succeed: " +
                    (values != null ? values.size() : 0));
        } catch (Exception e) {
            LOGGER_FAILURE.warn("deleteDataFromListJedis failed: " + (values != null ? values.size() : 0), e);
        } finally {
            release(jedis);
        }
    }

    public void addHashMapToJedis(
            String key, Map<String, String> map, int cacheSeconds,
            boolean isModified) {
        Jedis jedis = null;
        if (map != null && map.size() > 0) {
            try {
                jedis = this.getJedis();
                jedis.select(getDBIndex());
                jedis.hmset(key, map);
                if (cacheSeconds >= 0)
                    jedis.expire(key, cacheSeconds);
                LOGGER_DEFAULT.debug("addHashMapToJedis succeed: " + key + ":" + map.size());
            } catch (Exception e) {
                LOGGER_FAILURE.warn("addHashMapToJedis failed: " +  key + "->" + map.size() + ": ", e);
            } finally {
                release(jedis);
            }
        }
    }

    public void addHashMapToJedis(
            String key, String field, String value, int cacheSeconds) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            if (jedis != null) {
                jedis.select(getDBIndex());
                jedis.hset(key, field, value);
                jedis.expire(key, cacheSeconds);
                LOGGER_DEFAULT.debug("addHashMapToJedis succeed: " + key + field + ":" + value);
            }
        } catch (Exception e) {
            LOGGER_FAILURE.warn("addHashMapToJedis failed: " + key + field + ":" + value + ":", e);
        }finally {
            release(jedis);
        }
    }

    public void updateHashMapToJedis(
            String key, String incrementField, long incrementValue,
            String dateField, String dateValue) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            jedis.hincrBy(key, incrementField, incrementValue);
            jedis.hset(key, dateField, dateValue);
            LOGGER_DEFAULT.debug("updateHashMapToJedis succeed: " + key + "->" +
                    incrementField + ":" + incrementValue);
        } catch (Exception e) {
            LOGGER_FAILURE.warn("updateHashMapToJedis failed: " + key + "->" +
                    incrementField + ":" + incrementValue + ":", e);
        }finally {
            release(jedis);
        }
    }

    public String getStringFromJedis(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = (StringUtils.isNotBlank(value) && ! "nil".equalsIgnoreCase(value)) ?
                        value : null;
                LOGGER_DEFAULT.debug("getStringFromJedis succeed: " + key + "->" + value);
            }
        } catch (Exception e) {
            LOGGER_FAILURE.warn("getStringFromJedis failed: " + key + "->" + value + ":", e);
        } finally {
            release(jedis);
        }
        return value;
    }

    public List<String> getStringFromJedis(String[] keys) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            return jedis.mget(keys);
        } catch (Exception e) {
            LOGGER_FAILURE.warn("getStringFromJedis failed: " + Arrays.toString(keys) + ": ", e);
        } finally {
            release(jedis);
        }
        return null;
    }

    public List<String> getListFromJedis(String key) {
        List<String> values = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            if (jedis.exists(key)) {
                values = jedis.lrange(key, 0, -1);
                LOGGER_DEFAULT.debug("getListFromJedis succeed: " + key + "->" +
                        (values != null ? values.size() : 0));
            }
        } catch (JedisException e) {
            LOGGER_FAILURE.warn("getListFromJedis failed: " + key + "->" +
                    (values != null ? values.size() : 0) + ":", e);
        } finally {
            release(jedis);
        }

        return values;
    }

    public Set<String> getSetFromJedis(String key) {
        Set<String> values = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            if (jedis.exists(key)) {
                values = jedis.smembers(key);
                LOGGER_DEFAULT.debug("getSetFromJedis succeed: " + key + "->" +
                        (values != null ? values.size() : 0));
            }
        } catch (Exception e) {
            LOGGER_FAILURE.warn("getSetFromJedis failed: " + key + "->" +
                    (values != null ? values.size() : 0) + ": ", e);
        } finally {
            release(jedis);
        }

        return values;
    }

    public Map<String, String> getHashMapFromJedis(String key) {
        Map<String, String> hashMap = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            hashMap = jedis.hgetAll(key);
            LOGGER_DEFAULT.debug("getHashMapFromJedis succeed: " + key + "->" +
                    (hashMap != null ? hashMap.size() : 0));
        } catch (Exception e) {
            LOGGER_FAILURE.warn("getHashMapFromJedis failed:" + key + "->" +
                    (hashMap != null ? hashMap.size() : 0) + ": ", e);
        } finally {
            release(jedis);
        }
        return hashMap;
    }

    public String getHashMapValueFromJedis(String key, String field) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            if (jedis != null) {
                jedis.select(getDBIndex());
                if (jedis.exists(key)) {
                    value = jedis.hget(key, field);
                    LOGGER_DEFAULT.debug("getHashMapValueFromJedis succeed: " +
                            key + "->" + field + ":" + value);
                }
            }
        } catch (Exception e) {
            LOGGER_FAILURE.warn(
                    "getHashMapValueFromJedis failed: " + key + "->" + field + ":" + value + ":", e);
        } finally {
            release(jedis);
        }
        return value;
    }

    public Long getIdentifyId(String identifyName) {
        Jedis jedis = null;
        Long identify=null;
        try {
            jedis = this.getJedis();
            if (jedis != null) {
                jedis.select(getDBIndex());
                identify = jedis.incr(identifyName);
                if(identify==0){
                    return jedis.incr(identifyName);
                }else {
                    return identify;
                }
            }
        } catch (Exception e) {
            LOGGER_FAILURE.warn("getIdentifyId" + " failed:" + identifyName + "" + identify + ": ", e);
        } finally {
            release(jedis);
        }
        return null;
    }


    /**
     * 删除某db的某个key值
     * @param key
     * @return
     */
    public Long delKeyFromJedis(String key) {
        Jedis jedis = null;
        long result = 0;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            LOGGER_DEFAULT.debug("delKeyFromJedis succeed");
            return jedis.del(key);
        } catch (Exception e) {
            LOGGER_FAILURE.warn( "delKeyFromJedis failed: ", e);
        } finally {
            release(jedis);
        }
        return result;
    }

    /**
     * 根据dbIndex flushDB每个shard
     */
    public void flushDBFromJedis() {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            jedis.flushDB();
            LOGGER_DEFAULT.debug("flushDBFromJedis succeed");
        } catch (Exception e) {
            LOGGER_FAILURE.warn( "flushDBFromJedis failed: ", e);
        } finally {
            release(jedis);
        }
    }

    public boolean existKey(String key) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.select(getDBIndex());
            LOGGER_DEFAULT.debug("existKey succeed");
            return jedis.exists(key);
        } catch (Exception e) {
            LOGGER_FAILURE.warn("existsKey failed: " + e);
        } finally {
            release(jedis);
        }
        return false;
    }

}
