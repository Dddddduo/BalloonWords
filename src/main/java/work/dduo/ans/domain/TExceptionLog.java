package work.dduo.ans.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_exception_log
 */
@TableName(value ="t_exception_log")
public class TExceptionLog implements Serializable {
    /**
     * 异常id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 异常模块
     */
    private String module;

    /**
     * 异常uri
     */
    private String uri;

    /**
     * 异常名称
     */
    private String name;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 异常方法
     */
    private String errorMethod;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作ip
     */
    private String ipAddress;

    /**
     * 操作地址
     */
    private String ipSource;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 异常id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 异常id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 异常模块
     */
    public String getModule() {
        return module;
    }

    /**
     * 异常模块
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * 异常uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 异常uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 异常名称
     */
    public String getName() {
        return name;
    }

    /**
     * 异常名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 操作描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 操作描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 异常方法
     */
    public String getErrorMethod() {
        return errorMethod;
    }

    /**
     * 异常方法
     */
    public void setErrorMethod(String errorMethod) {
        this.errorMethod = errorMethod;
    }

    /**
     * 异常信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 异常信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 请求参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 请求参数
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 请求方式
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * 请求方式
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * 操作ip
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 操作ip
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * 操作地址
     */
    public String getIpSource() {
        return ipSource;
    }

    /**
     * 操作地址
     */
    public void setIpSource(String ipSource) {
        this.ipSource = ipSource;
    }

    /**
     * 操作系统
     */
    public String getOs() {
        return os;
    }

    /**
     * 操作系统
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * 浏览器
     */
    public String getBrowser() {
        return browser;
    }

    /**
     * 浏览器
     */
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    /**
     * 操作时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 操作时间
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
        TExceptionLog other = (TExceptionLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getModule() == null ? other.getModule() == null : this.getModule().equals(other.getModule()))
            && (this.getUri() == null ? other.getUri() == null : this.getUri().equals(other.getUri()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getErrorMethod() == null ? other.getErrorMethod() == null : this.getErrorMethod().equals(other.getErrorMethod()))
            && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()))
            && (this.getParams() == null ? other.getParams() == null : this.getParams().equals(other.getParams()))
            && (this.getRequestMethod() == null ? other.getRequestMethod() == null : this.getRequestMethod().equals(other.getRequestMethod()))
            && (this.getIpAddress() == null ? other.getIpAddress() == null : this.getIpAddress().equals(other.getIpAddress()))
            && (this.getIpSource() == null ? other.getIpSource() == null : this.getIpSource().equals(other.getIpSource()))
            && (this.getOs() == null ? other.getOs() == null : this.getOs().equals(other.getOs()))
            && (this.getBrowser() == null ? other.getBrowser() == null : this.getBrowser().equals(other.getBrowser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getModule() == null) ? 0 : getModule().hashCode());
        result = prime * result + ((getUri() == null) ? 0 : getUri().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getErrorMethod() == null) ? 0 : getErrorMethod().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((getParams() == null) ? 0 : getParams().hashCode());
        result = prime * result + ((getRequestMethod() == null) ? 0 : getRequestMethod().hashCode());
        result = prime * result + ((getIpAddress() == null) ? 0 : getIpAddress().hashCode());
        result = prime * result + ((getIpSource() == null) ? 0 : getIpSource().hashCode());
        result = prime * result + ((getOs() == null) ? 0 : getOs().hashCode());
        result = prime * result + ((getBrowser() == null) ? 0 : getBrowser().hashCode());
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
        sb.append(", module=").append(module);
        sb.append(", uri=").append(uri);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", errorMethod=").append(errorMethod);
        sb.append(", message=").append(message);
        sb.append(", params=").append(params);
        sb.append(", requestMethod=").append(requestMethod);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append(", ipSource=").append(ipSource);
        sb.append(", os=").append(os);
        sb.append(", browser=").append(browser);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}