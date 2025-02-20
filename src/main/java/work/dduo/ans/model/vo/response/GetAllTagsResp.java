package work.dduo.ans.model.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class GetAllTagsResp {

    private Integer id;
    private String name;
    private String from;
    private String hot;
    private String other1;
    private String other2;
    private String other3;
    private Date createTime;

}
