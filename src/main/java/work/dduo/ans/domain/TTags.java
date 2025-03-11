package work.dduo.ans.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_tags
 */
@TableName(value ="t_tags")
public class TTags implements Serializable {
    /**
     * 标签id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

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

    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 标签id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 标签名称
     */
    public String getName() {
        return name;
    }

    /**
     * 标签名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 提供者
     */
    public String getFrom() {
        return from;
    }

    /**
     * 提供者
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 热度
     */
    public String getHot() {
        return hot;
    }

    /**
     * 热度
     */
    public void setHot(String hot) {
        this.hot = hot;
    }

    /**
     * 拓展1
     */
    public String getOther1() {
        return other1;
    }

    /**
     * 拓展1
     */
    public void setOther1(String other1) {
        this.other1 = other1;
    }

    /**
     * 拓展2
     */
    public String getOther2() {
        return other2;
    }

    /**
     * 拓展2
     */
    public void setOther2(String other2) {
        this.other2 = other2;
    }

    /**
     * 拓展3
     */
    public String getOther3() {
        return other3;
    }

    /**
     * 拓展3
     */
    public void setOther3(String other3) {
        this.other3 = other3;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

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
        TTags other = (TTags) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getFrom() == null ? other.getFrom() == null : this.getFrom().equals(other.getFrom()))
            && (this.getHot() == null ? other.getHot() == null : this.getHot().equals(other.getHot()))
            && (this.getOther1() == null ? other.getOther1() == null : this.getOther1().equals(other.getOther1()))
            && (this.getOther2() == null ? other.getOther2() == null : this.getOther2().equals(other.getOther2()))
            && (this.getOther3() == null ? other.getOther3() == null : this.getOther3().equals(other.getOther3()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getFrom() == null) ? 0 : getFrom().hashCode());
        result = prime * result + ((getHot() == null) ? 0 : getHot().hashCode());
        result = prime * result + ((getOther1() == null) ? 0 : getOther1().hashCode());
        result = prime * result + ((getOther2() == null) ? 0 : getOther2().hashCode());
        result = prime * result + ((getOther3() == null) ? 0 : getOther3().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", from=").append(from);
        sb.append(", hot=").append(hot);
        sb.append(", other1=").append(other1);
        sb.append(", other2=").append(other2);
        sb.append(", other3=").append(other3);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}