package com.linyi.pig;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: linyi
 * @Date: 2025/2/25
 * @ClassName: PigHealthSmartMedicineApplication
 * @Version: 1.0
 * @Description:
 */
@SpringBootApplication
@MapperScan("com.linyi.pig.mapper")
public class PigHealthSmartMedicineApplication {
    public static void main(String[] args) {
        SpringApplication.run(PigHealthSmartMedicineApplication.class, args);
    }
}
