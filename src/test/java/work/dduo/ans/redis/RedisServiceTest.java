package work.dduo.ans.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.dduo.ans.middleware.impl.RedisServiceImpl;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisServiceImpl redisService;

    @Test
    public void testRedisConnection() {
        // 设置一个键值对 
        String key = "testKey";
        String value = "testValue";
        redisService.setObject(key,  value);

        // 获取键值对 
        String retrievedValue = redisService.getObject(key);
        assertEquals(value, retrievedValue);

        // 删除键值对 
        boolean isDeleted = redisService.deleteObject(key);
        assertTrue(isDeleted);

        // 验证键是否已删除 
        boolean keyExists = redisService.hasKey(key);
        assertFalse(keyExists);
    }

    @Test
    public void testSetExpire() {
        String key = "expireKey";
        String value = "expireValue";
        redisService.setObject(key,  value);

        // 设置过期时间 
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        boolean isExpireSet = redisService.setExpire(key,  timeout, timeUnit);
        assertTrue(isExpireSet);

        // 等待过期 
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 验证键是否已过期 
        boolean keyExists = redisService.hasKey(key);
        assertFalse(keyExists);
    }

    @Test
    public void testHashOperations() {
        String key = "hashKey";
        String hashKey = "hashField";
        String value = "hashValue";

        // 设置哈希值 
        redisService.setHash(key,  hashKey, value);

        // 获取哈希值 
        String retrievedValue = redisService.getHash(key,  hashKey);
        assertEquals(value, retrievedValue);

        // 删除哈希值 
        redisService.deleteHash(key,  hashKey);

        // 验证哈希值是否已删除 
        boolean hashKeyExists = redisService.hasHashValue(key,  hashKey);
        assertFalse(hashKeyExists);
    }

    @Test
    public void testListOperations() {
        String key = "listKey";
        String value = "listValue";

        // 添加列表元素 
        long listLength = redisService.setList(key,  value);
        assertEquals(1, listLength);

        // 获取列表元素 
        List<String> list = redisService.getList(key,  0, -1);
        assertEquals(1, list.size());
        assertEquals(value, list.get(0));

        // 删除列表元素 
        long deletedCount = redisService.deleteList(key,  1, value);
        assertEquals(1, deletedCount);

        // 验证列表是否为空 
        long newListLength = redisService.getListSize(key);
        assertEquals(0, newListLength);
    }

    @Test
    public void testSetOperations() {
        String key = "setKey";
        String value = "setValue";

        // 添加集合元素 
        long addedCount = redisService.setSet(key,  value);
        assertEquals(1, addedCount);

        // 获取集合元素 
        Set<String> set = redisService.getSet(key);
        assertEquals(1, set.size());
        assertTrue(set.contains(value));

        // 删除集合元素 
        long deletedCount = redisService.deleteSet(key,  value);
        assertEquals(1, deletedCount);

        // 验证集合是否为空 
        long newSetSize = redisService.getSetSize(key);
        assertEquals(0, newSetSize);
    }

    @Test
    public void testZSetOperations() {
        String key = "zSetKey";
        String value = "zSetValue";
        double score = 1.0;

        // 增加有序集合元素的分值 
        double newScore = redisService.incrZet(key,  value, score);
        assertEquals(score, newScore);

        // 获取有序集合元素的分值 
        double retrievedScore = redisService.getZsetScore(key,  value);
        assertEquals(score, retrievedScore);

        // 删除有序集合元素 
        long deletedCount = redisService.deleteZetScore(key,  value);
        assertEquals(1, deletedCount);

        // 验证有序集合是否为空 
        Map<Object, Double> zSet = redisService.getZsetAllScore(key);
        assertTrue(zSet.isEmpty());
    }
} 