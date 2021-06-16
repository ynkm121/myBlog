package com.liu.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogUser {
    private int userId;

    private String userName;

    private String password;

    private String nickName;

    private byte locked;
}
