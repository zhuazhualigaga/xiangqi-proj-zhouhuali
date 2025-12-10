package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.GameFrame;
import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.model.AbstractPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Point;

public class ChessBoardPanel extends JPanel {



    private final ChessBoardModel model;
    private final GameFrame gameFrame;
    private java.util.List<Point> legalMoves = new java.util.ArrayList<>();

    /**
     * 单个棋盘格子的尺寸（px）
     */
    private static final int CELL_SIZE = 80;

    /**
     * 棋盘边界与窗口边界的边距
     */
    private static final int MARGIN = 40;

    private static final int BOARD_MARGIN = 20;  // 外边距，用于画外框和阴影
    int left = MARGIN - BOARD_MARGIN;
    int top = MARGIN - BOARD_MARGIN;
    int right = MARGIN + (ChessBoardModel.getCols() - 1) * CELL_SIZE + BOARD_MARGIN;
    int bottom = MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE + BOARD_MARGIN;

    /**
     * 棋子的半径
     */
    private static final int PIECE_RADIUS = 32;

    private AbstractPiece selectedPiece = null;

    private boolean animating = false;
    private AbstractPiece animatingPiece = null;
    private int fromRow, fromCol, toRow, toCol;
    private int animSteps = 10;       // 一次移动分多少帧
    private int currentStep = 0;

    private double animFromX, animFromY;
    private double animToX, animToY;
    private double animCurrentX, animCurrentY;

    private javax.swing.Timer animTimer;

    public ChessBoardPanel(ChessBoardModel model, GameFrame gameFrame) {
        this.model = model;
        this.gameFrame = gameFrame;

        setPreferredSize(new Dimension(
                CELL_SIZE * (ChessBoardModel.getCols() - 1) + MARGIN * 2,
                CELL_SIZE * (ChessBoardModel.getRows() - 1) + MARGIN * 2
        ));
        setBackground(new Color(220, 179, 92));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

    }

    private void handleMouseClick(int x, int y) {
        int col = Math.round((float)(x - MARGIN) / CELL_SIZE);
        int row = Math.round((float)(y - MARGIN) / CELL_SIZE);

        if (!model.isValidPosition(row, col)) {
            return;
        }

        if (gameFrame.isPaused()){return;}

        if (selectedPiece == null) {
            AbstractPiece p = model.getPieceAt(row,col);
            if (p != null && p.isRed() == model.isRedTurn()){
                selectedPiece = p;
                updateLegalMoves();
            }else {
                selectedPiece = null;
            }
        } else {

            boolean moved = model.movePiece(selectedPiece, row, col);
            if (moved){
                // 启动动画：从 (startRow, startCol) 到 (row, col)
                startMoveAnimation(
                        selectedPiece,
                        selectedPiece.getRow(),   // 以前的 startRow
                        selectedPiece.getCol(),   // 以前的 startCol
                        row, col                  // 目标位置
                );

                selectedPiece = null;
                updateLegalMoves();
                //如果移动则调用gameFrame的函数，判断当前状态，或者做出变化
                gameFrame.onMoveSuccess();

            }else {
                AbstractPiece p = model.getPieceAt(row, col);
                if (p != null && p.isRed() == model.isRedTurn()){
                    selectedPiece = p;
                    updateLegalMoves();
                }
            }
        }
        repaint();

        // 处理完点击事件后，需要重新绘制ui界面才能让界面上的棋子“移动”起来
        // Swing 会将多个请求合并后再重新绘制，因此调用 repaint 后gui不会立刻变更
        // repaint 中会调用 paintComponent，从而重新绘制gui上棋子的位置等

        //this.getparent().repaint
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Demo的GUI都是由Swing中基本的组件组成的，比如背景的格子是用许多个line组合起来实现的，棋子是先绘制一个circle再在上面绘制一个text实现的
        // 因此绘制GUI的过程中需要自己手动计算每个组件的位置（坐标）
        drawBoard(g2d);
        drawPieces(g2d);
        drawLegalMoves(g2d);




    }

    /**
     * 绘制棋盘
     */
//绘制九宫格
    private void drawPalace(Graphics2D g, int rowStart, int rowEnd, int colStart, int colEnd) {
        int x1 = MARGIN + colStart * CELL_SIZE;
        int y1 = MARGIN + rowStart * CELL_SIZE;

        int x2 = MARGIN + colEnd * CELL_SIZE;
        int y2 = MARGIN + rowEnd * CELL_SIZE;

        // 左上到右下
        g.drawLine(x1, y1, x2, y2);

        // 右上到左下
        g.drawLine(x2, y1, x1, y2);
    }




    private void drawBoard(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
//        drawOuterDoubleBorder(g);


        // 绘制横线
        for (int i = 0; i < ChessBoardModel.getRows(); i++) {
            int y = MARGIN + i * CELL_SIZE;
            if (i == 0){
                g.drawLine(MARGIN, y, MARGIN + (ChessBoardModel.getCols() - 1) * CELL_SIZE, y);
                g.drawLine(MARGIN, y-10, MARGIN + (ChessBoardModel.getCols() - 1) * CELL_SIZE, y-10);
            }else if (i == ChessBoardModel.getRows() - 1) {
                g.drawLine(MARGIN, y, MARGIN + (ChessBoardModel.getCols() - 1) * CELL_SIZE, y);
                g.drawLine(MARGIN, y+10, MARGIN + (ChessBoardModel.getCols() - 1) * CELL_SIZE, y+10);
            }
            g.drawLine(MARGIN, y, MARGIN + (ChessBoardModel.getCols() - 1) * CELL_SIZE, y);
        }

        // 绘制竖线
        for (int i = 0; i < ChessBoardModel.getCols(); i++) {
            int x = MARGIN + i * CELL_SIZE;
            if (i == 0) {
                // 两边的竖线贯通整个棋盘
                g.drawLine(x, MARGIN, x, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
                g.drawLine(x-10, MARGIN, x-10, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            } else if (i == ChessBoardModel.getCols() - 1) {
                g.drawLine(x, MARGIN, x, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
                g.drawLine(x+10, MARGIN, x+10, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            } else {
                // 中间的竖线分为上下两段（楚河汉界断开）
                g.drawLine(x, MARGIN, x, MARGIN + 4 * CELL_SIZE);
                g.drawLine(x, MARGIN + 5 * CELL_SIZE, x, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            }
        }

        // 绘制“楚河”和“汉界”这两个文字
        g.setColor(Color.BLACK);
        g.setFont(new Font("楷体", Font.BOLD, 30));

        int riverY = MARGIN + 4 * CELL_SIZE + CELL_SIZE / 2;

        String chuHeText = "楚河";
        FontMetrics fm = g.getFontMetrics();
        int chuHeWidth = fm.stringWidth(chuHeText);
        g.drawString(chuHeText, MARGIN + CELL_SIZE * 2 - chuHeWidth / 2, riverY + 8);

        String hanJieText = "汉界";
        int hanJieWidth = fm.stringWidth(hanJieText);
        g.drawString(hanJieText, MARGIN + CELL_SIZE * 6 - hanJieWidth / 2, riverY + 8);

        //绘制田字格
        // 黑方九宫格
        drawPalace(g, 0, 2, 3, 5);
        drawPalace(g, 7, 9, 3, 5);

//        highlightLastFromCell(g);
    }

    /**
     * 绘制棋子
     */



    private void drawLegalMoves(Graphics2D g){
        if (legalMoves.isEmpty()){
            return;
        }

        for (Point p : legalMoves){
            int col = p.x;
            int row = p.y;
            // 这个格子的左上角位置（一个格子的大小就是 CELL_SIZE x CELL_SIZE）
            int r = 6;

            // 棋盘交叉点中心坐标
            int centerX = MARGIN + col * CELL_SIZE;
            int centerY = MARGIN + row * CELL_SIZE;

            g.setColor(new Color(250, 250, 250, 120)); // 淡白色高亮
            g.fillOval(centerX-r, centerY-r, r*2,r*2);
            g.setColor(new Color(200, 200, 200,180));
            g.setStroke(new BasicStroke(2));
            g.drawOval(centerX-r, centerY-r, r*2, r*2);
        }




    }

    private void drawSinglePiece(Graphics2D g, AbstractPiece piece, int centerX, int centerY) {
        boolean isSelected = (piece == selectedPiece && !animating); // 动画中不算选中
// 基础半径
        double scale = isSelected ? 1.15 : 1.0;  // 选中时放大 15%
        int radius = (int) (PIECE_RADIUS * scale);
        int diameter = radius * 2;

        //选中特效
        if (isSelected) {
            g.setColor(new Color(0, 0, 0, 80));
            int shadowOffset = 4;
            g.fillOval(centerX - radius + shadowOffset,
                    centerY - radius + shadowOffset,
                    diameter, diameter);
//            drawCornerBorders(g,centerX,centerY);
        }


        //棋子圆
        g.setColor(new Color(245, 222, 179));
        g.fillOval(centerX - radius, centerY - radius, diameter, diameter);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval(centerX - radius, centerY - radius, diameter, diameter);

        // 再在circle上面绘制对应的棋子名字
        if (piece.isRed()) {
            g.setColor(new Color(200, 0, 0));
        } else {
            g.setColor(Color.BLACK);
        }
        g.setFont(new Font("楷体", Font.BOLD, 30));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(piece.getName());
        int textHeight = fm.getAscent();
        g.drawString(piece.getName(), centerX - textWidth / 2, centerY + textHeight / 2 - 2);
        // 下面我们会在这里加入“放大+阴影”的效果
    }

    private void drawPieces(Graphics2D g) {
        // 遍历棋盘上的每一个棋子，每次循环绘制该棋子
        for (AbstractPiece piece : model.getPieces()) {
            if (animating && piece == animatingPiece){
                continue;
            }

            drawSinglePiece(g, piece,
                    MARGIN + piece.getCol() * CELL_SIZE,
                    MARGIN + piece.getRow() * CELL_SIZE);
        }

        if (animating && animatingPiece != null){
            drawSinglePiece(g, animatingPiece, (int) animCurrentX, (int) animCurrentY);
        }
    }


    private void startMoveAnimation(AbstractPiece piece,
                                    int fromRow, int fromCol,
                                    int toRow, int toCol) {
        this.animating = true;
        this.animatingPiece = piece;

        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;

        // 起点、终点的像素坐标（棋盘交叉点中心）
        animFromX = MARGIN + fromCol * CELL_SIZE;
        animFromY = MARGIN + fromRow * CELL_SIZE;
        animToX = MARGIN + toCol * CELL_SIZE;
        animToY = MARGIN + toRow * CELL_SIZE;

        currentStep = 0;

        if (animTimer != null && animTimer.isRunning()) {
            animTimer.stop();
        }

        // 每 20ms 一帧，大概 200ms~300ms 完成
        int delay = 20;
        animTimer = new javax.swing.Timer(delay, e -> {
            currentStep++;
            double t = (double) currentStep / animSteps;
            if (t > 1.0) t = 1.0;

            animCurrentX = animFromX + (animToX - animFromX) * t;
            animCurrentY = animFromY + (animToY - animFromY) * t;

            repaint();

            if (currentStep >= animSteps) {
                animating = false;
                animatingPiece = null;
                animTimer.stop();
            }
        });
        animTimer.start();
    }
    private void updateLegalMoves(){
        legalMoves.clear();
        if (selectedPiece == null){
            return;
        }

        for (int row = 0; row < ChessBoardModel.getRows(); row++){
            for (int col = 0; col < ChessBoardModel.getCols(); col++){

                if (selectedPiece.canMoveTo(row, col, model)){
                    legalMoves.add(new Point(col,row));
                }
            }
        }
    }






}