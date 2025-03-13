package work.dduo.ans.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_sentences
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="t_sentences")
public class TSentences implements Serializable {
    /**
     * 句子id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 句子内容
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;

    /**
     * 提供者
     */
    private String from;

    /**
     * 热度
     */
    private String hot;

    /**
     * 拓展1
     */
    private String other1;

    /**
     * 拓展2
     */
    private String other2;

    /**
     * 拓展3
     */
    private String other3;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}