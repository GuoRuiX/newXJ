package com.rjhc.credit.information.service.start;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.rjhc.credit.information.service.server.dao.mapper")
@SpringBootApplication(scanBasePackages="com.rjhc")
@EnableScheduling
@EnableCaching
@ServletComponentScan("com.rjhc.credit.information.service.start.filters")
public class CreditInformationServiceStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditInformationServiceStartApplication.class, args);
    }

}
