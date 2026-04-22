package com.efreight.base.module.one.record.neone;

import cn.hutool.core.net.NetUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@ComponentScan(
        basePackages = "com.efreight.base",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.efreight\\.base\\.common\\.core\\.config\\.NacosPrintConfig"
        )
)
@EnableScheduling
@SpringBootApplication(excludeName = {
        "com.efreight.base.common.core.config.NacosPrintConfig"
})
@EnableEncryptableProperties
public class CargoIdApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(CargoIdApplication.class, args);
        Environment env = ctx.getEnvironment();
        String ip = NetUtil.getLocalhost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        String applicationName = env.getProperty("spring.application.name");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application " + applicationName + " is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "swagger-api-doc: \t\thttp://" + ip + ":" + port + path + "/swagger-ui/index.html\n" +
                "----------------------------------------------------------");
    }
}
