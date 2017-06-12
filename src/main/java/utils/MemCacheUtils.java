package utils;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemCacheUtils
{

    private static MemCachedClient mem = null;

    public static MemCachedClient getInstance()
    {
        if (mem == null)
        {
            return new MemCachedClient();
        } else
        {
            return mem;
        }

    }

    static
    {
        // 设置缓存服务器列表，当使用分布式缓存的时，可以指定多个缓存服务器
        String[] servers = {"127.0.0.1:11211"
                //"server.mydomain.com:11211"
        };

        // 设置服务器权重
        Integer[] weights = {3, 2};

        // 创建一个Socked连接池实例  获取socke连接池的实例对象
        SockIOPool pool = SockIOPool.getInstance();

        // 向连接池设置服务器和权重
        pool.setServers(servers);
        pool.setWeights(weights);

        // 设置初始连接数、最小和最大连接数以及最大处理时间
        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        pool.setMaxIdle(1000 * 60 * 60 * 6);

        // 设置主线程的睡眠时间
        pool.setMaintSleep(30);

        // 设置TCP的参数，连接超时等
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);

        // 初始化连接池
        pool.initialize();
    }


}
