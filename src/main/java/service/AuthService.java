package service;

import bean.User;
import utils.JdbcUtil;
import utils.MD5Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 15546 on 2017/6/1.
 */
public class AuthService
{
    public User validateUser(String username, String password)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "select count(*) from tb_user where username=? and password=?";
        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(MD5Utils.MD5Encode(password, "utf-8", false));

        try
        {
            long count = jdbcUtil.getCount(sql, params);
            if (count <= 0)
            {
                return null;
            } else
            {
                sql = "select * from tb_user where username=?";
                params.clear();
                params.add(username);
                List<Map<String, Object>> datas = jdbcUtil.findResult(sql, params);
                Map<String, Object> map = datas.get(0);
                User user = new User();
                user.setPassword(map.get("password").toString());
                user.setUsername(map.get("username").toString());
                return user;
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

    public void registerUser(String username, String password)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        long max = 0;
        String sql = "select max(id) from tb_user";
        try
        {
            List<Map<String, Object>> res = jdbcUtil.findResult(sql, null);

            for (Map<String, Object> map : res)
            {
                max = Math.max(max, Long.valueOf(map.get("max(id)").toString()));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        sql = "insert into tb_user(id,username,password) values(?,?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(max + 1);
        params.add(username);
        params.add(MD5Utils.MD5Encode(password, "utf-8", false));
        try
        {
            jdbcUtil.updateByPreparedStatement(sql, params);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public String getUserNameById(long id)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        long max = 0;
        String sql = "select username from tb_user where id=?";
        try
        {
            List<Object> params = new ArrayList<Object>();
            params.add(id);
            List<Map<String, Object>> res = jdbcUtil.findResult(sql, params);
            if (res == null)
            {
                return null;
            } else
            {
                return res.get(0).get("username").toString();
            }


        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public long getUserIDByName(String name)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        long max = 0;
        String sql = "select id from tb_user where username=?";
        try
        {
            List<Object> params = new ArrayList<Object>();
            params.add(name);
            List<Map<String, Object>> res = jdbcUtil.findResult(sql, params);
            if (res == null)
            {
                return 0;
            } else
            {
                return Long.valueOf(res.get(0).get("id").toString());
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }

    }
}
