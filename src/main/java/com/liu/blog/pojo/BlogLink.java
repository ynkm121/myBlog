package com.liu.blog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogLink {
    private int linkId;

    private byte linkType;

    private String linkName;

    private String linkUrl;

    private String linkDescription;

    private int linkRank;

    private byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
