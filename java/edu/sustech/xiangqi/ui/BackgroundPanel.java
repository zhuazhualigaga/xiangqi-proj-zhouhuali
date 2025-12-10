package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image bg;

    public BackgroundPanel(){
        bg = new ImageIcon("src/main/resources/底图.png").getImage();//周：注意路径不一样，当时我找了好久的错误，然后这个图我当时原图无法导入，就用截图给他稍微改了尺寸
        setLayout(null);
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }

}