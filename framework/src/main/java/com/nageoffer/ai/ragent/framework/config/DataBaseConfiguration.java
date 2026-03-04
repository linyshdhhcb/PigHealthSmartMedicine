

package com.nageoffer.ai.ragent.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.nageoffer.ai.ragent.framework.database.MyMetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * 数据库持久层配置类
 * 配置 MyBatis-Plus 相关分页插件等
 */
@Configuration
public class DataBaseConfiguration {

    /**
     * MyBatis-Plus MySQL 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * MyBatis-Plus 源数据自动填充类
     */
    @Bean
    public MetaObjectHandler myMetaObjectHandler() {
        return new MyMetaObjectHandler();
    }
}
