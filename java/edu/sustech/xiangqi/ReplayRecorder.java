package edu.sustech.xiangqi;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.sustech.xiangqi.model.AbstractPiece;


public class ReplayRecorder {//周：记录每一步走棋的坐标变化，用于回放系统（不是用于游戏界面显示中文棋谱！那个是 MoveRecorder）

    private List<String> records = new ArrayList<>();//列表，用来存一整局棋的全部走法。

    public void addRecord(AbstractPiece piece, int oldRow, int oldCol, int newRow, int newCol)//周添加一条走棋记录
    {
        String rec = piece.getName() + "," + oldRow + "," + oldCol + "->" + newRow + "," + newCol;
        records.add(rec);
    }

    public List<String> getRecords()//读取全部记录
    {
        return records;
    }

    public void clear()//清空记录
    {
        records.clear();
    }

    // 保存到文件moves
    public void saveHistoryToFile(String filename){
        try(PrintWriter out = new PrintWriter(new File(filename))){
            for(String record : records)
            {
                out.println(record);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //  从文件加载
    public void loadHistoryFromFile(String filename){
        records.clear();
        try(Scanner in = new Scanner(new File(filename))){
            while(in.hasNextLine()){
                records.add(in.nextLine());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

