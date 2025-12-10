package edu.sustech.xiangqi.model;

public class ElephantPiece extends AbstractPiece{
    public ElephantPiece(String name, int row, int col, boolean isRed) {super(name, row, col, isRed);}

    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();
        //不可以原地行走
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        //不能走到己方棋子上
        AbstractPiece p = model.getPieceAt(targetRow,targetCol);
        if (p!=null && p.isRed()==this.isRed()){
            return false;
        }

        int rowDiff = targetRow - currentRow;
        int colDiff = targetCol - currentCol;

        // 象的移动规则：横向走两格再纵向走两格
        if (isRed()) {
            // 红方兵（向上走，row减小）
            boolean crossedRiver = targetRow < 5; // 过了楚河汉界
            if (crossedRiver) {//过河
                return false;
            } else {        // 关键限制 1：不能过河
                if (rowDiff == 2 && colDiff == 2 && model.getPieceAt(targetRow, targetCol)==null) return true;
                if (rowDiff == 2 && colDiff == -2 && model.getPieceAt(targetRow, targetCol)==null) return true;
                if (rowDiff == -2 && colDiff == 2 && model.getPieceAt(targetRow, targetCol)==null) return true;
                return rowDiff == -2 && colDiff == -2 && model.getPieceAt(targetRow, targetCol) == null;
            }
        } else {
            // 黑方卒（向下走，row增大）
            boolean crossedRiver = targetRow >= 5; // 过了楚河汉界

            if (crossedRiver) {
                return false;
            } else {
                if (rowDiff == 2 && colDiff == 2 && model.getPieceAt(targetRow, targetCol)==null) return true;
                if (rowDiff == 2 && colDiff == -2 && model.getPieceAt(targetRow, targetCol)==null) return true;
                if (rowDiff == -2 && colDiff == 2 && model.getPieceAt(targetRow, targetCol)==null) return true;
                return rowDiff == -2 && colDiff == -2 && model.getPieceAt(targetRow, targetCol) == null;
            }
        }
    }
}