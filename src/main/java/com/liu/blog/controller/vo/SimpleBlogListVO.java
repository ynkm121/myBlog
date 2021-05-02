package com.liu.blog.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleBlogListVO implements Serializable {

    private Long blogId;

    private String blogTitle;
}
