package com.kangjj.eventbus.lib;

import android.os.Handler;
import android.os.Looper;

import com.kangjj.eventbus.lib.annotation.Subscribe;
import com.kangjj.eventbus.lib.core.MethodManager;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.1.2_write_eventbus
 * @Package: com.kangjj.eventbus.lib
 * @CreateDate: 2019/11/20 15:01
 */
public class EventBus {
    //volatile修饰的变量不允许线程内部缓存和重排序，即直接修改内存
    private static volatile EventBus instance;

    private Map<Object, List<MethodManager>> cacheMap;
    private Handler handler;
    private ExecutorService executorService;

    private EventBus(){
        cacheMap = new HashMap<>();
        handler = new Handler(Looper.getMainLooper());
        //创建一个子线程（缓存线程池）
        executorService = Executors.newCachedThreadPool();
    }

    public static EventBus getDefault(){
        if(instance==null){
            synchronized (EventBus.class){
                if(instance == null){
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    //找到MainActivity所有带符合注解的方法
    public void register(Object getter){
        //获取MainActivity所有的方法
        List<MethodManager> methodList = cacheMap.get(getter);
        if(methodList == null){
            methodList = findAnnotationMethod(getter);
            ///todo 5 存放到缓存Map中 Map<Object, List<MethodManager>>
            cacheMap.put(getter,methodList);
        }
    }

    /**
     *  todo 获取MainActivity中所有注解的方法
     * @param getter
     * @return
     */
    private List<MethodManager> findAnnotationMethod(Object getter) {
        List<MethodManager> methodList = new ArrayList<>();
        //获取类
        Class<?> clazz = getter.getClass();
        while(clazz != null){
            String clazzName = clazz.getName();
            if(clazzName.startsWith("java.") || clazzName.startsWith("javax.")
                    || clazzName.startsWith("android.") || clazzName.startsWith("androidx.")){
                break;
            }
            //todo 1 获取类中所有方法
            Method[] methods  = clazz.getMethods();
            for (Method method : methods) {
                //todo 2 获取方法的注解（method.getAnnotation），方法没有含有该注解就继续下次循环。
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if(subscribe == null){
                    continue;
                }
                //todo 3 严格控制方法格式和规范
                Type returnType = method.getGenericReturnType();
                //todo 3.1 方法必须是返回void（一次匹配）
                if (!"void".equals(returnType.toString())) {
                    throw new RuntimeException(method.getName()+"方法必须返回是void");
                }
                //todo 3.2 方法有且只有一个参数
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length != 1){
                    throw new RuntimeException(method.getName() + "方法有且只有一个参数");
                }
                //todo 4 将参数类型（Class)、线程模式（ThreadMode）、订阅者的方法（Method）保存在bean中，然后将该bean添加到list
                MethodManager methodManager = new MethodManager(parameterTypes[0],subscribe.threadMode(),method);
                methodList.add(methodManager);
            }//end for
            //todo 5 不断循环找出父类含有订阅者（注解方法）的类。直到为空，比如AppCompatActivity没有吧
            clazz = clazz.getSuperclass();
        }//end while
        return methodList;
    }

    //todo 发送消息
    public void post(final Object setter){
        //todo 1 订阅者已经登记，从登记表中找出
        Set<Object> set = cacheMap.keySet();
        for (final Object getter : set) {
            //todo 2 获取类中所有注解的方法
            List<MethodManager> methodList = cacheMap.get(getter);
            if(methodList!=null){
                // 循环每个方法
                for (final MethodManager method : methodList) {
                    //todo 可能多个方法的参数一样，从而都同时收到发送的消息
                    if(method.getType().isAssignableFrom(setter.getClass())){
                        // todo 匹配MainActivity中所有注解的方法符合要求的，都发送消息
                        //  class1.isAssignableFrom(class2) 判定此 Class 对象所表示的类或接口
                        //  与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口

                        switch (method.getThreadMode()) {
                            case POSTING:
                                invoke(method,getter,setter);
                                break;
                            case MAIN:
                                if(Looper.myLooper() == Looper.getMainLooper()){
                                    invoke(method,getter,setter);
                                }else{
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke(method,getter,setter);
                                        }
                                    });
                                }
                                break;
                            case BACKGROUD:
                                if(Looper.myLooper() == Looper.getMainLooper()){ //如果在主线程post，需要到子线程执行
                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke(method,getter,setter);
                                        }
                                    });
                                }else{//子线程到子线程 不用切换线程
                                    invoke(method,getter,setter);
                                }
                                break;
                            case ASYNC:
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 找到匹配方法后，通过反射调用MainActiivty中所有符合要求的方法
     * @param method
     * @param getter
     * @param setter
     */
    private void invoke(MethodManager method, Object getter, Object setter) {

        Method execute = method.getMethod();
        try {
            execute.invoke(getter,setter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
