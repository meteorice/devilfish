package com.meteorice.devilfish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type Global config.
 */
@SpringBootApplication
//@MapperScan("com.meteorice.devilfish.dao")
public class GlobalConfig {

    public static void main(String[] args) {
        SpringApplication.run(GlobalConfig.class, args);
    }
}
