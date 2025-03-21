package work.dduo.ans.model.vo.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddSentenceReq {
    @TableId(type = IdType.AUTO)
    private Long sentenceId;
    private String content;
    private String from;

}
