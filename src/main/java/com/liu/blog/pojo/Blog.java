package com.liu.blog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    private Long blogId;
    private String blogTitle;
    private String blogSubUrl;
    private String blogCoverImage;
    private String blogContent;
    private Integer blogCategoryId;
    private String blogCategoryName;
    private String blogTags;
    private Byte blogStatus;
    private Integer blogViews;
    private Byte enableComment;
    private Byte isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Date updateTime;

}
