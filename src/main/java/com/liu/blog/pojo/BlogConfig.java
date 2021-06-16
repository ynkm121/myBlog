package com.liu.blog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogConfig {
    private String configName;

    private String configValue;

    private Date createTime;

    private Date updateTime;
}
