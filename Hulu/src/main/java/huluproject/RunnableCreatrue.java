package huluproject;

import huluproject.position.Position;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class RunnableCreatrue extends Picture2D implements Runnable
{
    private static Random random=new Random();
    //private static Timer timer=new Timer();
    boolean dead=false;
    int state=0;//0:move,1:fight,2:recycable
    protected int group=0;//0:good,1:bad
    protected int kind=-1;//-1:unknown,0-6:huluwa,7:snake,8:scorpion
    private BattleField field;
    private Position position;

    private int deltax=0;
    private int deltay=0;

    public int getDeltax(){return deltax;}
    public int getDeltay(){return deltay;}

    public void setGroup(int group)
    {
        this.group=group;
    }

    public int getGroup()
    {
        return group;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }
    public Position getPosition()
    {
        return position;
    }

    public RunnableCreatrue()
    {

    }
    public RunnableCreatrue(int x,int y,BattleField field,String pic)
    {
        super(x,y);
        this.field=field;
        URL imageurl=this.getClass().getClassLoader().getResource(pic+".png");
        URL deadimageurl=this.getClass().getClassLoader().getResource(pic+"_dead.png");
        ImageIcon imageIcon=new ImageIcon(imageurl);
        ImageIcon deadimageIcon=new ImageIcon(deadimageurl);
        this.setImage(imageIcon.getImage());
        this.setDeadimage(deadimageIcon.getImage());
    }

    public void moveTo(int delta_x,int delta_y)
    {
        setX(getX()+delta_x);
        setY(getY()+delta_y);
    }
    public synchronized boolean confictWith(RunnableCreatrue another)
    {
        if (this.group==another.group)
            return false;
        else
        {
            if (Math.abs(this.getX()-another.getX())<100 && Math.abs(this.getY()-another.getY())<100)
                return true;
            return false;
        }
    }
    public synchronized void fightWith(RunnableCreatrue another)
    {
        if (random.nextInt(2)==0)
            this.dead=true;
        else
            another.dead=true;
    }
    public synchronized void decideNextMove()
    {
        if (this.group==0)//goodgroup
        {

            ArrayList tempbad=field.getBadGroup();
            if (tempbad.size()==0)
                return;
            int minx=900;
            int targetindex=0;
            for (int i=0;i<tempbad.size();i++)
            {
                RunnableCreatrue c=(RunnableCreatrue) tempbad.get(i);
                if (c.dead)
                    continue;
                if (confictWith(c))//检测到碰撞
                {
                    fightWith(c);
                }
                if (c.getX()<minx)
                {
                    minx=c.getX();
                    targetindex=i;
                }
            }
            RunnableCreatrue c=(RunnableCreatrue)tempbad.get(targetindex);
            int targetx=c.getX();
            int targety=c.getY();
            double k=(double)(targety-getY())/(double)(targetx-getX());
            if (targetx>getX())
            {
                if (k>0) {deltax=10;deltay=5;}
                else if (k<0) {deltax=10;deltay=-5;}
            }
            else
            {
                deltax=10;
                deltay=0;
            }
        }
        else
        {
            ArrayList tempgood=field.getGoodGroup();
            if (tempgood.size()==0)
                return;
            int maxx=0;
            int targetindex=0;
            for (int i=0;i<tempgood.size();i++)
            {
                RunnableCreatrue c=(RunnableCreatrue) tempgood.get(i);
                if (c.dead)
                    continue;
                if (c.getX()>maxx)
                {
                    maxx=c.getX();
                    targetindex=i;
                }
            }
            RunnableCreatrue c=(RunnableCreatrue)tempgood.get(targetindex);
            int targetx=c.getX();
            int targety=c.getY();
            double k=(double)(targety-getY())/(double)(targetx-getX());
            if (targetx<getX())
            {
                if (k>0) {deltax=-10;deltay=-5;}
                else if (k<0) {deltax=-10;deltay=5;}
            }
            else
            {
                deltax=-10;
                deltay=0;
            }
        }
    }
    public void run()
    {
        while (true)
        {
            if (field.state==BattleFieldState.END || field.state==BattleFieldState.PREPARE)
                break;
            if (dead)
                break;
            decideNextMove();
            moveTo(deltax,deltay);
            try
            {
                TimeUnit.MILLISECONDS.sleep(100+random.nextInt(200));
            } catch (Exception ex)
            {

            }
        }
        try
        {
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        catch (Exception ex)
        {

        }
        finally
        {
            state=2;//标记为可回收
        }
    }
}


class Huluwa extends RunnableCreatrue
{
    private static int count=0;
    private int no=0;
    public Huluwa(int x,int y,BattleField field,String pic,int no)
    {
        super(x, y, field, pic);
        this.no=no;
        group=0;
        kind=no;
        count++;
    }
}
class Snake extends RunnableCreatrue
{
    private static int count=0;
    public Snake(int x,int y,BattleField field,String pic)
    {
        super(x, y, field, pic);
        group=1;
        kind=7;//snake
        count++;
    }
}
class Scorpion extends RunnableCreatrue
{
    private static int count=0;
    public Scorpion(int x,int y,BattleField field,String pic)
    {
        super(x, y, field, pic);
        group=1;
        kind=8;//scorpion
        count++;
    }
}