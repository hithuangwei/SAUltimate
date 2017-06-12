package bean;

import java.io.Serializable;

/**
 * Created by Huang on 2017/5/19.
 */
public class WeiboLog implements Serializable
{
    private static final long serialVersionUID = 2504467948968634865L;
    private long id;
    private long weiboId;
    private String opType;
    private String description;
    private String username;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getWeiboId()
    {
        return weiboId;
    }

    public void setWeiboId(long weiboId)
    {
        this.weiboId = weiboId;
    }

    public String getOpType()
    {
        return opType;
    }

    public void setOpType(String opType)
    {
        this.opType = opType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
