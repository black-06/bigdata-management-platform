package com.bmp.dao;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.github.yulichang.injector.MPJSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@MapperScan("com.bmp.dao.mapper")
public class DaoConfiguration {

    @Component
    public static class SqlInjector extends MPJSqlInjector {
        @Override
        public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
            List<AbstractMethod> methods = super.getMethodList(mapperClass, tableInfo);
            methods.add(new InsertBatchSomeColumn());
            return methods;
        }
    }
}
