package work.dduo.ans.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_sentence_tag
 */
@Data
public class TSentenceTag implements Serializable {
    /**
     * 句子id
     */
    private Integer sentenceId;

    /**
     * 标签id
     */
    private Integer tagId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TSentenceTag other = (TSentenceTag) that;
        return (this.getSentenceId() == null ? other.getSentenceId() == null : this.getSentenceId().equals(other.getSentenceId()))
            && (this.getTagId() == null ? other.getTagId() == null : this.getTagId().equals(other.getTagId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSentenceId() == null) ? 0 : getSentenceId().hashCode());
        result = prime * result + ((getTagId() == null) ? 0 : getTagId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sentenceId=").append(sentenceId);
        sb.append(", tagId=").append(tagId);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}