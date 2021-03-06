package com.example.service;

import com.example.Util.JedisAdapter;
import com.example.Util.RedisKeyUtil;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Admin on 2016/7/16.
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1:0;
    }

    public long like(int userId, int entityType, int entityId) {
        // 在喜欢集合里添加
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        // 从反对里删除
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityType, int entityId){
        // 在反对集合里增加
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));
        // 从喜欢里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
