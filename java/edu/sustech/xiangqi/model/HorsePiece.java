package edu.sustech.xiangqi.model;

public class HorsePiece extends AbstractPiece{
    public HorsePiece(String name, int row, int col, boolean isRed) {super(name, row, col, isRed);}

    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model){
        int currentRow = getRow();
        int currentCol = getCol();
        int colDiff = targetCol - currentCol;
        int rowDiff = targetRow - currentRow;

        //不能走到己方棋子上
        AbstractPiece p = model.getPieceAt(targetRow,targetCol);
        if (p!=null && p.isRed()==this.isRed()){
            return false;
        }

        // todo:马的移动规则：
        // 1. 一种是先横向走两格，再纵向走一格；另一种是先纵向走两格，再横向走一格。
        // 马可以向八个方向行走，只要符合 “日” 字轨迹且无限制，就能到达对应落点。
        //红方和黑方的行走规则一致
        // 2. 蹩马腿
        //马行走时，向前走 “日” 时，正前方的格子有棋子，就属于 “蹩马腿”。
        if (((rowDiff == 1 && colDiff == 2) || (rowDiff == -1 && colDiff == 2)) && (model.getPieceAt(currentRow, currentCol+1)==null)){return true;}
        if (((rowDiff == 2 && colDiff == 1) || (rowDiff == 2 && colDiff == -1)) && model.getPieceAt(currentRow+1, currentCol)==null){return true;}
        if (((rowDiff == 1 && colDiff == -2) || (rowDiff == -1 && colDiff == -2)) && model.getPieceAt(currentRow, currentCol-1)==null){return true;}
        return (((rowDiff == -2 && colDiff == -1) || (rowDiff == -2 && colDiff == 1)) && model.getPieceAt(currentRow-1, currentCol)==null);
    }
}