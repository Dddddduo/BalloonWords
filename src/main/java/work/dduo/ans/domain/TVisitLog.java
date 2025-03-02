package work.dduo.ans.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_visit_log
 */
@TableName(value ="t_visit_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TVisitLog implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 访问ip
     */
    private String ipAddress;

    /**
     * 访问地址
     */
    private String ipSource;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 访问时间
     * 使用mybatis-plus的元对象处理器自动填充
     */
    @TableField(fill=FieldFill.INSERT)
    private Date createTime;

    private static final long serialVersionUID = 1L;

}