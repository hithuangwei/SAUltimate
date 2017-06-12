package service;

import utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 15546 on 2017/6/11.
 */
public class CommentService
{
    public void addComment(long weiboId, String commentContent, String username)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "insert into tb_comment(createUsername,createDate,comment_content,weibo_item_id) values(?,?,?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(new Date());
        params.add(commentContent);
        params.add(weiboId);
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

    public List<Map<String, Object>> getAllComment(long weiboId,long start,long pageSize)
    {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        String sql = "select * from tb_comment where weibo_item_id=? limit "+start+","+pageSize;
        List<Object> params = new ArrayList<Object>();
        params.add(weiboId);
        try
        {
            return jdbcUtil.findResult(sql, params);
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
