package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.GameFrame;

import javax.swing.*;
import java.awt.*;

public class PauseOverlay extends JPanel {

    private Runnable onResume;
    private Runnable onRestart;   // 新增
    private final Runnable onQuit;
//    private final Runnable onRegret;

    public PauseOverlay(Runnable onResume,
                        Runnable onRestart,
                        Runnable onQuit) {
        this.onResume = onResume;
        this.onRestart = onRestart;
        this.onQuit = onQuit;
//        this.onRegret = onRegret;
        setOpaque(false);
        setLayout(new GridBagLayout()); // 用布局管理器
        initUI();
    }

    private void initUI() {
        // 中间的对话框 Panel
        JPanel dialog = new JPanel();
        dialog.setOpaque(false);
        dialog.setPreferredSize(new Dimension(600,500));
        dialog.setLayout(new GridLayout(2, 2, 10, 10)); // 1 行标题 + 3 个按钮


        JLabel title = new JLabel("游戏暂停", SwingConstants.CENTER);
        title.setFont(new Font("楷体", Font.BOLD, 24));

        Dimension btnSize = new Dimension(60, 220); // 例如，宽220，高60

        CardButton resumeBtn = new CardButton("继续游戏");
        Image img = new ImageIcon("src/main/resources/1001/13.jpg").getImage();
        resumeBtn.setBackgroundImage(img);
        resumeBtn.setActive(true);

        CardButton restartBtn = new CardButton("重新开始");
        CardButton quitBtn = new CardButton("返回主菜单");
        CardButton regret = new CardButton("悔棋");


        Font f = new Font("楷体", Font.BOLD, 20);
        for (JButton b : new JButton[]{resumeBtn, restartBtn, quitBtn, regret}) {
            b.setFont(f);
            b.setPreferredSize(btnSize);
        }

//        dialog.add(title);
        dialog.add(resumeBtn);
        dialog.add(restartBtn);
        dialog.add(quitBtn);
        dialog.add(regret);

        GridBagConstraints gbc = new GridBagConstraints();
        add(dialog, gbc);

        // 按钮事件
        resumeBtn.addActionListener(e -> {
            if (onResume != null) onResume.run();
        });

        // 关键：重新开始按钮的事件
        restartBtn.addActionListener(e -> {
            if (onRestart != null) onRestart.run();
        });
//        restartBtn.addActionListener(e -> );
//         restartBtn, quitBtn：在外面 new PauseOverlay 的时候，
//         也可以通过 set 方法或构造参数把逻辑传进来
    }


    @Override
    protected void paintComponent(Graphics g) {
        // 先调用 super（习惯好一点）
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}