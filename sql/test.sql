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

# 将t_sentences_tags表的主键id字段设置自增
ALTER TABLE t_sentence_tag MODIFY id INT AUTO_INCREMENT;

# 将t_sentences_tags表的create_time字段设置为自动填充
ALTER TABLE t_sentence_tag
    MODIFY COLUMN create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 查看当前线程状态
SHOW PROCESSLIST;
