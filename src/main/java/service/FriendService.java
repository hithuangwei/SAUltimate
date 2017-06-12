package service;

import utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 15546 on 2017/6/11.
 */
public class FriendService
{
    public boolean isFriendRelation(long firstId, long secondId)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "select count(*) from tb_friend where (firstId=? and secondId=?) or (" + "secondId=? and firstId=?)";
        List<Object> params = new ArrayList<Object>();
        params.add(firstId);
        params.add(secondId);

        try
        {
            long count = jdbcUtil.getCount(sql, params);
            if (count <= 0)
            {
                return false;
            } else
            {
                return true;
            }

        } catch (SQLException e)
        {

            e.printStackTrace();
            return false;
        } finally
        {
            jdbcUtil.releaseConn();

        }
    }


    public void addFriend(long firstId, long secondId)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "insert into tb_friend(firstId,secondId) values(?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(firstId);
        params.add(secondId);
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


    public List<String> getAllFriend(long firstId, int start, int pageSize)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "select distinct username from  tb_friend where firstId=? limit " + start + "," + pageSize;
        List<Object> params = new ArrayList<Object>();
        params.add(firstId);
        try
        {

            List<Map<String, Object>> res = jdbcUtil.findResult(sql, params);
            if (res == null)
            {
                return null;
            } else
            {
                List<String> names = new ArrayList<String>();
                for (Map<String, Object> item : res)
                {
                    names.add(item.get("username").toString());
                }
                return names;
            }
        } catch (SQLException e)
        {

            e.printStackTrace();
            return null;

        } finally
        {
            jdbcUtil.releaseConn();

        }
    }


}
