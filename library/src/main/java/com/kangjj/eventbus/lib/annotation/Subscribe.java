package com.kangjj.eventbus.lib.annotation;

import com.kangjj.eventbus.lib.mode.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.1.2_write_eventbus
 * @Package: com.kangjj.eventbus.lib.annotation
 * @CreateDate: 2019/11/20 15:01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    ThreadMode threadMode() default ThreadMode.POSTING;
}
