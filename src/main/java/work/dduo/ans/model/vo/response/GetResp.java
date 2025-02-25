package work.dduo.ans.model.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class GetResp {
    private Long id;
    private String content;
    private String from;
    private Boolean hot;
    private String tagId;
    private String tags;
}

