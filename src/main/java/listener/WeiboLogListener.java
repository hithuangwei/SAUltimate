package listener;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.WeiboService;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

/**
 * Created by 15546 on 2017/6/3.
 */
public class WeiboLogListener implements ServletContextListener
{
    private Logger logger = LoggerFactory.getLogger(WeiboLogListener.class);
    private MyThread myThread;
    private static WeiboService weiboService = new WeiboService();
    private static final String SUBJECT = "weibo_logging";


    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        if (myThread != null && myThread.isInterrupted())
        {
            myThread.interrupt();
        }
    }

    public void contextInitialized(ServletContextEvent servletContextEvent)
    {

        logger.info("contextInitialized WeiboLogReiver");

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
//            while (!this.isInterrupted())
//            {// 线程未中断执行循环

            // ConnectionFactory ：连接工厂，JMS 用它创建连接
            ActiveMQConnectionFactory connectionFactory;

            // Connection ：JMS 客户端到JMS Provider 的连接
            Connection connection = null;
            // Session： 一个发送或接收消息的线程
            Session session;
            // Destination ：消息的目的地;消息发送给谁.
            Destination destination;
            // 消费者，消息接收者
            MessageConsumer consumer;
            connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
            //  System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","domain");


            try
            {
                // 构造从工厂得到连接对象
                connection = connectionFactory.createConnection();
                // 启动
                connection.start();
                // 获取操作连接
                session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
                // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
                destination = session.createQueue(SUBJECT);
                consumer = session.createConsumer(destination);
            /*
             * 利用activemq接收java对象
             * */
                while (true)
                {
                    // 设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                    ActiveMQObjectMessage msg = (ActiveMQObjectMessage) session.createObjectMessage();
                    msg = (ActiveMQObjectMessage) consumer.receive(100000);
                    if (msg != null)
                    {
                        Map s = (Map<String, String>) msg.getObject();
                        logger.info("receive message " + s);
//                        WeiboLog weiboLog = new WeiboLog();
//                        weiboLog.setUsername(s.get("username").toString());
//                        weiboLog.setOpType(s.get("opType").toString());
//                        weiboLog.setWeiboId(Long.valueOf(s.get("weiboId").toString()));
//                        weiboLog.setDescription(s.get("username").toString() + " " + s.get("opType").toString() + " " + s.get("weiboId"));
//                        WeiboItem item = weiboService.findWeiboItemById(Long.valueOf());
                        weiboService.addLog(s.get("weiboId").toString(), s.get("opType").toString(), s.get("username").toString());
//
                    }


                }
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    if (null != connection) connection.close();
                } catch (Throwable ignore)
                {
                }
            }

        }
    }
}
