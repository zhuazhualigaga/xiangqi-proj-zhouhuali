package edu.sustech.xiangqi.model;

import java.util.ArrayList;
import java.util.List;
import java.io.File;


import edu.sustech.xiangqi.ReplayRecorder;
import edu.sustech.xiangqi.model.MoveRecorder;



public class ChessBoardModel {

    private MoveRecorder moveRecorder = new MoveRecorder();      // 周：显示中文棋谱，就是你的那个
    private ReplayRecorder replayRecorder = new ReplayRecorder(); // 周：回放用坐标，我做的回放用到的是这个，起名有点像，要注意

    // 储存棋盘上所有的棋子，要实现吃子的话，直接通过pieces.remove(被吃掉的棋子)删除就可以
    private final List<AbstractPiece> pieces;
    private static final int ROWS = 10;
    private static final int COLS = 9;






    public enum GameState{
        ONGOING,
        RED_WIN,
        BLACK_WIN,
        DRAW
    }
    private GameState gameState = GameState.ONGOING;
    private boolean redTurn = true;

    private boolean opponentInCheck;//


    public ChessBoardModel() {
        pieces = new ArrayList<>();
        initializePieces();
    }

    private void initializePieces() {
        // 黑方棋子
        pieces.add(new GeneralPiece("將", 0, 4, false));
        pieces.add(new SoldierPiece("卒", 3, 0, false));
        pieces.add(new SoldierPiece("卒", 3, 2, false));
        pieces.add(new SoldierPiece("卒", 3, 4, false));
        pieces.add(new SoldierPiece("卒", 3, 6, false));
        pieces.add(new SoldierPiece("卒", 3, 8, false));
        pieces.add(new HorsePiece("马", 0, 7, false));
        pieces.add(new HorsePiece("马", 0, 1, false));
        pieces.add(new ElephantPiece("象", 0, 6, false));
        pieces.add(new ElephantPiece("象", 0, 2, false));
        pieces.add(new AdvisorPiece("仕", 0, 3, false));
        pieces.add(new AdvisorPiece("仕", 0, 5, false));
        pieces.add(new ChariotPiece("车", 0, 0, false));
        pieces.add(new ChariotPiece("车", 0, 8, false));
        pieces.add(new CannonPiece("炮", 2, 7, false));
        pieces.add(new CannonPiece("炮", 2, 1, false));


        // 红方棋子
        pieces.add(new GeneralPiece("帅", 9, 4, true));
        pieces.add(new SoldierPiece("兵", 6, 0, true));
        pieces.add(new SoldierPiece("兵", 6, 2, true));
        pieces.add(new SoldierPiece("兵", 6, 4, true));
        pieces.add(new SoldierPiece("兵", 6, 6, true));
        pieces.add(new SoldierPiece("兵", 6, 8, true));
        pieces.add(new HorsePiece("马", 9, 7, true));
        pieces.add(new HorsePiece("马", 9, 1, true));
        pieces.add(new ElephantPiece("象", 9, 6, true));
        pieces.add(new ElephantPiece("象", 9, 2, true));
        pieces.add(new AdvisorPiece("仕", 9, 3, true));
        pieces.add(new AdvisorPiece("仕", 9, 5, true));
        pieces.add(new ChariotPiece("车", 9, 0, true));
        pieces.add(new ChariotPiece("车", 9, 8, true));
        pieces.add(new CannonPiece("炮", 7, 1, true));
        pieces.add(new CannonPiece("炮", 7, 7, true));
    }

    public List<AbstractPiece> getPieces() {
        return pieces;
    }

    public AbstractPiece getPieceAt(int row, int col) {
        for (AbstractPiece piece : pieces) {
            if (piece.getRow() == row && piece.getCol() == col) {
                return piece;
            }
        }
        return null;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public boolean isRedTurn(){
        return redTurn;
    }
    public boolean showIncheck() {
        return opponentInCheck;
    }

    private void switchTurn(){
        redTurn = !redTurn;
    }

    public boolean movePiece(AbstractPiece piece, int newRow, int newCol) {
        int oldRow = piece.getRow();
        int oldCol = piece.getCol();//这两行我用来记录回放时候用的旧坐标

        //检查游戏状态
        if (gameState != GameState.ONGOING){
            return false;
        }

        //只能移动到合法的位置
        if (!isValidPosition(newRow, newCol)) {
            return false;
        }
        //符合棋子的移动规则
        if (!piece.canMoveTo(newRow, newCol, this)) {
            return false;
        }

        //如果目标位置有棋子，先实现吃子
        AbstractPiece target = getPieceAt(newRow, newCol);
        if (target != null){
            if (target.isRed() == piece.isRed()){
                return false;
            }
            //如果吃掉对方的将，则赢
            if (target instanceof GeneralPiece){
                pieces.remove(target);
                if (target.isRed()){gameState = GameState.BLACK_WIN;}
                else {gameState = GameState.RED_WIN;}
            }
            pieces.remove(target);
        }

        //周：以下是为了加入回放功能
        moveRecorder.addRecord(piece, oldRow, oldCol, newRow, newCol);//周：棋局上的显示，中文棋谱
        replayRecorder.addRecord(piece, oldRow, oldCol, newRow, newCol);//周：回放中间要用的，数字坐标棋谱

        // 真正移动棋子
        piece.moveTo(newRow, newCol);

        // 更新状态
        opponentInCheck = isInCheck(!piece.isRed());
        switchTurn();
        return true;
    }







        //6、检查是否将军,检查是否对该移动棋子的将军


        //7、改变回合



    public MoveRecorder getMoveRecorder() {//周：当时因为取名重复了，这里给你改了一下方法的名字，你原来是getRecorder，返回的是recoder
        return moveRecorder;
    }

    public ReplayRecorder getReplayRecorder() {//周：这个方法用于回放（.moves 文件）的保存与读取
        return replayRecorder;
    }


    public static int getRows() {
        return ROWS;
    }

    public static int getCols() {
        return COLS;
    }


    //加入重启游戏的机制
    public void resetGame(){
        pieces.clear();
        initializePieces();
        redTurn = true;
        gameState = GameState.ONGOING;
    }

    //加入显示游戏状态的函数
    public GameState showGameState(){
        return gameState;
    }


    //找到红方的将军
    private AbstractPiece findGeneral(boolean red){
        for (AbstractPiece p: pieces){
            if (p instanceof GeneralPiece && p.isRed() == red){
                //传入true， 找红方将军；  传入false，找黑方将军。
                return p;
            }
        }
        return null;
    }
    //加入判断将军的机制
    public boolean isInCheck(boolean red){ //红方棋子移动会检查会传入false
        AbstractPiece general = findGeneral(red);
        if (general == null){return false;}
        int gr = general.getRow();
        int gc = general.getCol();

        for (AbstractPiece piece : pieces){
            if (piece.isRed() == red) {continue;}
            if (piece.canMoveTo(gr,gc,this)){
                return true;
            }
        }
        return false;
    }





    //周：下面是我的存档部分
    public void clearPieces(){
        pieces.clear();
    }

    public void addPiece(AbstractPiece p){
        pieces.add(p);
    }

    public void setRedTurn(boolean redTurn){
        this.redTurn = redTurn;
    }

//周：下面一段是做存档文件是否损坏判断的
    public boolean loadFromFile(File file){//周：传入的具体存档

        try{
            java.util.Scanner in = new java.util.Scanner(file, "UTF-8");//周：用来一行一行读取文件内容，UTF-8 → 用来兼容中文棋子名（帅、炮、象）

            // --- 先检查 TIME 行 ---
            if(!in.hasNextLine())//周：有没有下一行？如果没有 = 文件为空 = 损坏
            {
                return false;
            }
            String line = in.nextLine();//周：读取整行
            if(!line.startsWith("TIME,"))//周：必须以 "TIME," 开头
            {
                return false;
            }

            // --- 检查 TURN 行 ---
            if(!in.hasNextLine())
            {
                return false;
            }
            String turnLine = in.nextLine();
            if(!turnLine.startsWith("TURN,"))
            {
                return false;
            }

            boolean redTurn = turnLine.contains("RED");
            this.setRedTurn(redTurn);

            // --- 清空棋子 ---
            this.clearPieces();//周：把之前的棋局全部删掉，准备加载新的数据。

            // --- 解析棋子 ---
            while(in.hasNextLine())
            {
                line = in.nextLine();
                String[] arr = line.split(",");//周：分割数据，碰到逗号就分。

                if(arr.length != 4)
                {
                    return false;
                }

                String name = arr[0];
                String color = arr[1];

                int row, col;

                try{
                    row = Integer.parseInt(arr[2]);
                    col = Integer.parseInt(arr[3]);
                }catch(Exception e){
                    return false;   //周： 坐标不是数字 → 损坏
                }

                boolean isRed = color.equals("RED");

                AbstractPiece p = createPieceByName(name, row, col, isRed);
                if(p == null)
                {
                    return false;  //周：棋子名未知 → 损坏，比如把帅改成周华丽。
                }

                this.addPiece(p);//周：加入棋子到棋盘
            }

            return true; // 周：读取成功

        }catch(Exception e){
            return false; // 周：文件不存在/读不了 → 损坏
        }
    }

    // ★★★★★ 根据棋子名称创建正确的棋子对象（读档专用）★★★★★
    private AbstractPiece createPieceByName(String name, int row, int col, boolean isRed){

        switch(name){
            case "将":
//            case "將":
            case "帅":
                return new GeneralPiece(name, row, col, isRed);

            case "卒":
            case "兵":
                return new SoldierPiece(name, row, col, isRed);

            case "马":
                return new HorsePiece(name, row, col, isRed);

            case "象":
                return new ElephantPiece(name, row, col, isRed);

            case "仕":
                return new AdvisorPiece(name, row, col, isRed);

            case "车":
                return new ChariotPiece(name, row, col, isRed);

            case "炮":
                return new CannonPiece(name, row, col, isRed);

            default:
                System.out.println("未知棋子名称：" + name);
                return null;
        }
    }
    public void forceMove(AbstractPiece p, int newRow, int newCol){
        p.moveTo(newRow, newCol);   // 周：在回放中跳过所有规则，只修改棋子坐标，因为有上一步的选项，不符合规则
    }



}