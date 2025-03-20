package work.dduo.ans.model.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "sentences")
public class GetAllResp {

    @Id
    private Integer id;

    @Field(type = FieldType.Text) // elasticsearch中可能要进行全文搜索的字段
    private String content;

    private Date createTime;

    @Field(type = FieldType.Text) // elasticsearch中可能要进行全文搜索的字段
    private String from;

    private String hot;

    private String other1;

    private String other2;

    private String other3;
}
