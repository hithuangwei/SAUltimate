package service;

import bean.WeiboItem;
import com.danga.MemCached.MemCachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JdbcUtil;
import utils.MemCacheUtils;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Huang on 2017/5/19.
 */
public class WeiboService
{

    private static Logger logger = LoggerFactory.getLogger(WeiboService.class);

    /**
     * 点击数增长1
     *
     * @param item
     */
    public void increaseClickNum(WeiboItem item)
    {
        MemCachedClient mcc = MemCacheUtils.getInstance();
        String weiboId = String.valueOf(item.getId());
        //如果缓存有数据，更新
        if (mcc.get(weiboId) != null)
        {
            WeiboItem data = (WeiboItem) mcc.get(weiboId);
            data.setClickNum(data.getClickNum() + 1);
            logger.info("id:" + weiboId + "can be found in cache increaseClickNum");

        }
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "update tb_weibo_item set clickNum=clickNum+1 where id=?";
        List<Object> params = new ArrayList<Object>();
        params.add(item.getId());
        try
        {
            jdbcUtil.updateByPreparedStatement(sql, params);
        } catch (SQLException e)
        {

            e.printStackTrace();
        } finally
        {
            jdbcUtil.releaseConn();
        }

    }

    /**
     * 日志添加 远程日志服务器调用此方法
     *
     * @param opType
     * @param username
     */
    public void addLog(String id, String opType, String username)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "insert into tb_weibo_log(op_type,weibo_id,username,description,datetime) values(?,?,?,?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(opType);
        params.add(id);
        params.add(username);
        params.add(username + " " + opType + " " + id);
        params.add(new Date());
        try
        {
            jdbcUtil.updateByPreparedStatement(sql, params);
        } catch (SQLException e)
        {

            e.printStackTrace();
        } finally
        {
            jdbcUtil.releaseConn();
        }
    }

    /**
     * 发布微博
     *
     * @param item
     * @return
     */
    public long publishWeibo(WeiboItem item)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String findMaxSQL = "select max(id) from tb_weibo_item";
        long max = 0;
        try
        {
            List<Map<String, Object>> res = jdbcUtil.findResult(findMaxSQL, null);

            for (Map<String, Object> map : res)
            {
                max = Math.max(max, Long.valueOf(map.get("max(id)").toString()));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        String sql = "insert into tb_weibo_item(id,content,createDate,username,likeNum,clickNum) values(?,?,?,?,?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(max + 1);
        params.add(item.getContent());
        params.add(item.getCreateDate());
        params.add(item.getUserName());
        params.add(0);
        params.add(0);
        long id = 0;
        try
        {
            id = jdbcUtil.updateByPreparedStatement(sql, params);
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            jdbcUtil.releaseConn();
        }
        return id;
    }

    /**
     * 发布微博
     *
     * @param item
     * @return
     */
    public long addWeiboWithId(WeiboItem item)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "insert into tb_weibo_item(id,content,createDate,username,likeNum,clickNum) values(?,?,?,?,?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(item.getId());
        params.add(item.getContent());
        params.add(item.getCreateDate());
        params.add(item.getUserName());
        params.add(item.getLikeNum());
        params.add(item.getClickNum());
        long id = 0;
        try
        {
            id = jdbcUtil.updateByPreparedStatement(sql, params);
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            jdbcUtil.releaseConn();
        }
        return id;
    }

    /**
     * 查找微博
     *
     * @param id
     * @return
     */
    public WeiboItem findWeiboItemById(long id)
    {
        MemCachedClient mcc = MemCacheUtils.getInstance();
        if (mcc.get(String.valueOf(id)) != null)
        {
            long startCache = System.currentTimeMillis();
            WeiboItem ret = (WeiboItem) mcc.get(String.valueOf(id));
            long endCache = System.currentTimeMillis();
            logger.info("id：" + id + " can be found in cache,cost:" + (endCache - startCache));
            return ret;
        } else
        {
            long startDB = System.currentTimeMillis();
            JdbcUtil jdbcUtil = new JdbcUtil();
            if (jdbcUtil.getConnection() == null)
            {
                jdbcUtil.getConnection();
            }
            String sql = "select * from tb_weibo_item where id=?";
            List<Object> params = new ArrayList<Object>();
            params.add(id);
            List<Map<String, Object>> datas = null;
            try
            {
                datas = jdbcUtil.findResult(sql, params);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

            if (datas != null && datas.size() > 0)
            {
                WeiboItem item = new WeiboItem();
                Map<String, Object> map = datas.get(0);
                item.setId(Long.valueOf(map.get("id").toString()));
                item.setClickNum(Integer.valueOf(map.get("clickNum").toString()));
                item.setLikeNum(Integer.valueOf(map.get("likeNum").toString()));
                item.setUserName(map.get("username").toString());
                item.setCreateDate((Date) map.get("createDate"));
                item.setContent(map.get("content").toString());

                return item;
            }
            long endDB = System.currentTimeMillis();
            logger.info("id：" + id + " can be found in db,cost:" + (endDB - startDB));
            return null;
        }
    }

    public List<WeiboItem> findAllWeiboItem(int start, int pageSize)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "select id from tb_weibo_item limit " + start + "," + pageSize;
        System.out.println("sql " + sql);
        List<Map<String, Object>> datas = null;
        try
        {
            datas = jdbcUtil.findResult(sql, null);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        List<WeiboItem> ret = new ArrayList<WeiboItem>();
        for (Map<String, Object> data : datas)
        {

            String weiboId = data.get("id").toString();
            ret.add(findWeiboItemById(Long.valueOf(weiboId)));
        }
        return ret;
    }

    public long findTotalWeiboCount()
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "select count(*) from tb_weibo_item ";
        List<Map<String, Object>> datas = null;
        try
        {
            return jdbcUtil.getCount(sql, null);
        } catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }

    }

    public String getAbstractInfo(String src)
    {
        int min = Math.min(25, src.length());
        return src.substring(0, min);
    }


    public void deleteWeiboItemById(long id)
    {
        String weiboId = String.valueOf(id);
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "delete from tb_weibo_item where id=?";
        List<Object> params = new ArrayList<Object>();
        params.add(id);

        MemCachedClient mcc = MemCacheUtils.getInstance();

        try
        {
            jdbcUtil.updateByPreparedStatement(sql, params);
            if (mcc.get(weiboId) != null)
            {
                logger.info("id:" + id + " can be found in cache,deleted");
                mcc.delete(weiboId);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateWeiboItemById(long id, String newContent)
    {
        String weiboId = String.valueOf(id);

        MemCachedClient mcc = MemCacheUtils.getInstance();

        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "update tb_weibo_item set content=? where id=?";
        List<Object> params = new ArrayList<Object>();
        params.add(newContent);
        params.add(id);
        try
        {
            jdbcUtil.updateByPreparedStatement(sql, params);
            if (mcc.get(weiboId) != null)
            {
                WeiboItem data = (WeiboItem) mcc.get(weiboId);
                data.setContent(newContent);

                mcc.set(weiboId, data);
                logger.info("id:" + id + " can be found in cache,update");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public List<WeiboItem> getTopClickItems()
    {
        MemCachedClient mcc = MemCacheUtils.getInstance();

        JdbcUtil jdbcUtil = new JdbcUtil();
        List<WeiboItem> ret = new ArrayList<WeiboItem>();
        String sql = "select id,clickNum from tb_weibo_item order by clickNum desc limit 100";

        List<Map<String, Object>> datas = null;
        try
        {
            datas = jdbcUtil.findResult(sql, null);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        List<WeiboItem> totalItems = new ArrayList<WeiboItem>();
        for (Map<String, Object> data : datas)
        {
            String id = String.valueOf(data.get("id").toString());
            int clickNum = Integer.valueOf(data.get("clickNum").toString());
            long weiboId = Long.valueOf(id);
            WeiboItem item = new WeiboItem();
            item.setId(weiboId);
            item.setClickNum(clickNum);
            totalItems.add(item);
        }
        logger.info("fetch from distribute database count: " + totalItems.size());
        Collections.sort(totalItems);
        int min = Math.min(totalItems.size(), 100);
        ret.clear();
        for (int i = 0; i < min; i++)
        {
            WeiboItem item = findWeiboItemById(totalItems.get(i).getId());
            logger.info("top" + totalItems.get(i).getId());
            mcc.set(String.valueOf(totalItems.get(i).getId()), item);
            ret.add(item);
        }
        logger.info("after shuffering and sorting count: " + ret.size());
        return ret;

    }


}
