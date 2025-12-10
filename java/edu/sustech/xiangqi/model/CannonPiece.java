package edu.sustech.xiangqi.model;

public class CannonPiece extends AbstractPiece {
    public CannonPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        // 炮的移动规则：
        int currentRow = getRow();
        int currentCol = getCol();

        int rowDiff = targetRow - currentRow;
        int colDiff = targetCol - currentCol;

        AbstractPiece target = model.getPieceAt(targetRow, targetCol);
        int countBetween = 0;

        //不能走到己方棋子上
        if (target!=null && target.isRed()==this.isRed()){
            return false;
        }

        // 统计起点和终点之间有多少个棋子（不包括终点）
        if (rowDiff != 0) {//上下行走
            for (int i = 1; i < Math.abs(rowDiff); i++) {
                if (rowDiff > 0) {
                    if (model.getPieceAt(currentRow + i, currentCol) != null) {
                        countBetween++;
                    }
                }
                if (rowDiff < 0) {
                    if (model.getPieceAt(currentRow - i, currentCol) != null) {
                        countBetween++;
                    }
                }
            }
        }else {//左右行走
            for (int i = 1; i < Math.abs(colDiff); i++) {
                if (colDiff > 0) {
                    if (model.getPieceAt(currentRow, currentCol + i) != null) {
                        countBetween++;
                    }
                }
                if (colDiff < 0) {
                    if (model.getPieceAt(currentRow, currentCol - i) != null) {
                        countBetween++;
                    }
                }
            }
        }

        // 1、炮只能走直线
        if (rowDiff != 0 && colDiff != 0) {
            return false;
        }
        //2、不吃子
        if (target == null) {
            if (countBetween == 0){
                return true;
            }
        }else {
            if (countBetween == 1){
                return true;
            }
        }
        return false;
    }
}