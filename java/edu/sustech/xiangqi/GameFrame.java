package edu.sustech.xiangqi;
import java.io.File;

import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.ui.*;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;


public class GameFrame extends JFrame {
    //游戏框

    private ChessBoardModel model;//周：象棋数据（棋子、规则、吃子、回合、记录等）
    private ChessBoardPanel boardPanel;//棋盘界面（画线、画棋子、点击鼠标移动棋子
    private BackgroundPanel bgpanel;//周：背景图层（UI 背景、按钮）

    private JButton restartButton; //1、加入重启的按钮
    private JLabel turnLabel; //2、加入显示回合下棋的标签
    private JLabel timerLabel; //3、加入计时器
    private JLabel statusLabel;//4、状态板（显示将军或者游戏结束）
    private MoveTagColumn redTags;
    private MoveTagColumn blackTags;
    private javax.swing.Timer switchTimer;
    private CardButton redCard;
    private CardButton blackCard;




    private javax.swing.Timer turnTimer;
    private long turnStartMillis;


    private String username;
    private boolean isGuest;

    public GameFrame(String username, boolean isGuest)  {


        super("中国象棋");
        this.username = username;  // 保存用户名
        this.isGuest = isGuest;    // 保存是否游客
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        turnTimer = new javax.swing.Timer(1000, e-> updateTimerLabel());

        bgpanel = new BackgroundPanel();
        bgpanel.setLayout(null);

        //加入棋盘
        model = new ChessBoardModel();
        boardPanel = new ChessBoardPanel(model, this);
        boardPanel.setOpaque(false);

        Dimension boardSize = boardPanel.getPreferredSize();
        boardPanel.setBounds(400,30, boardSize.width, boardSize.height);
        bgpanel.add(boardPanel);

        JButton saveMenuButton = new JButton("存档");
        saveMenuButton.setBounds(1100, 380, 120, 50);
        saveMenuButton.setFont(new Font("楷体", Font.BOLD, 22));
        bgpanel.add(saveMenuButton);

        saveMenuButton.addActionListener(e -> {
            openSaveMenuFrame(this, model,username, isGuest);// currentUser改为username
        });


        JButton loadButton = new JButton("读档");
        loadButton.setBounds(1100, 450, 120, 50);
        loadButton.setFont(new Font("楷体", Font.BOLD, 22));
        bgpanel.add(loadButton);

        loadButton.addActionListener(e -> {
            openLoadMenuFrame(this, model, username, isGuest);
        });



        JButton replayBtn = new JButton("回放");
        replayBtn.setBounds(1100, 520, 120, 50);
        replayBtn.setFont(new Font("楷体", Font.BOLD, 22));
        bgpanel.add(replayBtn);



        replayBtn.addActionListener(e -> {
            openReplayMenuFrame(this, username);
        });
            // 打开回放窗口







        //2、重启按钮
        restartButton = new RoundButton("重新开始");
        restartButton.setBounds(1100,50,200,50);
        restartButton.setFont(new Font("楷体", Font.BOLD, 26 ));
        restartButton.addActionListener(e -> restartGame());
//        bgpanel.add(restartButton);

        //3、回合显示标签
        turnLabel = new JLabel();
        turnLabel.setBounds(1100,150,200,30);
        turnLabel.setFont(new Font("楷体", Font.BOLD, 26));
        turnLabel.setForeground(new Color(255, 230, 180)); // 暖色字体
//        bgpanel.add(turnLabel);
        //

        //4、计时器显示标签
        timerLabel = new JLabel();
        timerLabel.setBounds(1100,200,200,30);
        timerLabel.setFont(new Font("楷体", Font.BOLD, 26));
        timerLabel.setForeground(new Color(200, 220, 255)); // 冷色字体
//        bgpanel.add(timerLabel);

        //5、status标签，指示游戏是否结束，将军时出现check,
        //将死时出现game over
        statusLabel = new JLabel();
        statusLabel.setBounds(450,200,200,30);
        statusLabel.setFont(new Font("楷体", Font.BOLD, 28));
        statusLabel.setForeground(new Color(255, 230, 180)); // 暖色字体
//        bgpanel.add(statusLabel);

        // 6、加入两个“最近三步”棋谱标签列
        redTags = new MoveTagColumn();
        redTags.setBounds(50, 700, 140, 120);   // 位置和大小你自己调
        bgpanel.add(redTags);
        redTags.setActive(true);

        blackTags = new MoveTagColumn();
        blackTags.setBounds(1300, 130, 140, 120);
        bgpanel.add(blackTags);
        blackTags.setActive(false);

        //7、加入圆角卡牌
        blackCard = new CardButton("");
//                "<html><div style='text-align:center;'>"
//                        + "<span style='font-size:20px;color:#000;'>暂停</span><br/>"
//                        + "<span style='font-size:14px;color:#555;'>打开菜单</span>"
//                        + "</div></html>"
        blackCard.setBounds(1150, 100, 140, 180); // 自己调位置
//        Image img = new ImageIcon("src/main/resources/1001/4.jpg").getImage();


        Image img  = new ImageIcon("src/main/resources/1001/4.jpg").getImage();
        blackCard.setBackgroundImage(img);
        blackCard.setActive(false);
//        pauseCard.addActionListener(e -> showPauseMenu());  // 点击事件
        bgpanel.add(blackCard);

        redCard = new CardButton("");
//                "<html><div style='text-align:center;'>"
//                        + "<span style='font-size:20px;color:#000;'>暂停</span><br/>"
//                        + "<span style='font-size:14px;color:#555;'>打开菜单</span>"
//                        + "</div></html>");
        redCard.setActive(true);
        redCard.setBounds(200, 670, 140, 180); // 自己调位置
//        Image img2 = new ImageIcon("src/main/resources/1001/17.jpg").getImage();

        Image img2 = new ImageIcon("src/main/resources/1001/17.jpg").getImage();
        redCard.setBackgroundImage(img2);
        blackCard.addActionListener(e -> showPauseMenu());  // 点击事件
        bgpanel.add(redCard);


        setContentPane(bgpanel);
        setSize(1500, 950);
        setLocationRelativeTo(null);
        updateTurnLabel();
        startTurnTimer();
        setVisible(true);
    }

    private PauseOverlay pauseOverlay;
    private boolean paused = false;

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private void showPauseMenu() {
        if (pauseOverlay == null) {
            pauseOverlay = new PauseOverlay(
                    this::hidePauseMenu,
                    () -> {hidePauseMenu(); restartGame();},
                    this::backtoMainMenu);
//                    () -> boardPanel.undo());
        };

        setPaused(true);

        JRootPane root = getRootPane();
        root.setGlassPane(pauseOverlay);
        pauseOverlay.setVisible(true);
        pauseOverlay.requestFocusInWindow();
    }

    private void hidePauseMenu() {
        setPaused(false);
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(false);
        }
    }
    private void backtoMainMenu(){
        //todo:回到主界面
    }


    private void updateLastThreeTags() {
        List<String> all = model.getMoveRecorder().getRecords();

        if (all.isEmpty()) return;

        // 最后一条就是刚刚走的那一步
        String lastMove = all.get(all.size() - 1);
        int index = all.size();

        // 谁刚刚走完？
        boolean lastMoveRed = !model.isRedTurn(); // 因为 movePiece 之后轮到对方

        if (lastMoveRed) {
            redTags.addMove(index+" "+lastMove);
        } else {
            blackTags.addMove(index+" "+lastMove);
        }

        // 设置哪一方是“当前方”（不透明）
        redTags.setActive(lastMoveRed);
        redCard.setActive(lastMoveRed);
        blackTags.setActive(!lastMoveRed);
        blackCard.setActive(!lastMoveRed);
        scheduleSwitchActiveAfterDelay();

    }

    private void scheduleSwitchActiveAfterDelay() {
        // 如果上一次的计时还没跑完，先停掉
        if (switchTimer != null && switchTimer.isRunning()) {
            switchTimer.stop();
        }

        switchTimer = new javax.swing.Timer(800, e -> {
            boolean redToMove = model.isRedTurn(); // 当前该谁走
            redTags.setActive(redToMove);
            redCard.setActive(redToMove);
            blackTags.setActive(!redToMove);
            blackCard.setActive(!redToMove);
        });
        switchTimer.setRepeats(false); // 只执行一次
        switchTimer.start();
    }



    public void restartGame(){
        model.resetGame();
        boardPanel.repaint();
//        updateTurnLabel();
//        startTurnTimer();
    }

    public void endGame(){
        //显示哪一方获胜
        stopTurnTimer();
//        updatestatusLabel();
    }

    private void updateTurnLabel(){
        if (model.isRedTurn()){
            turnLabel.setForeground(Color.RED);
            turnLabel.setText("当前回合：红方");
        }else {
            turnLabel.setForeground(Color.BLACK);
            turnLabel.setText("当前回合：黑方");
        }
    }

    private void updatestatusLabel(){
        if (model.showGameState() != ChessBoardModel.GameState.ONGOING){statusLabel.setText("游戏结束！");}
        if (model.showIncheck()){statusLabel.setText("将军！");}
    }

    private void startTurnTimer(){
        turnStartMillis = System.currentTimeMillis();
        timerLabel.setText("用时： 0秒");
        turnTimer.restart();
    }

    public void stopTurnTimer(){
        if (model.showGameState() != ChessBoardModel.GameState.ONGOING){
            turnTimer.stop();
        }
    }

    private void updateTimerLabel(){
        long now = System.currentTimeMillis();
        long elapsedSeconds = (now - turnStartMillis) / 1000;
        timerLabel.setText("用时： " + elapsedSeconds + "秒");
    }



    public void onMoveSuccess(){
        updateTurnLabel();
        updatestatusLabel();
        updateLastThreeTags();
        //startTurnTimer;

        ChessBoardModel.GameState state = model.showGameState();

        if (state != ChessBoardModel.GameState.ONGOING){
            endGame();
        }
        //加入红方或者黑方胜利的判断和显示
    }


    //周：存档
    private void openSaveMenuFrame(JFrame parent, ChessBoardModel model, String username, boolean isGuest)
    {

        JFrame saveFrame = new JFrame("选择存档位置");
        saveFrame.setSize(1920, 1180);
        saveFrame.setLayout(null);
        saveFrame.setLocationRelativeTo(parent);//跑到父窗口中间，居中函数
        if(isGuest){
            JOptionPane.showMessageDialog(parent, "游客不能使用存档菜单");
            return;
        }
        ImageIcon rawIcon = new ImageIcon("src/main/resources/档案图片.jpg");//周：图片地址可能要根据实际改
        int w = 1920;
        int h = 1180;
        Image scaledImg = rawIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon bgIcon = new ImageIcon(scaledImg);
        JLabel bg = new JLabel(bgIcon);
        bg.setBounds(0, 0, w, h);
        bg.setLayout(null);
        bg.setPreferredSize(new Dimension(1920, 1180));
        saveFrame.setContentPane(bg);
        saveFrame.pack();  // 自动计算窗口大小，完全适应内容
        saveFrame.setLocationRelativeTo(parent);


        ImageIcon rawBtnIcon = new ImageIcon("src/main/resources/存.png");


        Image scaledBtn = rawBtnIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);//缩放
        ImageIcon btnIcon = new ImageIcon(scaledBtn);

        rawBtnIcon = new ImageIcon("src/main/resources/存.png");
        scaledBtn = rawBtnIcon.getImage().getScaledInstance(60,60, Image.SCALE_SMOOTH);
        btnIcon = new ImageIcon(scaledBtn);
        int[][] pos = {
                {420, 400},
                {1165, 400},
                {420, 630},
                {1165, 630},
                {420, 855},
                {1165, 855}
        };
        for(int i = 1; i <= 6; i++){
            int slot = i;

            int btnX = pos[i-1][0];  // “存”按钮位置
            int btnY = pos[i-1][1];

            //左侧缩略图区域（套在 '空' 字位置）

            int thumbW = 300;
            int thumbH = 180;

            int thumbX = btnX - 199;   // 向左移，使其贴在大框上（缩略图的坐标
            int thumbY = btnY - 80;    // 向上调，完全对齐背景框（缩略图的
            JLabel thumb = new JLabel();
            thumb.setBounds(thumbX, thumbY, thumbW, thumbH);
            bg.add(thumb);

            // 加载缩略图（缩到框大小）
            File imageFile = new File("saves/" + username + "_slot" + slot + ".png");
            if(imageFile.exists())
            {
                ImageIcon raw = new ImageIcon(imageFile.getAbsolutePath());
                Image scaled = raw.getImage().getScaledInstance(thumbW, thumbH, Image.SCALE_SMOOTH);
                thumb.setIcon(new ImageIcon(scaled));
            }

            // 存档按钮仍然在右下角金色圆形区域
            JButton btn = new JButton(btnIcon);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setOpaque(false);
            btn.setBounds(btnX+80, btnY, 120, 120);
            bg.add(btn);
            // 显示用户（放在浅粉区）
            JLabel userLabel = new JLabel("用户：" + username);
            userLabel.setForeground(Color.DARK_GRAY);
            userLabel.setFont(new Font("楷体", Font.PLAIN, 20));
            userLabel.setBounds(btnX + 190, btnY - 50, 300, 30);
            bg.add(userLabel);


// 显示存档时间（读 .sav 第一行）
            File savFile = new File("saves/" + username + "_slot" + slot + ".sav");
            if(savFile.exists()){
                try(java.util.Scanner in = new java.util.Scanner(savFile)){
                    String firstLine = in.nextLine();    // TIME日期时间

                    if(firstLine.startsWith("TIME")){
                        String time = firstLine.split(",")[1];

                        JLabel timeLabel = new JLabel("时间：" + time);
                        timeLabel.setForeground(Color.DARK_GRAY);
                        timeLabel.setFont(new Font("楷体", Font.PLAIN, 20));

                        timeLabel.setBounds(btnX + 190, btnY - 10, 300, 30);
                        bg.add(timeLabel);
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }


            btn.addActionListener(e -> {
                int c = JOptionPane.showConfirmDialog(saveFrame, "是否覆盖存档槽位 " + slot + "？", "确认", JOptionPane.OK_CANCEL_OPTION);
                if(c != JOptionPane.OK_OPTION)//不存
                {
                    return;
                }

                SaveRepo repo = new SaveRepo();
                repo.saveToSlot(model, username, slot);
                saveBoardScreenshot(slot);

                JOptionPane.showMessageDialog(saveFrame, "存档成功！");
                saveFrame.dispose();
            });

        }

        saveFrame.setVisible(true);
        System.out.println("GameFrame 创建成功");

    }
    //周：截图相关
    private void saveBoardScreenshot(int slot)
    {
        try{
            //  截图整个 GameFrame（包括背景
            int w = this.getWidth();
            int h = this.getHeight();

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();

            //把整个窗口画到图片上
            this.paint(g2);
            g2.dispose();

            //保存文件username_slotX.png
            File outFile = new File("saves/" + username + "_slot" + slot + ".png");
            javax.imageio.ImageIO.write(img, "png", outFile);

            System.out.println("完整界面截图已保存：" + outFile.getAbsolutePath());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //周：读档
    private void openLoadMenuFrame(JFrame parent, ChessBoardModel model, String username, boolean isGuest){

        JFrame loadFrame = new JFrame("选择读档位置");
        loadFrame.setSize(1920, 1180);
        loadFrame.setLayout(null);
        loadFrame.setLocationRelativeTo(parent);
        if(isGuest){
            JOptionPane.showMessageDialog(parent, "游客不能使用读档菜单");
            return;
        }

        // 读档背景图（和存档一样）
        ImageIcon rawIcon = new ImageIcon("src/main/resources/档案图片.jpg");
        int w = 1920;
        int h = 1180;
        Image scaledImg = rawIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon bgIcon = new ImageIcon(scaledImg);
        JLabel bg = new JLabel(bgIcon);
        bg.setBounds(0, 0, w, h);
        bg.setLayout(null);
        bg.setPreferredSize(new Dimension(1920, 1180));
        loadFrame.setContentPane(bg);
        loadFrame.pack();
        loadFrame.setLocationRelativeTo(parent);

        //  加载 读 图标
        ImageIcon rawLoadIcon = new ImageIcon("src/main/resources/读.png");
        Image scaledLoadImg = rawLoadIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon loadBtnIcon = new ImageIcon(scaledLoadImg);

        //按钮位置数组与save 的一致
        int[][] pos = {
                {420, 400},
                {1165, 400},
                {420, 630},
                {1165, 630},
                {420, 855},
                {1165, 855}
        };

        for(int i = 1; i <= 6; i++){
            int slot = i;

            int btnX = pos[i-1][0];
            int btnY = pos[i-1][1];

            //  左侧缩略图
            int thumbW = 300;
            int thumbH = 180;
            int thumbX = btnX - 199;
            int thumbY = btnY - 80;

            JLabel thumb = new JLabel();
            thumb.setBounds(thumbX, thumbY, thumbW, thumbH);
            bg.add(thumb);

            File imageFile = new File("saves/" + username + "_slot" + slot + ".png");
            if(imageFile.exists()){
                ImageIcon raw = new ImageIcon(imageFile.getAbsolutePath());
                Image scaled = raw.getImage().getScaledInstance(thumbW, thumbH, Image.SCALE_SMOOTH);
                thumb.setIcon(new ImageIcon(scaled));
            }

            //  用户名标签
            JLabel userLabel = new JLabel("用户：" + username);
            userLabel.setForeground(Color.DARK_GRAY);
            userLabel.setFont(new Font("楷体", Font.PLAIN, 20));
            userLabel.setBounds(btnX + 190, btnY - 50, 300, 30);
            bg.add(userLabel);

            // 时间标签 与存档一致
            File savFile = new File("saves/" + username + "_slot" + slot + ".sav");
            if(savFile.exists()){
                try(java.util.Scanner in = new java.util.Scanner(savFile)){
                    String firstLine = in.nextLine();
                    if(firstLine.startsWith("TIME")){
                        String time = firstLine.split(",")[1];
                        JLabel timeLabel = new JLabel("时间：" + time);
                        timeLabel.setForeground(Color.DARK_GRAY);
                        timeLabel.setFont(new Font("楷体", Font.PLAIN, 20));
                        timeLabel.setBounds(btnX + 190, btnY - 10, 300, 30);
                        bg.add(timeLabel);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            // 右下角“读”按钮替代 “存”
            JButton btn = new JButton(loadBtnIcon);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setOpaque(false);
            btn.setBounds(btnX + 80, btnY, 120, 120);
            bg.add(btn);

            // 按下“读档”按钮的逻辑
            btn.addActionListener(e -> {

                int choice = JOptionPane.showConfirmDialog(
                        loadFrame,
                        "读取前要保存当前进度吗？",
                        "读取确认",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );

                if(choice == JOptionPane.CANCEL_OPTION){
                    return;
                }

                if(choice == JOptionPane.YES_OPTION){
                    loadFrame.dispose();
                    openSaveMenuFrame(parent, model, username, isGuest);
                    return;
                }

                if(choice == JOptionPane.NO_OPTION){
                    boolean ok = loadSlot(model, username, slot);

                    if(ok){
                        loadFrame.dispose();
                        JOptionPane.showMessageDialog(parent, "读档成功！");
                    }

                }

            });
        }

        loadFrame.setVisible(true);
    }

    //周：回放界面
    private void openReplayMenuFrame(JFrame parent, String username)
    {
        JFrame replayFrame = new JFrame("选择回放槽位");
        replayFrame.setSize(1920, 1180);
        replayFrame.setLayout(null);
        replayFrame.setLocationRelativeTo(parent);

        // 背景图
        ImageIcon rawIcon = new ImageIcon("src/main/resources/档案图片.jpg");
        int w = 1920, h = 1180;
        Image scaledImg = rawIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        JLabel bg = new JLabel(new ImageIcon(scaledImg));

        bg.setBounds(0, 0, w, h);
        bg.setLayout(null);
        replayFrame.setContentPane(bg);
        replayFrame.pack();
        replayFrame.setLocationRelativeTo(parent);

        //
        ImageIcon replayIconRaw = new ImageIcon("src/main/resources/回放.png");
        Image replayImgScaled = replayIconRaw.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon replayIcon = new ImageIcon(replayImgScaled);

        // six slot positions
        int[][] pos = {
                {420, 400}, {1165, 400},
                {420, 630}, {1165, 630},
                {420, 855}, {1165, 855}
        };

        for(int i = 1; i <= 6; i++)
        {
            int slot = i;

            int btnX = pos[i-1][0];
            int btnY = pos[i-1][1];

            // 缩略图位置
            int thumbW = 300, thumbH = 180;
            int thumbX = btnX - 199;
            int thumbY = btnY - 80;

            JLabel thumb = new JLabel();
            thumb.setBounds(thumbX, thumbY, thumbW, thumbH);
            bg.add(thumb);

            // 加载缩略图
            File imageFile = new File("saves/" + username + "_slot" + slot + ".png");
            if(imageFile.exists()){
                ImageIcon rawShot = new ImageIcon(imageFile.getAbsolutePath());
                Image scaled = rawShot.getImage().getScaledInstance(thumbW, thumbH, Image.SCALE_SMOOTH);
                thumb.setIcon(new ImageIcon(scaled));
            }

            // 显示时间
            File savFile = new File("saves/" + username + "_slot" + slot + ".sav");
            if(savFile.exists()){
                try(java.util.Scanner in = new java.util.Scanner(savFile)){
                    String firstLine = in.nextLine();
                    if(firstLine.startsWith("TIME")){
                        String time = firstLine.split(",")[1];
                        JLabel timeLabel = new JLabel("时间：" + time);
                        timeLabel.setForeground(Color.DARK_GRAY);
                        timeLabel.setFont(new Font("楷体", Font.PLAIN, 20));
                        timeLabel.setBounds(btnX + 190, btnY - 10, 300, 30);
                        bg.add(timeLabel);
                    }
                }catch(Exception e){}
            }

            // 用户名
            JLabel userLabel = new JLabel("用户：" + username);
            userLabel.setForeground(Color.DARK_GRAY);
            userLabel.setFont(new Font("楷体", Font.PLAIN, 20));
            userLabel.setBounds(btnX + 190, btnY - 50, 300, 30);
            bg.add(userLabel);

            // 右下角回放按钮
            JButton playBtn = new JButton(replayIcon);
            playBtn.setBorderPainted(false);
            playBtn.setContentAreaFilled(false);
            playBtn.setFocusPainted(false);
            playBtn.setOpaque(false);
            playBtn.setBounds(btnX + 80, btnY, 120, 120);
            bg.add(playBtn);

            // 回放按钮逻辑
            playBtn.addActionListener(e -> {

                File movesFile = new File("saves/" + username + "_slot" + slot + ".moves");

                if(!movesFile.exists()){
                    JOptionPane.showMessageDialog(replayFrame,
                            "该槽位还没有回放记录！");
                    return;
                }

                // 打开回放窗口
                new ReplayFrame(movesFile);

                // 关闭菜单
                replayFrame.dispose();
            });
        }

        replayFrame.setVisible(true);
    }

//周：读档检查

    private boolean loadSlot(ChessBoardModel model, String username, int slot){
        File file = new File("saves/" + username + "_slot" + slot + ".sav");

        if(!file.exists()){
            JOptionPane.showMessageDialog(this, "此槽位没有存档！");
            return false;
        }

        boolean ok = model.loadFromFile(file);   //  返回是否成功

        if(!ok){
            JOptionPane.showMessageDialog(this, "存档已损坏，无法读取！");
            return false;
        }

        boardPanel.repaint();
        updateTurnLabel();

        return true;   // ★成功
    }






}