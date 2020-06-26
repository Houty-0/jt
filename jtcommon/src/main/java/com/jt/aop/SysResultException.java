package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

//1.标识全局异常处理机制.
@RestControllerAdvice   //@ControllerAdvice(AOP中通知) + @ResponseBody
public class SysResultException {

    /**
     * 如果后台服务器发生运行时异常.则执行异常方法
     * 问题:JSONP能否接收SysResult错误对象?????
     *思路: 跨域请求 一定会携带特定的参数 ?callback=xxxxxx
     */
    /**
     * 如果后台服务器发生运行时异常.则执行异常方法
     */
    @ExceptionHandler(RuntimeException.class)
    public SysResult sysResult(Exception exception) {
        exception.printStackTrace(); //输出/log日志保存
        return SysResult.fail();
    }
}
