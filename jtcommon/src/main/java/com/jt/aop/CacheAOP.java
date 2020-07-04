package com.jt.aop;

import com.jt.annotation.CacheFind;
import com.jt.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

@Slf4j
@Component
@Aspect
public class CacheAOP {

    /**
     * 1.添加环绕通知  使用环绕通知必须添加返回值
     * 2.拦截自定义注解
     *   需求: 动态获取自定义注解中的参数
     * 工作原理说明:
     * 	定义注解的变量名称  cacheFind
     *      通知参数接收名称     cacheFind
     *      除了匹配名称之外,还需要匹配类型.
     *
     * 注意事项:
     * 	1.环绕通知使用时,必须添加ProceedingJoinPoint
     *  2.并且其中的参数joinPoint,必须位于第一位.
     *
     * 缓存实现业务思路:
     * 	1.准备key   1:动态的生成key    2:用户指定的key    key是否有值.
     * 	2.先查询缓存
     * 		2.1 没有数据   执行数据库操作. 执行目标方法
     * 		将目标方法的返回值 转化为JSON串,保存到redis中.
     * 		2.2 有数据      动态获取缓存数据之后利用工具API转化为真实的对象.
     */
    @Autowired
    private Jedis jedis;

    @Around("@annotation(cacheFind)")
    public Object around(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
        //调用方法,获取key
        String key = getKey(joinPoint,cacheFind);
        String values = jedis.get(key);
        Object object = null;

        try {
            if(StringUtils.isEmpty(values)){
                object = joinPoint.proceed();
                //将数据保存到redis中
                String json = ObjectMapperUtil.toJSON(object);
                //判断是否需要超时设定
                if(cacheFind.secondes()>0){
                    int secondes = cacheFind.secondes();
                    jedis.setex(key,secondes,json);
                }else {
                    //该数据永不超时
                    jedis.set(key,json);
                }
                log.info("AOP查询数据库!!!!!");
            }else {
                Class<?> targetClass = getReturnType(joinPoint);
                object = ObjectMapperUtil.toObj(values,targetClass);
                log.info("AOP查询缓存!!!!");
            }
        }catch (Throwable e){
            e.printStackTrace();
            throw new RuntimeException();
        }


        return object;
    }

    //需求动态的获取返回值类型
    private Class<?> getReturnType(ProceedingJoinPoint joinPoint) {
        //Signature标识方法的API  利用反射动态获取method对象 获取返回值
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();

        return methodSignature.getReturnType();
    }

    //动态获取key
    private String getKey(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
        //1.检查用户是否传递key
        String key = cacheFind.key();
        if(StringUtils.isEmpty(key)){
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methedName = joinPoint.getSignature().getName();
            //包名.类名.方法名::第一个参数
            Object arg0 = joinPoint.getArgs()[0];
            key = className + methedName + "::" + arg0;
        }
        return key;
    }
}
