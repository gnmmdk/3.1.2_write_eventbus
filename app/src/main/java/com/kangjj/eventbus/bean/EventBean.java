package com.kangjj.eventbus.bean;

import androidx.annotation.NonNull;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.1.2_write_eventbus
 * @Package: com.kangjj.eventbus.bean
 * @CreateDate: 2019/11/20 16:13
 */
public class EventBean {
    private String name;

    public EventBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "name = " + name;
    }
}
