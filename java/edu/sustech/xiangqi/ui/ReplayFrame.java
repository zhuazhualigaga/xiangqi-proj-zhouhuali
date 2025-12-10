//package edu.sustech.xiangqi.ui;
//
//import edu.sustech.xiangqi.model.*;
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.util.List;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.charset.StandardCharsets;
//
//
//
//public class ReplayFrame extends JFrame {
//    private javax.swing.Timer autoTimer;
//
//
//    private ChessBoardModel model;
//    private ChessBoardPanel boardPanel;
//    private List<String> moves;   // 所有历史移动
//    private int index = 0;        // 当前播放到第几步
//
//
//    private boolean isPlaying = false;  // 是否正在自动播放
//
//
//    public ReplayFrame(File movesFile) {
//
//        super("棋局回放");
//        setSize(1500, 950);
//        setLocationRelativeTo(null);
//        setLayout(null);
//
//        // 1. 创建空棋盘模型
//        model=new ChessBoardModel();
//        model.resetGame(); // 清空 & 初始化
//
//        // 2. 棋盘面板
//        boardPanel = new ChessBoardPanel(model, null);
//        boardPanel.setBounds(300, 50, 800, 800);
//        add(boardPanel);
//
//        // 3. 读取历史移动记录
//        try {
//            moves = Files.readAllLines(movesFile.toPath(), StandardCharsets.UTF_8);
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "回放文件读取失败！");
//            moves = java.util.Collections.emptyList();  // 保证程序不崩
//        }
//
//
//
//        // 4. 播放按钮
//        JButton nextBtn = new JButton("下一步");
//        nextBtn.setBounds(1150, 200, 100, 50);
//        add(nextBtn);
//
//        nextBtn.addActionListener(e -> playNext());
//
//        JButton prevBtn = new JButton("上一步");
//        prevBtn.setBounds(1150, 260, 100, 50);
//        add(prevBtn);
//
//        JButton autoBtn = new JButton("播放");
//        autoBtn.setBounds(1150, 360, 100, 50);
//        add(autoBtn);
//        autoBtn.addActionListener(e -> {
//
//            if (!isPlaying) {  // 如果当前是暂停状态 → 开始播放
//                autoTimer = new Timer(800, ee -> playNext());
//                autoTimer.start();
//                isPlaying = true;
//                autoBtn.setText("暂停");  // 切换按钮显示
//            }
//            else {  // 正在播放 → 现在要暂停
//                autoTimer.stop();
//                isPlaying = false;
//                autoBtn.setText("播放");
//            }
//        });
//
//        prevBtn.addActionListener(e -> playPrev());
//
//
//        setVisible(true);
//        JButton backBtn = new JButton("返回");
//        backBtn.setBounds(1150, 300, 100, 50);
//        backBtn.setFont(new Font("楷体", Font.BOLD, 22));
//        add(backBtn);
//
//        backBtn.addActionListener(e -> {
//            if(autoTimer != null) autoTimer.stop(); // 停止自动播放
//            this.dispose();  // 关闭回放窗口，返回主界面
//        });
//
//    }
//
//
//    private void playNext() {
//        // 回放结束则直接退出，不弹窗
//        if (index >= moves.size()) {
//            return;
//        }
//
//
//        String rec = moves.get(index);
//        index++;
//
//        // 解析格式 "兵,6,0->5,0"
//        try {
//            String[] twoParts = rec.split("->"); // ["兵,6,0", "5,0"]
//
//            String[] left = twoParts[0].split(","); // ["兵", "6", "0"]
//            String[] right = twoParts[1].split(","); // ["5", "0"]
//
//            String name = left[0];
//            int oldRow = Integer.parseInt(left[1]);
//            int oldCol = Integer.parseInt(left[2]);
//
//            int newRow = Integer.parseInt(right[0]);
//            int newCol = Integer.parseInt(right[1]);
//
//
//            // 找棋子
//            AbstractPiece p = model.getPieceAt(oldRow, oldCol);
//            if(p != null)
//            {
//                model.movePiece(p, newRow, newCol);
//                boardPanel.repaint();
//            }
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "回放数据损坏，无法继续！");
//        }
//
//
//    }
//    private void playPrev() {
//        // 如果已经在开局，就不能再退
//        if (index <= 0) {
//            return;
//        }
//
//        // index 指向上一个动作
//        index--;
//
//        // ★ 重置棋盘为初始局面
//        model.resetGame();
//
//        // ★ 重放 index 之前的所有动作
//        for (int i = 0; i < index; i++) {
//            applyMove(moves.get(i));
//        }
//
//        boardPanel.repaint();
//    }
//    private void applyMove(String rec) {
//        try {
//            String[] twoParts = rec.split("->");
//            String[] left = twoParts[0].split(",");
//            String[] right = twoParts[1].split(",");
//
//            int oldRow = Integer.parseInt(left[1]);
//            int oldCol = Integer.parseInt(left[2]);
//            int newRow = Integer.parseInt(right[0]);
//            int newCol = Integer.parseInt(right[1]);
//
//            AbstractPiece p = model.getPieceAt(oldRow, oldCol);
//            if (p != null) {
//                model.forceMove(p, newRow, newCol);
//            }
//
//        } catch (Exception ignored) {}
//    }
//
//
//
//}
package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ReplayFrame extends JFrame {//这个是用来做回访的

    private javax.swing.Timer autoTimer;   //周：只保留 Swing Timer，自动播放用的计时器

    private ChessBoardModel model;//周：数据层
    private ChessBoardPanel boardPanel;
    private List<String> moves;// 周：回放记录（每一步）
    private int index = 0;// 周：当前播放到第几步

    private boolean isPlaying = false;  // 是否正在自动播放

    public ReplayFrame(File movesFile) {

        super("棋局回放");
        setSize(1500, 950);
        setLocationRelativeTo(null);//周：居中显示
        setLayout(null);

        // 1. 创建棋盘模型
        model = new ChessBoardModel();
        model.resetGame();//初始摆盘

        // 2. 棋盘面板，负责画期盼和棋子
        boardPanel = new ChessBoardPanel(model, null);
        boardPanel.setBounds(300, 50, 800, 800);
        add(boardPanel);

        // 3. 加载回放文件
        try {
            moves = Files.readAllLines(movesFile.toPath(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "回放文件读取失败！");
            moves = java.util.Collections.emptyList();
        }

        // --- 按钮区 ---
        JButton nextBtn = new JButton("下一步");
        nextBtn.setBounds(1150, 200, 100, 50);
        nextBtn.addActionListener(e -> playNext());
        add(nextBtn);

        JButton prevBtn = new JButton("上一步");
        prevBtn.setBounds(1150, 260, 100, 50);
        prevBtn.addActionListener(e -> playPrev());
        add(prevBtn);

        JButton autoBtn = new JButton("播放");
        autoBtn.setBounds(1150, 360, 100, 50);
        add(autoBtn);

        autoBtn.addActionListener(e -> {
            if (!isPlaying) {
                // 开始自动播放
                autoTimer = new javax.swing.Timer(800, ee -> playNext());
                autoTimer.start();
                isPlaying = true;
                autoBtn.setText("暂停");
            } else {
                // 暂停
                autoTimer.stop();
                isPlaying = false;
                autoBtn.setText("播放");
            }
        });

        JButton backBtn = new JButton("返回");
        backBtn.setBounds(1150, 430, 100, 50);
        backBtn.addActionListener(e -> {
            if (autoTimer != null) autoTimer.stop();
            this.dispose();
        });
        add(backBtn);

        setVisible(true);
    }

    //周：播放下一步
    private void playNext() {

        if (index >= moves.size()) {
            if (autoTimer != null)
            {
                autoTimer.stop();
            }
            isPlaying = false;
            return;
        }

        applyMove(moves.get(index));
        index++;

        boardPanel.repaint();
    }

    // 上一步
    private void playPrev() {

        if (index <= 0)
        {
            return;
        }

        index--;//防止越界，回放上一步并不是撤销，而是从初始态重放 index 次

        // 重置到初始局面
        model.resetGame();

        // 重放 index 之前的所有动作
        for (int i = 0; i < index; i++) {
            applyMove(moves.get(i));
        }

        boardPanel.repaint();
    }

    // 执行一步棋
    private void applyMove(String rec) {
        try {
            String[] parts = rec.split("->");
            String[] left = parts[0].split(",");
            String[] right = parts[1].split(",");

            int oldRow = Integer.parseInt(left[1]);
            int oldCol = Integer.parseInt(left[2]);
            int newRow = Integer.parseInt(right[0]);
            int newCol = Integer.parseInt(right[1]);

            AbstractPiece p = model.getPieceAt(oldRow, oldCol);
            if (p != null) {
                model.forceMove(p, newRow, newCol);
            }

        } catch (Exception ignored) {}
    }
}
