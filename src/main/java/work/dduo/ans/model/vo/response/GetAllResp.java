package work.dduo.ans.model.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class GetAllResp {
    private Integer id;
    private String content;
    private Date createTime;
    private String from;
    private String hot;
    private String other1;
    private String other2;
    private String other3;
}
