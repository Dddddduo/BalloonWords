package work.dduo.ans.model.vo.response;

import lombok.Data;

import java.util.List;

// 脱敏
@Data
public class GetRespVO {

    // 句子部分
    private String content;

    // 标签部分
    private List<String> tagName;

}
