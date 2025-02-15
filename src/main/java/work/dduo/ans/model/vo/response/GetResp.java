package work.dduo.ans.model.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class GetResp {
    // 句子部分
    private Long id;
    private String content;
    private String from;
    private Boolean hot;
    private Long tagId;
    private String tagFrom;
    private String tags;

}

