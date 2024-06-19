package cn.yk.starter;

import cn.yk.starter.RateLimit.rateLimiting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
public class Test {
    @GetMapping("/hello")
    @rateLimiting(maxTimes = 10,key = "test",second = 100,timeType = ChronoUnit.MINUTES)
    public String hello() {
        return "hello>>>"+new Date();
    }
}

