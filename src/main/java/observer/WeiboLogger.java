package observer;


import bean.WeiboItem;
import inter.Observer;
import inter.Subject;
import middleware.LoggingSender;

import javax.jms.JMSException;
import java.util.HashMap;

/**
 * Created by Huang on 2017/5/19.
 */
public class WeiboLogger implements Observer

{


    public void updateMessage(Subject item, String opType, String username)
    {

//        WeiboService service=new WeiboService();
//        service.addLog((WeiboItem) item,opType,username);
        HashMap<String, String> map = new HashMap<String, String>();




        map.put("weiboId", String.valueOf(((WeiboItem) item).getId()));
        map.put("opType", opType);
        map.put("username", username);

        try
        {
            LoggingSender.send(map);
        } catch (JMSException e)
        {
            e.printStackTrace();
        }

    }
}
