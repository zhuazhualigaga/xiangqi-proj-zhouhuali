package edu.sustech.xiangqi.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CardButton extends JButton {

    private Image backgroundImage;   // 可选背景图
    private boolean active = true; // true: 当前方走，不透明；false: 半透明

    public CardButton(String text) {
        super(text);
        setContentAreaFilled(false);  // 不用默认背景
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(new Font("楷体", Font.PLAIN, 20));
        setForeground(new Color(40, 40, 40));
    }

    /** 设置背景图片（可选） */
    public void setBackgroundImage(Image img) {
        this.backgroundImage = img;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(280, 100); // 你可以按需求调整大小
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 25;

        // 1. 画背景图片（如果设置了）
        if (backgroundImage != null) {
            g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
            g2.drawImage(backgroundImage, 0, 0, w, h, this);
            g2.setClip(null);
        }

//         2. 填充浅灰色背景（稍微透明一点，显得柔和）
        //todo: 激活与非激活之间的状态转换
//        Color bg = active
//                ? new Color(245, 235, 210, 240)
//                : new Color(245, 235, 210, 100);
//        Color border = new Color(150, 120, 80,
//                active ? 220 : 100);
        if (!active) {
            Color fill = new Color(80, 80, 80, 220);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);
            // 背景颜色：浅色木质感，active 时更实，inactive 时更透明
        }

        // 3. 双实线边框
        Color border = new Color(120, 120, 120);
        g2.setColor(border);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(7, 7, w - 15, h - 15, (arc - 4), arc - 4);

        // 4. 鼠标悬停/按下的轻微高亮
        if (getModel().isRollover()) {
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);
        }
        if (getModel().isPressed()) {
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);
        }

        g2.dispose();

        // 5. 最后画文字（支持 HTML 富文本）
        super.paintComponent(g);
    }

    public void setActive(boolean active){
        this.active = active;
        repaint();
    }
}