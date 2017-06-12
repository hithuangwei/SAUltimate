package listener;

import bean.WeiboItem;

import controller.MainServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.WeiboService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;
import java.util.List;

/**
 * Created by 15546 on 2017/6/2.
 */
public class SchedulerListener implements ServletContextListener
{
    private Logger logger = LoggerFactory.getLogger(SchedulerListener.class);
    private MyThread myThread;
    private WeiboService weiboService;

    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        if (myThread != null && myThread.isInterrupted())
        {
            myThread.interrupt();
        }
    }

    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        System.out.println("定时服务开启时间：" + new Date());
        logger.info("contextInitialized SchedulerListener" + new Date());

        String str = null;
        if (str == null && myThread == null)
        {
            myThread = new MyThread();
            myThread.start(); // servlet 上下文初始化时启动 socket
        }
    }

    class MyThread extends Thread
    {
        public void run()
        {
            while (!this.isInterrupted())
            {// 线程未中断执行循环
                try
                {
//                    Thread.sleep(1000 * 60 * 10);
                    Thread.sleep(1000 * 30);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                logger.info("schedule at " + new Date());
                System.out.println("schedule at" + new Date());
                if (weiboService == null)
                {
                    weiboService = new WeiboService();
                }
                List<WeiboItem> items = weiboService.getTopClickItems();
                MainServlet.topWeibos=items;
            }
        }
    }


}