package utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by 15546 on 2017/6/3.
 */
public class RandomChinese
{
//    public static void main(String[] args)
//    {
//        WeiboService service = new WeiboService();
//
//        for (int k = 0; k < 800; k++)
//        {
//            String str = "";
//            for (int i = 1; i < 24; i++)
//            {
//
//                str += String.valueOf(getRandomChar());
//            }
//            WeiboItem item = new WeiboItem();
//            item.setClickNum(new Random().nextInt(60));
//            item.setLikeNum(new Random().nextInt(2000));
//            item.setUserName("huangwei");
//            item.setContent(str);
//            item.setCreateDate(new Date());
//            item.setId(k + 1);
//            service.addWeiboWithId(item);
//        }
//    }

    private static char getRandomChar()
    {
        String str = "";
        int hightPos; //
        int lowPos;

        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try
        {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            System.out.println("错误");
        }

        return str.charAt(0);
    }
}
