package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

public class MoveTagColumn extends JPanel {

    private final RoundMoveLabel[] labels = new RoundMoveLabel[3];

    public MoveTagColumn() {
        setOpaque(false); // 背景透明，让背景图透出来
        setLayout(new GridLayout(3, 1, 5, 5)); // 三行一列，中间留 5px 间距

        for (int i = 0; i < 3; i++) {
            labels[i] = new RoundMoveLabel();
            add(labels[i]);
        }
    }

    /** 往这列添加一个新走子记录，只保留最近 3 条 */
    public void addMove(String text) {
        // 旧记录往上挤
        labels[0].setText(labels[1].getText());
        labels[1].setText(labels[2].getText());
        labels[2].setText(text);
    }

    /** 设置这三个标签是否为“当前方”（不透明/半透明） */
    public void setActive(boolean active) {
        for (RoundMoveLabel label : labels) {
            label.setActive(active);
        }
    }

    /** 清空所有标签（比如重新开局时用） */
    public void clear() {
        for (RoundMoveLabel label : labels) {
            label.setText("");
        }
    }
}