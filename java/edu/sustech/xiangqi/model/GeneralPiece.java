package edu.sustech.xiangqi.model;

/**
 * 帅/将
 */
public class GeneralPiece extends AbstractPiece {

    public GeneralPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        // TODO: 将/帅的移动规则：
        // 1. 单格走
        // 2. 不能离开田字格
        int currentRow = getRow();
        int currentCol = getCol();

        //不能走到己方棋子上
        AbstractPiece p = model.getPieceAt(targetRow,targetCol);
        if (p!=null && p.isRed()==this.isRed()){
            return false;
        }

        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        int rowDiff = targetRow - currentRow;
        int colDiff = targetCol - currentCol;

        if (isRed()) {
            // 向前（向上）一步
            if (rowDiff == -1 && colDiff == 0 && currentRow > 7) {return true;}
            else if(rowDiff == 1 && colDiff == 0 && currentRow < 9){return true;}
            else if(rowDiff == 0 && colDiff == 1 && currentCol < 5){return true;}
            else if(rowDiff == 0 && colDiff == -1 && currentCol > 3){return true;}

        } else {
            // 黑方卒（向下走，row增大）
            if (rowDiff == -1 && colDiff == 0 && currentRow > 0) {return true;}
            else if(rowDiff == 1 && colDiff == 0 && currentRow < 2){return true;}
            else if(rowDiff == 0 && colDiff == 1 && currentCol < 5){return true;}
            else if(rowDiff == 0 && colDiff == -1 && currentCol > 3){return true;}
        }
        return false;
    }
}