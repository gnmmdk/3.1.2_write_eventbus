package com.kangjj.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kangjj.eventbus.bean.EventBean;
import com.kangjj.eventbus.lib.EventBus;
import com.kangjj.eventbus.lib.annotation.Subscribe;
import com.kangjj.eventbus.lib.mode.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUD)
    public void getMessage(EventBean bean){
        Log.e("EventBus >>1>> ", "thread = " + Thread.currentThread().getName());
        Log.e("EventBus >>1>> ", "" + bean.getName());
    }


    public void click(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
