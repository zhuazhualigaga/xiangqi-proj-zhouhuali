package edu.sustech.xiangqi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveRecorder {
    private final List<String> records = new ArrayList<String>();

    public void addRecord(AbstractPiece piece,
                          int fromRow, int fromCol,
                          int toRow, int toCol){
        records.add(formatMove(piece, fromRow, fromCol, toRow, toCol));
    }

    public List<String> getRecords(){
        return Collections.unmodifiableList(records);
    }

    private static final String[] Chinese_num = {
            "一","二","三","四","五","六","七","八","九","十"
    };

    private String fileToChinese(boolean red, int col){
        int file; //1-9
        if (red){
            file = 9 - col; //1-9
        }else {
            file = col + 1; //1-9
        }
        return Chinese_num[file-1];
    }

    private String rankToChinese(boolean red, int row){
        int rank; // 1-10
        if (red){
            rank = 10 - row;
        }else {
            rank = row + 1;
        }
        return Chinese_num[rank-1];
    }

    public String formatMove(AbstractPiece piece,
                             int fromRow, int fromCol,
                             int toRow, int toCol){
        boolean red = piece.isRed();
        String pieceName = piece.getName();
        String fromFile = fileToChinese(red, fromCol);

        String action; //进、退、平
        String targetPart;

        if (fromRow == toRow){ //左右移动，平
            action = "平";
            targetPart = fileToChinese(red,toCol);
        }else {
            targetPart = rankToChinese(red, toRow);
            if (red){//红方向下是退
                if (fromRow > toRow){action = "进";}
                else {action = "退";}
            }else {
                if (fromRow > toRow){action = "退";}
                else {action = "进";}
            }
        }
        return pieceName + fromFile + action + targetPart;
    }
}