package work.dduo.ans.model.vo.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddSentenceTagReq {
    @TableId(type = IdType.AUTO)
    private Long batchInsertTags;

    private Long sentenceId;

    private List<AddTagsReq> tagsList;
}
