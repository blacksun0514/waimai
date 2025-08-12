package com.sky.config;

import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(){
        log.info("开始创建阿里云文件上传客户端");
        return new AliOssUtil("oss-cn-hangzhou.aliyuncs.com",
                "LTAI5tGk1Y98DSEkTfoJpQff",
                "wdj5AyHFCUQ0I45R6f2JZO58PijABn",
                "sky-take-out");
    }
}
