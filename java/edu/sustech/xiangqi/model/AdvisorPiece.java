package edu.sustech.xiangqi.model;

public class AdvisorPiece extends AbstractPiece {
    public AdvisorPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();
        int rowDiff = Math.abs(targetRow-currentRow);
        int colDiff = Math.abs(targetCol-currentCol);
        //不能走到己方棋子上
        AbstractPiece p = model.getPieceAt(targetRow,targetCol);
        if (p!=null && p.isRed()==this.isRed()){
            return false;
        }

        //TODO:核心行走方式：九宫斜走
        //仕（士）只能在己方 “九宫格”（3×3 的田字格区域）内移动。
        //移动轨迹为斜走一格，即从九宫的一个交叉点，斜向走到相邻的交叉点，形似 “走对角线”。
        //仕（士）的所有移动必须在九宫格内完成，一旦踏出九宫边界即为非法走法。

        if (isRed()) {
            // 红方兵（向上走，row减小）
            if (rowDiff == 1 && colDiff == 1 ){
                return targetCol >= 3 && targetCol <= 5 && targetRow >= 7;
            }
        } else {
            if (rowDiff == 1 && colDiff == 1 ){
                return targetCol >= 3 && targetCol <= 5 && targetRow <= 2 ;
            }
        }
        return false;
    }
}