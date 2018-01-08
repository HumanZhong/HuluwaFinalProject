package huluproject;

import huluproject.formation.ArrowFormer;
import huluproject.formation.SnakeFormer;
import huluproject.position.Position;
import huluproject.replay.ReplayObject;
import huluproject.replay.ReplayScreen;
import huluproject.replay.Replayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.CharArrayReader;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BattleField extends JPanel
{
    private final int GRIDWIDTH=100;
    private final int GRIDHEIGHT=100;
    private int width=1200;
    private int height=900;
    private static Random random=new Random();

    BattleFieldState state=BattleFieldState.PREPARE;
    boolean result=false;//true:huluwa win,false:monster win

    private ArrayList testlist=new ArrayList();
    private ArrayList badGroup=new ArrayList();
    private ArrayList goodGroup=new ArrayList();

    ExecutorService executorService= Executors.newCachedThreadPool();

    Replayer replayer=new Replayer(false);
    ReplayScreen currentScreen=new ReplayScreen();
    /*Picture2D pichlw0=new Picture2D("hlw0.png");
    Picture2D pichlw1=new Picture2D("hlw1.png");
    Picture2D pichlw2=new Picture2D("hlw2.png");
    Picture2D pichlw3=new Picture2D("hlw3.png");
    Picture2D pichlw4=new Picture2D("hlw4.png");
    Picture2D pichlw5=new Picture2D("hlw5.png");
    Picture2D pichlw6=new Picture2D("hlw6.png");*/
    Picture2D[] picHlw=new Picture2D[7];
    Picture2D[] picHlwDead=new Picture2D[7];
    Picture2D picSnake=new Picture2D("snake.png");
    Picture2D picSnakeDead=new Picture2D("snake_dead.png");
    Picture2D picScorpion=new Picture2D("scorpion.png");
    Picture2D picScorpionDead=new Picture2D("scorpion_dead.png");

    public int getFieldWidth(){return width;}
    public int getFieldHeight(){return height;}

    public ArrayList getGoodGroup()
    {
        return goodGroup;
    }
    public ArrayList getBadGroup()
    {
        return badGroup;
    }

    public boolean isBattleExists()
    {
        if (goodGroup.size()==0 || badGroup.size()==0)
        {
            state=BattleFieldState.END;
            return false;
        }
        else
            return true;
    }

    public BattleField()
    {
        addKeyListener(new KAdapter());
        setFocusable(true);
        initPicture();
        initBattleField();
    }
    public void initPicture()
    {
        for (int i=0;i<7;i++)
        {
            picHlw[i] = new Picture2D("hlw" + i + ".png");
            picHlwDead[i]=new Picture2D("hlw"+i+"_dead.png");
        }
    }
    public void initBattleField()
    {
        state=BattleFieldState.PREPARE;
    }
    public void startBattle()
    {
        state=BattleFieldState.RUN;
        executorService.shutdownNow();
        executorService=Executors.newCachedThreadPool();
        for (int i=0;i<7;i++)
        {
            String pic="hlw"+i;
            Huluwa huluwa=new Huluwa(100,(i+1)*100,this,pic,i);
            goodGroup.add(huluwa);
            executorService.execute(new Thread(huluwa));
        }
        SnakeFormer.Form(goodGroup,0,400);
        for (int i=0;i<8;i++)
        {
            if (i<4)
            {
                Snake snake = new Snake(700, (i + 1) * 100, this, "snake");
                badGroup.add(snake);
                executorService.execute(new Thread(snake));
            }
            else
            {
                Scorpion scorpion=new Scorpion(700,(i+1)*100,this,"scorpion");
                badGroup.add(scorpion);
                executorService.execute(new Thread(scorpion));
            }
        }
        ArrowFormer.Form(badGroup,700,400);
        /*executorService.execute(()->{//生物生成线程
            while (isBattleExists())
            {
                if (random.nextInt(50) < 25)//生成葫芦娃
                {
                    int n = random.nextInt(7);
                    //String pic="hlw"+new Integer(n).toString();
                    Huluwa huluwa = new Huluwa(0, n * 50 + 300, this, "hlw0", n);
                    goodGroup.add(huluwa);
                    executorService.execute(new Thread(huluwa));
                } else
                {
                    Snake snake = new Snake(1100, random.nextInt(450) + 300, this, "snake");
                    badGroup.add(snake);
                    executorService.execute(snake);
                }
                try
                {
                    TimeUnit.SECONDS.sleep(3);
                }
                catch (Exception ex)
                {

                }
            }
        });*/
        executorService.execute(()->{//屏幕刷新线程//++++++++++++++++++++++尝试将屏幕刷新线程设置为高Priority
            while (isBattleExists())
            {
                repaint();
                try
                {
                    TimeUnit.MILLISECONDS.sleep(50);
                }
                catch (Exception ex)
                {
                    System.out.println("error in refresh thread");
                }
                //state=BattleFieldState.END;
                repaint();
            }
            state=BattleFieldState.END;
            repaint();
        });
        executorService.execute(()->{//死亡生物回收线程
            while (isBattleExists())
            {
                for (int i=0;i<goodGroup.size();i++)
                {
                    RunnableCreatrue c=(RunnableCreatrue)goodGroup.get(i);
                    if (c.state==2)//已死亡且可回收
                    {
                        goodGroup.remove(i);
                        i--;
                    }
                    else if (c.getX()>1100 || c.getX()<0 || c.getY()<0 || c.getY()>800)
                    {
                        goodGroup.remove(i);
                        i--;
                    }
                }
                for (int i=0;i<badGroup.size();i++)
                {
                    RunnableCreatrue c=(RunnableCreatrue)badGroup.get(i);
                    if (c.state==2)//已死亡且可回收
                    {
                        badGroup.remove(i);
                        i--;
                    }
                    else if (c.getX()>1100 || c.getX()<0 || c.getY()<0 || c.getY()>800)
                    {
                        badGroup.remove(i);
                        i--;
                    }
                }
                repaint();
                try
                {
                    TimeUnit.MILLISECONDS.sleep(500);
                }
                catch (Exception ex)
                {

                }
            }
        });

    }
    public void replay()
    {
        state=BattleFieldState.REPLAYING;
        ArrayList<ReplayScreen> screens=replayer.getScreens();
        //Iterator<ReplayScreen> it=screens.iterator();
        executorService.execute(()->{
            for (int i=0;i<screens.size();i++)
            {
                currentScreen=screens.get(i);
                repaint();
                try
                {
                    TimeUnit.MILLISECONDS.sleep(50);
                }
                catch (Exception ex)
                {
                    System.out.println("Error in Replay function");
                }
            }
            state=BattleFieldState.PREPARE;
            repaint();
        });
    }
    public void paintBattleField(Graphics g)
    {
        ReplayScreen screen=new ReplayScreen();

        Picture2D background=new Picture2D("background2.jpg");
        g.drawImage(background.getImage(),0,0,1200,900,this);
        if (state==BattleFieldState.PREPARE)
        {
            Picture2D pic=new Picture2D("start_tip.png");
            g.drawImage(pic.getImage(),700,50,400,80,this);
        }
        if (state==BattleFieldState.END)
        {
            Picture2D pic1=new Picture2D("hlw_win.png");
            Picture2D pic2=new Picture2D("monster_win.png");
            if (goodGroup.size()==0)
                g.drawImage(pic2.getImage(),400,400,300,100,this);
            else
                g.drawImage(pic1.getImage(),400,400,300,100,this);
            Picture2D endtip=new Picture2D("end_tip.png");
            g.drawImage(endtip.getImage(),700,50,400,80,this);
            return;
        }
        if (state==BattleFieldState.REPLAYING)
        {
            for (int i=0;i<currentScreen.getObjects().size();i++)
            {
                ReplayObject obj=currentScreen.getObjects().get(i);
                if (obj.getKind()<7 && obj.getKind()>=0)//huluwa
                {
                    if (!obj.getDead())
                        g.drawImage(picHlw[obj.getKind()].getImage(),obj.getX(),obj.getY(),GRIDWIDTH,GRIDHEIGHT,this);
                    else
                        g.drawImage(picHlwDead[obj.getKind()].getImage(),obj.getX(),obj.getY(),GRIDWIDTH,GRIDHEIGHT,this);
                }
                else if (obj.getKind()==7)//snake
                {
                    if (!obj.getDead())
                        g.drawImage(picSnake.getImage(),obj.getX(),obj.getY(),GRIDWIDTH,GRIDHEIGHT,this);
                    else
                        g.drawImage(picSnakeDead.getImage(),obj.getX(),obj.getY(),GRIDWIDTH,GRIDHEIGHT,this);
                }
                else if (obj.getKind()==8)//scorpion
                {
                    if (!obj.getDead())
                        g.drawImage(picScorpion.getImage(),obj.getX(),obj.getY(),GRIDWIDTH,GRIDHEIGHT,this);
                    else
                        g.drawImage(picScorpionDead.getImage(),obj.getX(),obj.getY(),GRIDWIDTH,GRIDHEIGHT,this);
                }
            }
        }
        ArrayList allthings=new ArrayList();
        allthings.addAll(goodGroup);
        allthings.addAll(badGroup);
        for (int i=0;i<allthings.size();i++)
        {
            RunnableCreatrue item=(RunnableCreatrue) allthings.get(i);
            if (!item.dead)
            {
                g.drawImage(item.getImage(), item.getX(), item.getY(), GRIDWIDTH, GRIDHEIGHT, this);
                if (state==BattleFieldState.RUN)
                    screen.addReplayObject(item.kind,false,item.getX(),item.getY());
            }
            else
            {
                g.drawImage(item.getDeadimage(),item.getX(),item.getY(),GRIDWIDTH,GRIDHEIGHT,this);
                if (state==BattleFieldState.RUN)
                    screen.addReplayObject(item.kind,true,item.getX(),item.getY());
            }
        }
        if (state==BattleFieldState.RUN)
            replayer.addScreen(screen);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        paintBattleField(g);
    }

    /*内部类：键盘消息响应适配器*/
    class KAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int keycode=e.getKeyCode();
            switch (keycode)
            {
                case KeyEvent.VK_SPACE:
                {
                    if (state==BattleFieldState.PREPARE)
                        startBattle();
                }break;
                case KeyEvent.VK_L:
                {
                    if (state==BattleFieldState.PREPARE)
                    {
                        JFileChooser fd = new JFileChooser();
                        fd.showOpenDialog(null);
                        File targetfile=fd.getSelectedFile();
                        replayer.readFromFile(targetfile);
                        replay();
                    }
                }break;
                case KeyEvent.VK_S:
                {
                    if (state==BattleFieldState.END)
                    {
                        replayer.setResult(result);
                        replayer.writeToFile();
                    }
                }break;
                case KeyEvent.VK_R:
                {
                    if (state==BattleFieldState.END)
                        startBattle();
                }break;
            }
            repaint();
        }
    }
}
