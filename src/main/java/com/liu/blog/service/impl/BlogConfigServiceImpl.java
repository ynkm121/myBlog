package com.liu.blog.service.impl;

import com.liu.blog.dao.BlogConfigMapper;
import com.liu.blog.pojo.BlogConfig;
import com.liu.blog.service.BlogConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogConfigServiceImpl implements BlogConfigService {

    @Autowired
    BlogConfigMapper blogConfigMapper;

    @Override
    public Map<String, String> getConfig() {
        List<BlogConfig> configList = blogConfigMapper.selectAll();
        Map<String, String> configMap = configList.stream().collect(Collectors.toMap(BlogConfig::getConfigName, BlogConfig::getConfigValue));

        return configMap;
    }

    @Override
    public int updateConfig(BlogConfig config) {
        return 0;
    }
}
