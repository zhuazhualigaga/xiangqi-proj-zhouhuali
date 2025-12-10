package edu.sustech.xiangqi;

import java.io.File;
import edu.sustech.xiangqi.model.ChessBoardModel;

import java.io.PrintWriter;
import edu.sustech.xiangqi.model.AbstractPiece;

public class SaveRepo {

    private final File dir = new File("saves");

    public SaveRepo() {
        if(!dir.exists()) {
            dir.mkdirs();//周：构造方法：没有save就创建
        }
    }


    // 多存档槽位：返回 slot 号的存档文件
    public File slotFile(String username, int slot)
    {
        return new File(dir, username + "_slot" + slot + ".sav");//命名规则
    }

    public void saveToFile(ChessBoardModel model, File file)//负责写入save文件
    {
        try(PrintWriter out = new PrintWriter(file))//打开文件写入器
        {

            String time = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//写时间
            out.println("TIME," + time);
            // 1. 存轮次（TURN）
            if(model.isRedTurn()){
                out.println("TURN,RED");
            } else {
                out.println("TURN,BLACK");
            }


            // 2. 存棋子
            for(AbstractPiece p : model.getPieces())
            {
                String name = p.getName();
                String color = p.isRed() ? "RED" : "BLACK";
                int row = p.getRow();
                int col = p.getCol();
                out.println(name + "," + color + "," + row + "," + col);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 专门用于写入槽位存档，这个是存最终状态到选中档+存整局回放
    public void saveToSlot(ChessBoardModel model, String username, int slot){
        File file = slotFile(username, slot);
        saveToFile(model, file);

        model.getReplayRecorder().saveHistoryToFile("saves/" + username + "_slot" + slot + ".moves");
    }



}
