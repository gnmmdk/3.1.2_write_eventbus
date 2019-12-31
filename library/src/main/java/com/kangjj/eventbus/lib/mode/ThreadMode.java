package com.kangjj.eventbus.lib.mode;

/**
 * @Description: 线程状态
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.1.2_write_eventbus
 * @Package: com.kangjj.eventbus.lib.mode
 * @CreateDate: 2019/11/20 15:02
 */
public enum ThreadMode {
    //事件处理在和时间发送在相同的进程，所以时间处理时间不应太长，不然影响时间的发送线程，而这个线程可能是UI线程
    POSTING,
    //事件的处理会在UI线程中执行，时间处理不应太长时间
    MAIN,
    //后台进程，处理如保存到数据库等操作
    BACKGROUD,
    // 异步执行，另起线程操作。事件处理会在单独的线程中执行，主要用于在后台线程中执行耗时操作
    ASYNC
}
