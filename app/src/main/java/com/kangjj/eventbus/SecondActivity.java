package com.kangjj.eventbus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kangjj.eventbus.bean.EventBean;
import com.kangjj.eventbus.lib.EventBus;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.1.2_write_eventbus
 * @Package: com.kangjj.eventbus
 * @CreateDate: 2019/11/20 16:16
 */
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void eventBus(View view) {
        Log.e("EventBus >>2>> ", "thread = " + Thread.currentThread().getName());
        EventBus.getDefault().post(new EventBean("kangjj"));
        finish();
    }
}
