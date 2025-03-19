-- 删除一条句子 带事务控制
START TRANSACTION;
-- 先执行删除
DELETE FROM t_sentences WHERE id = 87;
-- 验证影响行数
SELECT ROW_COUNT();
-- 确认无误后提交
COMMIT;
-- 若发现问题可回滚
ROLLBACK;