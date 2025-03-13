package work.dduo.ans.model.dto;

import lombok.*;
import work.dduo.ans.model.vo.request.AddSentenceReq;
import work.dduo.ans.model.vo.request.AddTagsReq;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddSentenceDTO {
    // 正文
    private AddSentenceReq addSentenceReq;
    // 标签集合
    private List<AddTagsReq> tagsList;
}
