package cn.yk.starter.config;


import cn.yk.starter.RateLimit.RateLimitingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class RateLimitingConfiguration {

    @Bean
    public DefaultRedisScript<Long> redisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //C:\ide\IdeaProjects\llp-javamail\src\main\resources\lua\limit.lua
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/LimitRate.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    public RateLimitingAspect rateLimitingAspect(){
        return new RateLimitingAspect();
    }

}
