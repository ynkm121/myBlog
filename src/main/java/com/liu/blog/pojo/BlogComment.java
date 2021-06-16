package com.liu.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogComment {
    private int commentId;

    private int blogId;

    private String commentator;

    private String email;

    private String websiteUrl;

    private String commentBody;

    private Date commentCreateTime;

    private String commentatorIp;

    private String replyBody;

    private Date replyCreateTime;

    private byte commentStatus;

    private byte isDeleted;
}
