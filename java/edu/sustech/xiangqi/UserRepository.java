package edu.sustech.xiangqi;

import java.io.*;
import java.util.*;

public class UserRepository {                //周:这是一个用户数据库管理器，读取账号，密码比对的，也许你看看能用得上
    public Boolean userexists(String username)//周：登录和注册中的检查是否有这个个
    {
        File file=new File(".\\user.txt");//周：路径类，存的账号
        Scanner in;
        try
        {
            in=new Scanner(file);//周：读取账号
            while(in.hasNextLine())
            {
                if (username.equals(in.nextLine()))
                {
                    return true;
                }
            }
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();

        }

        return false;
    }

    public boolean checkPassword(String username, String password)//周：比对密码
    {
        File userfile=new File("user.txt");
        File passwordfile=new File("password.txt");

        Scanner userin;

        Scanner passwordin;//它俩同步扫描

        try
        {
            userin=new Scanner(userfile);
            passwordin=new Scanner(passwordfile);
            while (userin.hasNextLine()&&passwordin.hasNextLine())
            {
                String u=userin.nextLine();
                String p=passwordin.nextLine();
                if (username.equals(u))
                {
                    return password.equals(p);//周： 布尔值返回，直接判断

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public void adduser(String username, String password)//周：注册中的添加
    {

        try
         {
             BufferedWriter writer=new BufferedWriter(new FileWriter(".\\user.txt",true));//周：true表示追加模式，不覆盖原来内容
             String u=username;
             writer.write(u);//写入账号
             writer.newLine();//换行
             writer.close();//关闭文件，重要
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            BufferedWriter writer=new BufferedWriter(new FileWriter(".\\password.txt",true));
            String u=new String (password);
            writer.write(u);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    }



