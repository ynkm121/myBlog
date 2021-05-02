package com.liu.blog.service;

import com.liu.blog.pojo.BlogConfig;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface BlogConfigService {

    Map<String, String> getConfig();

    int updateConfig(BlogConfig config);
}
