package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

public class RoundMoveLabel extends JComponent {

    private String text = "";
    private boolean active = true; // true: 当前方走，不透明；false: 半透明

    public RoundMoveLabel() {
        setOpaque(false); // 自己不画默认背景
        setFont(new Font("楷体", Font.PLAIN, 20));
    }

    public void setText(String text) {
        this.text = text == null ? "" : text;
        repaint();
    }

    public String getText() {
        return text;
    }

    public void setActive(boolean active) {
        this.active = active;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(80, 35); // 你可以按需要调整
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 20;

        // 背景颜色：浅色木质感，active 时更实，inactive 时更透明
        Color bg = active
                ? new Color(245, 235, 210, 240)
                : new Color(245, 235, 210, 100);
        Color border = new Color(150, 120, 80,
                active ? 220 : 100);

        // 背景
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);

        // 边框
        g2.setColor(border);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

        // 文本
        g2.setFont(getFont());
        g2.setColor(new Color(60, 40, 20,
                active ? 255 : 180));

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        int textX = (w - textWidth) / 2;
        int textY = (h + textHeight) / 2 - 3;

        g2.drawString(text, textX, textY);

        g2.dispose();
    }
}