package cn.yk.starter.RateLimit;

import cn.yk.starter.enums.LimitType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Aspect
@Component
public class RateLimitingAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisScript<Long> redisScript;




    @Before(value = "@annotation(rateLimiting)")
    public void doBefore(JoinPoint joinPoint, rateLimiting rateLimiting){
        String key = rateLimiting.key();
        int maxTimes = rateLimiting.maxTimes();
        int second = rateLimiting.second();
        ChronoUnit timeType = rateLimiting.timeType();
        String combineKey = getCombineKey(rateLimiting, joinPoint);
        switch (timeType){
            case HOURS: second=second*3600;
                break;
            case MINUTES: second=second*60; default:break;
        }
        List<String> keys = Collections.singletonList(combineKey);
        try {
            Long number = (Long) redisTemplate.execute(redisScript, keys, maxTimes, second);
            if (number == null || number.intValue() > maxTimes) {
                throw new RuntimeException("访问过于频繁，请稍后再试");
            }
        }catch(Exception e){
            throw new RuntimeException("服务器限流异常，请稍后再试");
        }
    }
    public String getCombineKey(rateLimiting rateLimiting, JoinPoint point) {
        //从@RateLimiter注解中获取限流key rate_limit:
        StringBuffer stringBuffer = new StringBuffer(rateLimiting.key());
        //判断@RateLimiter注解的 限流类型
        if (rateLimiting.limitType() == LimitType.Ip) {
            //如果为IP则将请求ip端口进行拼接rate_limit:localhost:8080-
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            stringBuffer.append(request.getHeader("Host")).append("-");
        }

        MethodSignature signature = (MethodSignature) point.getSignature();
        //得到Method对象
        Method method = signature.getMethod();
        //得到Class
        Class<?> targetClass = method.getDeclaringClass();
        //将运行时类名和方法名进行拼接
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }













}
