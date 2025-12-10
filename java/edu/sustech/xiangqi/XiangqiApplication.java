package edu.sustech.xiangqi;
import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.ui.ChessBoardPanel;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class XiangqiApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {      //周：所有图形界面（GUI）的创建必须放在 Swing 的线程里运行
            JFrame LoginFrame=new JFrame("登录");//周：窗口标题是“登录”
            LoginFrame.setLayout(null);         //周：null布局等于手动摆放布局
            LoginFrame.setSize(600,600);

            JTextField username=new JTextField();//周：创建一个可输入文本的组件，输入框，username，用来存账户名字
            username.setLocation(100,50);
            username.setSize(400,50);
            LoginFrame.add(username);//周：把这个组件添加到窗口上显示


            JLabel charusername=new JLabel("账户");//周：label标签，只显示文字
            charusername.setSize(100,50);
            charusername.setLocation(30,50);
            LoginFrame.add(charusername);
            charusername.setFont(new Font("微软雅黑", Font.BOLD, 30));


            JTextField password=new JTextField();
            password.setLocation(100,110);
            password.setSize(400,50);
            LoginFrame.add(password);


            JLabel charpassword=new JLabel("密码");
            charpassword.setSize(100,50);
            charpassword.setLocation(30,110);
            charpassword.setFont(new Font("微软雅黑", Font.BOLD, 30));
            LoginFrame.add(charpassword);

            JButton loginbutton=new JButton("登录");
            loginbutton.setLocation(100,200);
            loginbutton.setSize(100,50);
            LoginFrame.add(loginbutton);

            JButton newer=new JButton("注册");
            newer.setLocation(250,200);
            newer.setSize(100,50);
            LoginFrame.add(newer);

            JButton visit=new JButton("访客模式");
            visit.setLocation(400,200);
            visit.setSize(100,50);
            LoginFrame.add(visit);


            LoginFrame.setVisible(true);

            UserRepository repo=new UserRepository();
            loginbutton.addActionListener(e -> {
                String u=username.getText();//周：内置方法，获取输入框里的文字
                String p=password.getText();
                if(!repo.userexists(u))
                {
                    JOptionPane.showMessageDialog(LoginFrame, "用户不存在，请先注册");//周：在 LoginFrame 这个窗口的中间，弹出一个消息框
                    return;
                }



                if (!repo.checkPassword(u,p))
                {
                    JOptionPane.showMessageDialog(LoginFrame, "密码错误");
                    return;
                }
                JOptionPane.showMessageDialog(LoginFrame, "登录成功！");

                GameFrame game=new GameFrame(u, false);//周：新开一个游戏，（名字，是否游客）
                game.setVisible(true);
                LoginFrame.dispose();


            });



            newer.addActionListener(e -> {
                //周：注册
                JFrame registerFrame = new JFrame("注册");
                registerFrame.setLayout(null);//周：null布局等于手动摆放布局
                registerFrame.setSize(400,300);
                registerFrame.setLocationRelativeTo(LoginFrame); //周：居中到登录窗附近，registerFrame居中到LoginFrame

                JLabel regUserLabel = new JLabel("账号");
                regUserLabel.setBounds(40,40,60,30);
                registerFrame.add(regUserLabel);

                JTextField regUserField = new JTextField();
                regUserField.setBounds(100,40,200,30);
                registerFrame.add(regUserField);

                JLabel regPassLabel = new JLabel("密码");
                regPassLabel.setBounds(40,90,60,30);
                registerFrame.add(regPassLabel);

                JTextField regPassField = new JTextField();
                regPassField.setBounds(100,90,200,30);
                registerFrame.add(regPassField);

                JButton confirmBtn = new JButton("确认注册");
                confirmBtn.setBounds(140,150,100,40);
                registerFrame.add(confirmBtn);


                confirmBtn.addActionListener(e2 -> {
                    String u = regUserField.getText();   // 注册窗口里的账号
                    String p = regPassField.getText();   // 注册窗口里的密码

                    // 判空
                    if (u.isEmpty())
                    {
                        JOptionPane.showMessageDialog(registerFrame, "请输入账号");
                        return;
                    }
                    if (p.isEmpty())
                    {
                        JOptionPane.showMessageDialog(registerFrame, "请输入密码");
                        return;
                    }

                    // 检查重
                    if (repo.userexists(u)==true)
                    {
                        JOptionPane.showMessageDialog(registerFrame, "账号已存在！请直接登录");
                        return;
                    }
                    repo.adduser(u,p);
                    JOptionPane.showMessageDialog(registerFrame, "注册成功，请在登录界面登录！");
                    registerFrame.dispose();
                });

                registerFrame.setVisible(true);
            });

            visit.addActionListener(e -> {
                JOptionPane.showMessageDialog(LoginFrame,"游客模式：不能存档");

                GameFrame game=new GameFrame("GUEST", true);
                game.setVisible(true);
                LoginFrame.dispose();

            });




        });
    }


}
