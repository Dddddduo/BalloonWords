package work.dduo.ans.model.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
