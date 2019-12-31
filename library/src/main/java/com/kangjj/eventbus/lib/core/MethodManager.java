package com.kangjj.eventbus.lib.core;

import com.kangjj.eventbus.lib.mode.ThreadMode;

import java.lang.reflect.Method;

/**
 * @Description: 保存符合要求的订阅方法封装类
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.1.2_write_eventbus
 * @Package: com.kangjj.eventbus.lib.core
 * @CreateDate: 2019/11/20 15:02
 */
public class MethodManager {
    //订阅者的回调方法（注解方法）的参数类型
    private Class<?> type;
    //订阅者的回调方法(注解方法）的线程模式
    private ThreadMode threadMode;

    //订阅者的回调方法（注解方法）
    private Method method;

    public MethodManager(Class<?> type, ThreadMode threadMode, Method method) {
        this.type = type;
        this.threadMode = threadMode;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
