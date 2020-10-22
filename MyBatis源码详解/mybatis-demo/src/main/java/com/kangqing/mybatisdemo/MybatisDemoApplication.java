package com.kangqing.mybatisdemo;

import com.kangqing.mybatisdemo.entity.User;
import com.kangqing.mybatisdemo.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class MybatisDemoApplication {

    public static void main(String[] args) {
        //SpringApplication.run(MybatisDemoApplication.class, args);

        // 第一阶段：初始化mybatis
        String resource = "mybatis-config.xml";
        // 得到配置文件的输入流
        InputStream is = null;

        try {
            is = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 得到SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        // 第二阶段，数据读写阶段
        try(SqlSession session = sqlSessionFactory.openSession()) {
            // 找到接口对应的实现
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.selectByPrimaryKey(2);
            System.out.println(user);
        }
    }

}
