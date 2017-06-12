package controller;

import bean.User;
import bean.WeiboItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import datatables.DataTableCommand;
import datatables.DataTableResult;
import inter.Observer;
import observer.WeiboLogger;
import service.AuthService;
import service.CommentService;
import service.FriendService;
import service.WeiboService;
import utils.JSONUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by 15546 on 2017/6/10.
 */
@WebServlet("*.do")
public class MainServlet extends HttpServlet
{
    public static List<WeiboItem> topWeibos = new ArrayList<WeiboItem>();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        //获取参数方法名
        String action = req.getRequestURI();
        try
        {
            if (action.equalsIgnoreCase("/blog/"))
            {
                GETLogin(req, resp, JSONUtils.getViewName("Login"));

            } else
            {
                String servletPath = req.getServletPath();
                String methodName = servletPath.substring(1, servletPath.length() - 3);
                Method method = this.getClass().getDeclaredMethod((req.getMethod() + methodName), HttpServletRequest.class, HttpServletResponse.class);
                method.invoke(this, req, resp);
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void GETLogin(HttpServletRequest req, HttpServletResponse resp,String viewName) throws ServletException, IOException
    {

        String path="WEB_INF/view/"+viewName;
        req.getRequestDispatcher(path).forward(req, resp);

    }

    public void POSTLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        AuthService authService = new AuthService();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User u = authService.validateUser(username, password);
        if (u != null)
        {

            req.getSession().setAttribute("username", username);

            String path = req.getContextPath();
            String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";

            resp.sendRedirect(basePath + "index");

        } else
        {
            req.setAttribute("reason", "认证失败");
            req.getRequestDispatcher("WEB-INF/view/login.jsp").forward(req, resp);
        }


    }

    /**
     * 主页
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void GETView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        WeiboService weiboService = new WeiboService();
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        req.getRequestDispatcher("WEB-INF/view/index.jsp").forward(req, resp);

    }

    public void POSTList(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException
    {
        WeiboService weiboService = new WeiboService();

        DataTableCommand cmd = new DataTableCommand(request.getParameterMap());
        DataTableResult res = new DataTableResult();
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        try
        {
            List<WeiboItem> weiboList = weiboService.findAllWeiboItem(cmd.getStart(), cmd.getLength());
            if (weiboList != null)
            {
                for (WeiboItem item : weiboList)
                {
                    if (item != null)
                    {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("createDate", item.getCreateDate());
                        map.put("username", item.getUserName());
                        map.put("clickNum", item.getClickNum());
                        map.put("content", item.getContent());

                        String htmlOP = "<a href=\"/blog/Mod.do?id=" + item.getId() + "\"  style=\"width:40px;font-size:12px;\" class=\"btn \">" +
                                "更新</button>" +
                                "  " + "<a href=\"/blog/Detail.do?id=" + 
                                item.getId() + "\" class=\"btn \" style=\"width:40px;font-size:12px;color:orange\">详情</a>"
                                + "<a href=\"/blog/Friend.do?id=" + item.getId() + "\" class=\"btn \" style=\"width:60px;font-size:12px;color:blue\">加为好友</a>" + "<a href=\"/blog/Del.do?id=" + item.getId() + "\" " + "class=\"btn \" style=\"width:40px;font-size:12px;color:red\">" +
                                "<center>删除</center></a>";
                        map.put("op", htmlOP);
                        data.add(map);
                    }
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }


        res.setRecordsFiltered(Integer.valueOf(String.valueOf(weiboService.findTotalWeiboCount())));
        res.setData(data);
        String jsonStr = JSON.toJSON(res).toString();

        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json; charset=utf-8");
        out.print(jsonStr);
        out.flush();
        out.close();
    }


    public void POSTPub(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        WeiboService weiboService = new WeiboService();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("utf-8");
        WeiboItem item = new WeiboItem();
        String content = req.getParameter("content").toString();
        item.setContent(content);
        item.setCreateDate(new Date());
        item.setClickNum(0);
        item.setLikeNum(0);
        HttpSession session = req.getSession();
        if (session != null)
        {

            if (session.getAttribute("username") == null)
            {
                item.setUserName("Anonymous");
            } else
            {
                item.setUserName(session.getAttribute("username").toString());
            }
        } else
        {
            item.setUserName("Anonymous");
        }
        long id = weiboService.publishWeibo(item);
        item.setId(id);
        //addItem(item, item.getUserName());
        String path = req.getContextPath();
        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
        resp.sendRedirect(basePath + "View.do");
    }

    public void addItem(WeiboItem item, String username)
    {

        Observer logger = new WeiboLogger();
        item.attach(logger);
        item.notifyMessage("create", username);
    }

    public void deleteItem(WeiboItem item, String username)
    {

        Observer logger = new WeiboLogger();
        item.attach(logger);
        item.notifyMessage("delete", username);
    }

    public void GETDel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        WeiboService weiboService = new WeiboService();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("utf-8");
        String path = req.getContextPath();
        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
        String id = req.getParameter("id");
        if (id != null && id.trim().equals("") == false)
        {
            try
            {
                long weiboId = Long.valueOf(id);
                WeiboItem item = weiboService.findWeiboItemById(weiboId);
                weiboService.deleteWeiboItemById(weiboId);
                if (item != null)
                {
                    String username = null;
                    if (req.getSession() != null && req.getSession().getAttribute("username") != null)
                    {
                        username = req.getSession().getAttribute("username").toString();
                    } else
                    {
                        username = "Anonymous";
                    }
                    //deleteItem(item, username);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            resp.sendRedirect(basePath + "View.do");
        }
    }


    public void GETTop(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=utf-8");

        if (topWeibos != null)
        {
            List<String> tags = new ArrayList<String>();
            for (int i = 0; i < topWeibos.size(); i++)
            {
                WeiboItem item = topWeibos.get(i);
                long id = item.getId();
                String content = item.getContent();
                String path = req.getContextPath();
                String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
                String weiboUrl = basePath + "detail?id=" + id;
//                <li><a target=_blank href="http://www.xwcms.net" style="color: white">网易女性美容幻灯片动画</a></li>
                String href = "<li><a target=_blank href='" + weiboUrl + "' style='color: white'>" + content + "</a></li>";
                tags.add(href);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 200);
            jsonObject.put("data", tags);
            String jsonStr = JSON.toJSONString(jsonObject);
            PrintWriter out = null;
            try
            {
                out = resp.getWriter();
                out.write(jsonStr);
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                if (out != null)
                {
                    out.close();
                }
            }
        } else

        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 400);
            String jsonStr = JSON.toJSONString(jsonObject);
            PrintWriter out = null;
            try
            {
                out = resp.getWriter();
                out.write(jsonStr);
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                if (out != null)
                {
                    out.close();
                }
            }
        }
    }

    public void POSTLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.getSession().removeAttribute("username");
        String path = req.getContextPath();
        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
        resp.sendRedirect(basePath + "Login.do");
    }


    public void POSTModify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("utf-8");
//        String path = req.getContextPath();
//        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
        String content = req.getParameter("content");
        String weiboId = req.getParameter("id").toString();
        WeiboService weiboService = new WeiboService();
        weiboService.updateWeiboItemById(Long.valueOf(weiboId), content);
        String path = req.getContextPath();
        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
        resp.sendRedirect(basePath + "View.do");
    }

    public void GETReg(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.getRequestDispatcher("WEB-INF/view/register.jsp").forward(req, resp);
    }

    public void POSTReg(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");
        AuthService authService = new AuthService();
        if (username != null && !username.trim().equals("") && password != null && !password.trim().equals("") && password.equals(confirm))
        {


            authService.registerUser(username, password);
            String path = req.getContextPath();
            String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
            resp.sendRedirect(basePath + "login");
        } else
        {
            req.setAttribute("reason", "注册失败");
            req.getRequestDispatcher("WEB-INF/view/register.jsp").forward(req, resp);
        }
    }


    public void POSTFriendList(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException
    {
        FriendService friendService = new FriendService();
        String username = request.getSession().getAttribute("username").toString();

        DataTableCommand cmd = new DataTableCommand(request.getParameterMap());
        DataTableResult res = new DataTableResult();
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        try
        {
            AuthService authService = new AuthService();
            List<String> names = friendService.getAllFriend(authService.getUserIDByName(username), cmd.getStart(), cmd.getLength());
            if (names != null)
            {
                for (int i = 0; i < names.size(); i++)
                {
                    String item = names.get(i);
                    if (item != null)
                    {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", String.valueOf(i + 1));
                        map.put("username", item);

                        data.add(map);
                    }
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        res.setData(data);
        String jsonStr = JSON.toJSON(res).toString();

        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json; charset=utf-8");
        out.print(jsonStr);
        out.flush();
        out.close();
    }

    public void POSTRepList(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException
    {

        CommentService commentService=new CommentService();
        DataTableCommand cmd = new DataTableCommand(request.getParameterMap());
        DataTableResult res = new DataTableResult();
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        long weiboId=Long.valueOf(request.getParameter("id"));
        try
        {

            List<Map<String,Object>> items = commentService.getAllComment(weiboId, cmd.getStart(), cmd.getLength());
            if (items != null)
            {
                for (int i = 0; i < items.size(); i++)
                {
                    Map<String,Object> item = items.get(i);
                    if (item != null)
                    {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", item.get("id"));
                        map.put("createUsername", item.get("createUsername"));
                        map.put("createDate", item.get("createDate"));
                        map.put("comment_content", item.get("comment_content"));
                        data.add(map);
                    }
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        res.setData(data);
        String jsonStr = JSON.toJSON(res).toString();

        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json; charset=utf-8");
        out.print(jsonStr);
        out.flush();
        out.close();
    }
    public void GETDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String path = req.getContextPath();
        WeiboService weiboService=new WeiboService();
        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
        req.setAttribute("item", weiboService.findWeiboItemById(Long.valueOf(req.getParameter("id"))));
        req.setAttribute("comment",new CommentService().getAllComment(Long.valueOf(req.getParameter("id")), 0, 10));
        req.getRequestDispatcher("WEB-INF/view/detail.jsp").forward(req, resp);
    }


    public void GETMod(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String path = req.getContextPath();
        WeiboService weiboService=new WeiboService();
        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
        WeiboItem item = weiboService.findWeiboItemById(Long.valueOf(req.getParameter("id").toString()));
        req.setAttribute("item", item);
        req.getRequestDispatcher("WEB-INF/view/modify.jsp").forward(req, resp);
    }

    public void POSTMod(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        WeiboService weiboService=new WeiboService();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("utf-8");
//        String path = req.getContextPath();
//        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";
        String content=req.getParameter("content");
        String weiboId=req.getParameter("id").toString();
        weiboService.updateWeiboItemById(Long.valueOf(weiboId),content);
        String path = req.getContextPath();
        String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path+"/";
        resp.sendRedirect(basePath+"View.do");
    }


}
