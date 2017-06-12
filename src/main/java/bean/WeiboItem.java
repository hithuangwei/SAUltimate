package bean;

import inter.Observer;
import inter.Subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Huang on 2017/5/18.
 */
public class WeiboItem implements Subject, Serializable, Comparable<WeiboItem>
{
    private long id;
    private String content;
    private Date createDate;
    private String userName;
    private int likeNum;
    private int clickNum;

    private List<Observer> observers = new ArrayList<Observer>();

    public int getClickNum()
    {
        return clickNum;
    }

    public void setClickNum(int clickNum)
    {
        this.clickNum = clickNum;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getLikeNum()
    {
        return likeNum;
    }

    public void setLikeNum(int likeNum)
    {
        this.likeNum = likeNum;
    }


    public void attach(Observer observer)
    {
        observers.add(observer);
    }


    public void detach(Observer observer)
    {
        observers.remove(observer);
    }


    public void notifyMessage(String opType, String username)
    {
        for (Observer o : observers)
        {
            o.updateMessage(this, opType, username);
        }
    }

    public int compareTo(WeiboItem other)
    {
        return  other.getClickNum()-this.getClickNum();
    }
}
