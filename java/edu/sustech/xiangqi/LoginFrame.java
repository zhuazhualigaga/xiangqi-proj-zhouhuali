package edu.sustech.xiangqi;

import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.ui.ChessBoardPanel;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class LoginFrame extends JFrame{
    //登录框
    private JFrame loginFrame;
    private JTextField username;
    private JLabel label2;
    private JTextField password;
    private JLabel label1;
    private JButton button1;


    public LoginFrame() {
        loginFrame = new JFrame("login");
        loginFrame.setLayout(null);//                 周：使用绝对定位布局（null layout）
        loginFrame.setSize(300,300);

        username = new JTextField("");
        username.setLocation(100,20);
        username.setSize(100,50);
        loginFrame.add(username);

        label2 = new JLabel("username");
        label2.setSize(100,50);
        label2.setLocation(30,20);
        loginFrame.add(label2);

        password = new JTextField("");
        password.setLocation(100,80);
        password.setSize(100,50);
        loginFrame.add(password);

        label1 = new JLabel("password");
        label1.setSize(100,50);
        label1.setLocation(30,80);
        loginFrame.add(label1);

        button1 = new JButton("login");
        button1.setSize(100,50);
        button1.setLocation(90,150);
        loginFrame.add(button1);


        loginFrame.setVisible(true);
    }
}