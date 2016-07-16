package com.example.Util;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.executor.ExecutorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * Created by Admin on 2016/7/16.
 */
@Service
public class JedisAdapter implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private Jedis jedis = null;
    private JedisPool pool = null;
    private static int TIMEOUT = 10000;
    private static String AUTH = "zcsmart";


    private Jedis getJedis(){
        Jedis jedis = pool.getResource();
        //jedis.auth("zcsmart");
        return jedis;
    }

    public String get(String key) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return getJedis().get(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis != null ){
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            //jedis.auth("zcsmart");
            jedis.set(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            //jedis.auth("zcsmart");
            return jedis.sadd(key,value);
        }catch (Exception e) {
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            //jedis.auth("zcsmart");
            return jedis.srem(key,value);
        }catch (Exception e) {
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            //jedis.auth("zcsmart");
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            //jedis.auth("zcsmart");
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常:"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    // 为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值
    public void setex(String key, String value) {
        // 验证码，防机器注册，记录上次注册的时间，有效期
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            //jedis.auth("zcsmart");
            jedis.setex(key,10,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    // Redis Lpush 命令将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            //jedis.auth("zcsmart");
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    // Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            //jedis.auth("zcsmart");
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if(value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        //config.setMaxActive(MAX_ACTIVE);
        config.setMaxIdle(8);
        //config.setMaxWait(MAX_WAIT);
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "172.25.11.12",6379, TIMEOUT, AUTH);

    }
}
