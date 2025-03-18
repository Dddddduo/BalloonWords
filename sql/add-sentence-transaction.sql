-- 数据库事务 进行多表操作 添加一个句子 同步更新sentence表和中间表sentence_tag表

-- 设置事务的最大执行时间为5000毫秒
SET max_execution_time = 5000;

-- 开始事务
START TRANSACTION;
-- 插入主表数据（假设create_time字段由数据库自动生成）
INSERT INTO t_sentences (content, `from`, hot)
VALUES ('生活不止眼前的苟且，还有诗和远方。', '高晓松', 0);
-- 假设hot默认0，其他字段按需处理

-- 获取新插入句子的自增ID
SET @sentence_id = LAST_INSERT_ID();
-- 插入中间表（批量操作）
-- 插入标签关联信息(使用子查询拆分标签ID)
-- 单次查询完成插入操作
INSERT INTO t_sentence_tag (sentence_id, tag_id)
SELECT
    @sentence_id,  -- 你的句子ID 获取到的自增ID
    id -- 标签id
FROM t_tags
WHERE `name` IN ('青春', '故事');

-- 提交事务
COMMIT;

