package work.dduo.ans.model.vo.request;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddSentenceReq {
    // 正文
    private String context;
    // 标签集合
    private List<TagsReq> tagsList;
    // 来源(作者)
    private String from;
}

