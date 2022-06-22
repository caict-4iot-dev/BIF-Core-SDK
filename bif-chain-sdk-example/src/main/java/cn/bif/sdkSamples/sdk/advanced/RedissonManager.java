package cn.bif.sdkSamples.sdk.advanced;

import org.redisson.Redisson;
import org.redisson.config.Config;

public class RedissonManager {
    private static Config config = new Config();
    //声明redisso对象
    private static Redisson redisson = null;
    //实例化redisson
    static{
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        config.useSingleServer().setPassword("bif8888");
        //得到redisson对象
        redisson = (Redisson) Redisson.create(config);
    }
    //获取redisson对象的方法
    public static Redisson getRedisson(){
        return redisson;
    }
}

