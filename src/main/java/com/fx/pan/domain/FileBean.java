package com.fx.pan.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件表
 * @TableName file
 */
@TableName(value ="file")
@Data
public class FileBean implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件原始名称
     */
    private String originName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 是否为目录
     */
    private Integer isDir;

    /**
     * 文件扩展名
     */
    private String fileExt;

    /**
     * 文件大小(单位B)
     */
    private Long fileSize;

    /**
     * 文件类型(0未知,1图片,2文档,3视频,4种子,5音频,6其他)
     */
    private Integer fileType;

    /**
     * 文件md5(用于快速上传)
     */
    private String identifier;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 文件是否共享(0不共享,1共享)
     */
    private Integer isShared;

    /**
     * 文件存储类型 (0:本地存储 1:cos对象存储)
     */
    private Integer storageType;

    /**
     * 文件审核(-1:审核未通过,0:审核中,1:审核通过)
     */
    private Integer audit;

    /**
     * 文件来源(0: 用户上传,1: 文件引用(用户保存的分享文件),2: 离线下载)
     */
    private Integer origin;

    /**
     * 文件的父目录id (根目录下的目录父目录和文件为-1)
     */
    private Long parentPathId;

    /**
     * 文件是否删除(逻辑删除,0:未删除,1:删除)
     */
    private Integer deleted;

    /**
     * 文件上传用户id
     */
    private Long userId;

    /**
     * 文件更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date fileUpdateTime;

    /**
     * 文件创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date fileCreateTime;

    /**
     * 文件版本(在线文档)
     */
    private Integer version;

    @TableField(exist = false)
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
        FileBean other = (FileBean) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getOriginName() == null ? other.getOriginName() == null : this.getOriginName().equals(other.getOriginName()))
            && (this.getFilePath() == null ? other.getFilePath() == null : this.getFilePath().equals(other.getFilePath()))
            && (this.getIsDir() == null ? other.getIsDir() == null : this.getIsDir().equals(other.getIsDir()))
            && (this.getFileExt() == null ? other.getFileExt() == null : this.getFileExt().equals(other.getFileExt()))
            && (this.getFileSize() == null ? other.getFileSize() == null : this.getFileSize().equals(other.getFileSize()))
            && (this.getFileType() == null ? other.getFileType() == null : this.getFileType().equals(other.getFileType()))
            && (this.getIdentifier() == null ? other.getIdentifier() == null : this.getIdentifier().equals(other.getIdentifier()))
            && (this.getFileUrl() == null ? other.getFileUrl() == null : this.getFileUrl().equals(other.getFileUrl()))
            && (this.getIsShared() == null ? other.getIsShared() == null : this.getIsShared().equals(other.getIsShared()))
            && (this.getStorageType() == null ? other.getStorageType() == null : this.getStorageType().equals(other.getStorageType()))
            && (this.getAudit() == null ? other.getAudit() == null : this.getAudit().equals(other.getAudit()))
            && (this.getOrigin() == null ? other.getOrigin() == null : this.getOrigin().equals(other.getOrigin()))
            && (this.getParentPathId() == null ? other.getParentPathId() == null : this.getParentPathId().equals(other.getParentPathId()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getFileUpdateTime() == null ? other.getFileUpdateTime() == null : this.getFileUpdateTime().equals(other.getFileUpdateTime()))
            && (this.getFileCreateTime() == null ? other.getFileCreateTime() == null : this.getFileCreateTime().equals(other.getFileCreateTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getOriginName() == null) ? 0 : getOriginName().hashCode());
        result = prime * result + ((getFilePath() == null) ? 0 : getFilePath().hashCode());
        result = prime * result + ((getIsDir() == null) ? 0 : getIsDir().hashCode());
        result = prime * result + ((getFileExt() == null) ? 0 : getFileExt().hashCode());
        result = prime * result + ((getFileSize() == null) ? 0 : getFileSize().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        result = prime * result + ((getIdentifier() == null) ? 0 : getIdentifier().hashCode());
        result = prime * result + ((getFileUrl() == null) ? 0 : getFileUrl().hashCode());
        result = prime * result + ((getIsShared() == null) ? 0 : getIsShared().hashCode());
        result = prime * result + ((getStorageType() == null) ? 0 : getStorageType().hashCode());
        result = prime * result + ((getAudit() == null) ? 0 : getAudit().hashCode());
        result = prime * result + ((getOrigin() == null) ? 0 : getOrigin().hashCode());
        result = prime * result + ((getParentPathId() == null) ? 0 : getParentPathId().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getFileUpdateTime() == null) ? 0 : getFileUpdateTime().hashCode());
        result = prime * result + ((getFileCreateTime() == null) ? 0 : getFileCreateTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fileName=").append(fileName);
        sb.append(", originName=").append(originName);
        sb.append(", filePath=").append(filePath);
        sb.append(", isDir=").append(isDir);
        sb.append(", fileExt=").append(fileExt);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", fileType=").append(fileType);
        sb.append(", identifier=").append(identifier);
        sb.append(", fileUrl=").append(fileUrl);
        sb.append(", isShared=").append(isShared);
        sb.append(", storageType=").append(storageType);
        sb.append(", audit=").append(audit);
        sb.append(", origin=").append(origin);
        sb.append(", parentPathId=").append(parentPathId);
        sb.append(", deleted=").append(deleted);
        sb.append(", userId=").append(userId);
        sb.append(", fileUpdateTime=").append(fileUpdateTime);
        sb.append(", fileCreateTime=").append(fileCreateTime);
        sb.append(", version=").append(version);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
