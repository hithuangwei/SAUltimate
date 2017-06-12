package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

/**
 * Created by 15546 on 2017/6/10.
 */
public class JSONUtils
{
    static String[] url = new String[]{"add", "delete", "detail", "top", "login", "logout", "index", "modify", "register"};
    static String[] name = new String[]{"Pub", "Del", "Detail", "Top", "Login", "Logout", "View", "Mod", "Reg"};
    static String[] viewName = new String[]{"default", "", "default", "", "login", "logout", "default", "modify", "reg"};

    public static String generateJSON()
    {


        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < url.length; i++)
        {
            JSONObject itemJSONObject = new JSONObject();
            itemJSONObject.put("methodName", name[i]);
            itemJSONObject.put("viewName", viewName[i]);
            jsonObject.put(url[i], itemJSONObject);
        }


        return jsonObject.toString();
    }

//    public static String getMethodName(String text, String key)
//    {
//        JSONObject jsonObject = JSON.parseObject(text);
//        return jsonObject.get(key).toString();
//
//    }

    public static String getViewName(String op)
    {
        String text = read();
        JSONObject jsonObject = JSON.parseObject(text);
        JSONObject jsonObject1=(JSONObject)jsonObject.get(op);
        return jsonObject1.get("viewName").toString();

    }


    public static void write() throws IOException
    {
        String str = generateJSON();
        String temp = System.getProperty("user.dir");
        File txt = new File(temp + "\\src\\main\\resources\\url_pattern.json");
        if (!txt.exists())
        {
            try
            {
                txt.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        byte bytes[] = new byte[512];
        bytes = str.getBytes();   //新加的
        int b = str.length();   //改
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(txt);
            fos.write(bytes, 0, b);
            fos.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static String getXmlPath()
    {
        //file:/D:/JavaWeb/.metadata/.me_tcat/webapps/TestBeanUtils/WEB-INF/classes/
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        path = path.replace('/', '\\'); // 将/换成\
        path = path.replace("file:", ""); //去掉file:
        path = path.replace("classes\\", ""); //去掉class\
        path = path.substring(1); //去掉第一个\,如 \D:\JavaWeb...
        path += "url_pattern.json";
        //System.out.println(path);
        return path;
    }

    public static String read()
    {
//        String temp = System.getProperty("user.dir");
//        String absPath = temp + "\\src\\main\\resources\\url_pattern.json";
        String absPath = getXmlPath();
        // 读取txt内容为字符串
        StringBuffer txtContent = new StringBuffer();
        // 每次读取的byte数
        byte[] b = new byte[8 * 1024];
        InputStream in = null;
        try
        {
            // 文件输入流
            in = new FileInputStream(absPath);
            while (in.read(b) != -1)
            {
                // 字符串拼接
                txtContent.append(new String(b));
            }
            // 关闭流
            in.close();
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return txtContent.toString();
    }
}
