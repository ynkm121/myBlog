package com.liu.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogTagRelation {
    private int relationId;
    private int blogId;
    private int tagId;
    private Date createTime;
}
