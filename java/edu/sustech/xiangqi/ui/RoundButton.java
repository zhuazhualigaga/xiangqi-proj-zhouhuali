package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

public class RoundButton  extends JButton {
    public RoundButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 按钮背景颜色（可根据需求换）
        Color bg = new Color(240, 230, 200, 190);  // 半透明深灰
        Color hover = new Color(255, 245, 220, 230);
        Color press = new Color(220, 190, 140, 230);


        if (getModel().isPressed()) {
            g2.setColor(press);
        } else if (getModel().isRollover()) {
            g2.setColor(hover);
        } else {
            g2.setColor(bg);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();

        super.paintComponent(g);
    }
}