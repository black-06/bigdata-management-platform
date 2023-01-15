package com.github.bmp.catalog.analysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@MapperScan("com.github.bmp.dao.mapper")
public class DaoConfiguration {
}
