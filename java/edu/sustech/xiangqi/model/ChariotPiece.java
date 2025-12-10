package edu.sustech.xiangqi.model;

public class ChariotPiece extends AbstractPiece{
    public ChariotPiece(String name, int row, int col, boolean isRed) {super(name, row, col, isRed);}

    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();

        // 车的移动规则：
        int rowDiff = targetRow - currentRow;
        int colDiff = targetCol - currentCol;

        //不能走到己方棋子上
        AbstractPiece p = model.getPieceAt(targetRow,targetCol);
        if (p!=null && p.isRed()==this.isRed()){
            return false;
        }

        //1、只能上下行走
        if (rowDiff!=0 && colDiff!=0){
            return false;
        }
        boolean countbetween = true;

        if (rowDiff!=0){
            for (int i=1; i<Math.abs(rowDiff); i++){
                if (rowDiff>0) {
                    if (model.getPieceAt(currentRow+i, currentCol)!= null) {
                        countbetween = false;
                    }
                }
                if (rowDiff<0){
                    if (model.getPieceAt(currentRow-i, currentCol)!=null){
                        countbetween = false;
                    }
                }
            }
        }else{
            for (int i=1; i<Math.abs(colDiff); i++){
                if (colDiff>0) {
                    if (model.getPieceAt(currentRow, currentCol+i) != null) {
                        countbetween = false;
                    }
                }
                if (colDiff<0){
                    if (model.getPieceAt(currentRow , currentCol-i) !=null){
                        countbetween = false;
                    }
                }
            }
        }

//      2、执行路上不能有棋子
        return countbetween;
    }
}