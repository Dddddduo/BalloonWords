USE dduo_yiyan_db;

SELECT t_sentences.id,
       t_sentences.content,
       t_tags.id,
       t_tags.name
FROM t_sentences,
     t_tags,
     t_sentence_tag
WHERE t_sentences.id = t_sentence_tag.id
  AND tag_id = t_sentence_tag.id
LIMIT 1;

#  随机获取一条句子 
SELECT TS.id,
       TS.content,
       TS.`from`,
       TS.hot,
       GROUP_CONCAT(TT.id SEPARATOR ',')   AS tagId,
       GROUP_CONCAT(TT.name SEPARATOR ',') AS tags
FROM t_sentences TS
         LEFT JOIN
     t_sentence_tag TST ON TS.id = TST.sentence_id
         LEFT JOIN
     t_tags TT ON TST.tag_id = TT.id
GROUP BY TS.id, TS.content, TS.`from`, TS.hot
ORDER BY RAND() -- 随机排序
LIMIT 1;
-- 只取一条数据

#  查询所有句子
SELECT *
FROM t_sentences;

#  查询所有标签
SELECT *
FROM t_tags;

#  根据标签查询句子
SELECT TS.id,
       TS.content,
       TS.create_time,
       TS.from,
       TS.hot,
       TS.other1,
       TS.other2,
       TS.other3,
       GROUP_CONCAT(TT.name SEPARATOR ',') AS tags
FROM t_sentences TS
         LEFT JOIN
     t_sentence_tag TST ON TS.id = TST.sentence_id
         LEFT JOIN
     t_tags TT ON TST.tag_id = TT.id
WHERE TT.name IN ('青春')
GROUP BY TS.id, TS.content, TS.create_time, TS.from, TS.hot, TS.other1, TS.other2, TS.other3;

# 将t_sentences表的create_time字段设置为自动填充
ALTER TABLE t_sentences
    MODIFY COLUMN create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 数据库事务 进行多表操作 添加一个句子 同步更新sentence表和中间表sentence_tag表
-- 开始事务
START TRANSACTION;
-- 插入主表数据（假设create_time字段由数据库自动生成）
INSERT INTO t_sentences (content, `from`, hot)
VALUES ('生活不止眼前的苟且，还有诗和远方。', '高晓松', 0);
-- 假设hot默认0，其他字段按需处理

-- 获取新插入句子的自增ID
SET @sentence_id = LAST_INSERT_ID();

-- 批量查询标签ID（确保标签存在）
SELECT id
INTO @tag_ids
FROM t_tags
WHERE `name` IN ('青春', '故事');

-- 插入中间表（批量操作）
INSERT INTO t_sentence_tag (sentence_id, tag_id)
SELECT @sentence_id, id
FROM t_tags
WHERE `name` IN ('青春', '故事');

COMMIT;