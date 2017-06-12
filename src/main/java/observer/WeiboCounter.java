package observer;


import bean.WeiboItem;
import inter.Observer;
import inter.Subject;
import service.WeiboService;

/**
 * Created by Huang on 2017/5/19.
 */
public class WeiboCounter implements Observer
{


    public void updateMessage(Subject item, String opType, String username)
    {
        WeiboService service = new WeiboService();
        service.increaseClickNum((WeiboItem) item);
    }
}
