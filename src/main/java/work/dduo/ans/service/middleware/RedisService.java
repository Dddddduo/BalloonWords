package work.dduo.ans.service.middleware;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import work.dduo.ans.model.vo.response.GetAllResp;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis服务接口实现类
 *
 * @author Dduo
 */
@Service
@SuppressWarnings("all")
public class RedisService {

    // 注入 RedisTemplate
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置 Redis 键的过期时间
     *
     * @param key       Redis 键
     * @param timeout   过期时间
     * @param timeUnit  时间单位
     * @return 是否成功设置过期时间
     */
    public Boolean setExpire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取 Redis 键的剩余过期时间
     *
     * @param key       Redis 键
     * @param timeUnit  时间单位
     * @return 剩余过期时间（单位为 timeUnit）
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 获取符合给定模式的所有 Redis 键
     *
     * @param pattern 键的模式（可以使用通配符）
     * @return 键的集合
     */
    public Collection<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 检查 Redis 中是否存在指定的键
     *
     * @param key Redis 键
     * @return 是否存在该键
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置 Redis 中的字符串类型数据
     *
     * @param key   Redis 键
     * @param value 存储的值
     * @param <T>   值的类型
     */
    public <T> void setObject(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置 Redis 中的字符串类型数据，并设置过期时间
     *
     * @param key       Redis 键
     * @param value     存储的值
     * @param timeout   过期时间
     * @param timeUnit  时间单位
     * @param <T>       值的类型
     */
    public <T> void setObject(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获取 Redis 中的字符串类型数据
     *
     * @param key Redis 键
     * @param <T> 值的类型
     * @return Redis 存储的值
     */
    public <T> T getObject(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除 Redis 中的字符串类型数据
     *
     * @param key Redis 键
     * @return 删除成功返回 true，否则返回 false
     */
    public Boolean deleteObject(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除 Redis 中的字符串类型数据（支持批量删除）
     *
     * @param keys Redis 键的集合
     * @return 被删除的键的数量
     */
    public Long deleteObject(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 对 Redis 中的字符串类型值进行递增操作
     *
     * @param key   Redis 键
     * @param delta 增加的值
     * @return 增加后的值
     */
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 对 Redis 中的字符串类型值进行递减操作
     *
     * @param key   Redis 键
     * @param delta 减少的值
     * @return 减少后的值
     */
    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, -delta);
    }

    /**
     * 设置 Redis 中哈希类型的数据
     *
     * @param key     Redis 键
     * @param hashKey 哈希键
     * @param value   存储的值
     * @param <T>     值的类型
     */
    public <T> void setHash(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 设置 Redis 中哈希类型的数据，并设置过期时间
     *
     * @param key       Redis 键
     * @param hashKey   哈希键
     * @param value     存储的值
     * @param timeout   过期时间
     * @param timeUnit  时间单位
     * @param <T>       值的类型
     * @return 是否成功设置过期时间
     */
    public <T> Boolean setHash(String key, String hashKey, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return setExpire(key, timeout, timeUnit);
    }

    /**
     * 设置 Redis 中哈希类型的数据（批量设置）
     *
     * @param key Redis 键
     * @param map 存储的键值对集合
     * @param <T> 值的类型
     */
    public <T> void setHashAll(String key, Map<String, T> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 设置 Redis 中哈希类型的数据（批量设置），并设置过期时间
     *
     * @param key       Redis 键
     * @param map       存储的键值对集合
     * @param timeout   过期时间
     * @param timeUnit  时间单位
     * @param <T>       值的类型
     * @return 是否成功设置过期时间
     */
    public <T> Boolean setHashAll(String key, Map<String, T> map, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, map);
        return setExpire(key, timeout, timeUnit);
    }

    /**
     * 获取 Redis 中哈希类型的数据
     *
     * @param key     Redis 键
     * @param hashKey 哈希键
     * @param <T>     值的类型
     * @return Redis 存储的值
     */
    public <T> T getHash(String key, String hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取 Redis 中哈希类型的所有数据
     *
     * @param key Redis 键
     * @return 键值对集合
     */
    public Map getHashAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除 Redis 中哈希类型的数据
     *
     * @param key       Redis 键
     * @param hashKeys  哈希键
     * @param <T>       值的类型
     */
    public <T> void deleteHash(String key, T... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断 Redis 中是否存在指定哈希类型的数据
     *
     * @param key     Redis 键
     * @param hashKey 哈希键
     * @return 是否存在指定哈希键
     */
    public Boolean hasHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 对 Redis 中哈希类型的数据进行递增操作
     *
     * @param key     Redis 键
     * @param hashKey 哈希键
     * @param delta   增加的值
     * @return 增加后的值
     */
    public Long incrHash(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 对 Redis 中哈希类型的数据进行递减操作
     *
     * @param key     Redis 键
     * @param hashKey 哈希键
     * @param delta   减少的值
     * @return 减少后的值
     */
    public Long decrHash(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    /**
     * 将数据添加到 Redis 中的列表类型
     *
     * @param key   Redis 键
     * @param value 存储的值
     * @param <T>   值的类型
     * @return 添加后的列表长度
     */
    public <T> Long setList(String key, T value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将数据添加到 Redis 中的列表类型，并设置过期时间
     *
     * @param key       Redis 键
     * @param value     存储的值
     * @param timeout   过期时间
     * @param timeUnit  时间单位
     * @param <T>       值的类型
     * @return 添加后的列表长度
     */
    public <T> Long setList(String key, T value, long timeout, TimeUnit timeUnit) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        setExpire(key, timeout, timeUnit);
        return count;
    }

    /**
     * 将数据批量添加到 Redis 中的列表类型
     *
     * @param key    Redis 键
     * @param values 存储的值集合
     * @param <T>    值的类型
     * @return 添加后的列表长度
     */
    public <T> Long setListAll(String key, T... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 将数据批量添加到 Redis 中的列表类型，并设置过期时间
     *
     * @param key       Redis 键
     * @param timeout   过期时间
     * @param timeUnit  时间单位
     * @param values    存储的值集合
     * @param <T>       值的类型
     * @return 添加后的列表长度
     */
    public <T> Long setListAll(String key, long timeout, TimeUnit timeUnit, T... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        setExpire(key, timeout, timeUnit);
        return count;
    }

    /**
     * 获取 Redis 中列表类型数据的指定范围（索引）
     *
     * @param key   Redis 键
     * @param start 起始索引
     * @param end   结束索引
     * @param <T>   值的类型
     * @return 指定范围内的列表数据
     */
    public <T> List<T> getList(String key, long start, long end) {
        // TODO 要做空值判断 这边如果没有数据返回的集合大小是0 而不是返回一个null
        List<T> result = (List<T>) redisTemplate.opsForList().range(key, start, end);
        if(result.size()==0){
            return null;
        }else return result;
    }

    /**
     * 获取 Redis 中列表类型数据的指定索引值
     *
     * @param key   Redis 键
     * @param index 索引值
     * @param <T>   值的类型
     * @return 列表中指定索引的数据
     */
    public <T> T getListByIndex(String key, long index) {
        return (T) redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取 Redis 中列表类型的大小
     *
     * @param key Redis 键
     * @return 列表长度
     */
    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 删除 Redis 中列表类型的指定数据
     *
     * @param key   Redis 键
     * @param count 删除的数量
     * @param value 要删除的值
     * @param <T>   值的类型
     * @return 删除的数量
     */
    public <T> Long deleteList(String key, long count, T value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 将数据添加到 Redis 中的集合类型
     *
     * @param key    Redis 键
     * @param values 存储的值集合
     * @param <T>    值的类型
     * @return 添加的元素数量
     */
    public <T> Long setSet(String key, T... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 将数据添加到 Redis 中的集合类型，并设置过期时间
     *
     * @param key       Redis 键
     * @param timeout   过期时间
     * @param timeUnit  时间单位
     * @param values    存储的值集合
     * @param <T>       值的类型
     * @return 添加的元素数量
     */
    public <T> Long setSet(String key, long timeout, TimeUnit timeUnit, T... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        setExpire(key, timeout, timeUnit);
        return count;
    }

    /**
     * 获取 Redis 中集合类型的数据
     *
     * @param key Redis 键
     * @param <T> 值的类型
     * @return 集合数据
     */
    public <T> Set<T> getSet(String key) {
        Set<T> result = (Set<T>) redisTemplate.opsForSet().members(key);
        return result;
    }

    /**
     * 删除 Redis 中集合类型的数据
     *
     * @param key    Redis 键
     * @param values 要删除的值集合
     * @param <T>    值的类型
     * @return 删除的元素数量
     */
    public <T> Long deleteSet(String key, T... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 判断 Redis 中集合类型的数据是否包含指定的值
     *
     * @param key   Redis 键
     * @param value 要检查的值
     * @param <T>   值的类型
     * @return 是否包含指定的值
     */
    public <T> Boolean hasSetValue(String key, T value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取 Redis 中集合类型数据的大小
     *
     * @param key Redis 键
     * @return 集合长度
     */
    public Long getSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 对 Redis 中的有序集合进行递增操作
     *
     * @param key   Redis 键
     * @param value 元素值
     * @param score 增加的分值
     * @param <T>   值的类型
     * @return 增加后的分值
     */
    public <T> Double incrZet(String key, T value, Double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 对 Redis 中的有序集合进行递减操作
     *
     * @param key   Redis 键
     * @param value 元素值
     * @param score 减少的分值
     * @param <T>   值的类型
     * @return 减少后的分值
     */
    public <T> Double decrZet(String key, T value, Double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, -score);
    }

    /**
     * 删除 Redis 中有序集合的数据
     *
     * @param key    Redis 键
     * @param values 要删除的元素值
     * @param <T>    值的类型
     * @return 删除的元素数量
     */
    public <T> Long deleteZetScore(String key, T... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 获取 Redis 中有序集合的指定范围内的数据（按分值倒序）
     *
     * @param key   Redis 键
     * @param start 起始索引
     * @param end   结束索引
     * @return 元素及其分值的集合
     */
    public Map<Object, Double> zReverseRangeWithScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end)
                .stream()
                .collect(Collectors.toMap(ZSetOperations.TypedTuple::getValue, ZSetOperations.TypedTuple::getScore));
    }

    /**
     * 获取 Redis 中有序集合中指定元素的分值
     *
     * @param key   Redis 键
     * @param value 元素值
     * @param <T>   值的类型
     * @return 分值
     */
    public <T> Double getZsetScore(String key, T value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 获取 Redis 中有序集合所有元素及其分值
     *
     * @param key Redis 键
     * @return 元素及其分值的集合
     */
    public Map<Object, Double> getZsetAllScore(String key) {
        return Objects.requireNonNull(redisTemplate.opsForZSet().rangeWithScores(key, 0, -1))
                .stream()
                .collect(Collectors.toMap(ZSetOperations.TypedTuple::getValue, ZSetOperations.TypedTuple::getScore));
    }

}
