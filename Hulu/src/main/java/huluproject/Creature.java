package huluproject;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Creature extends Picture2D
{
    int max_hp;
    int current_hp;
    int attack;
    int level=1;
    private boolean dead=false;
    private BattleField field;
    private static Random random=new Random(100);
    private boolean fighting=false;
    //记录正在与当前生物fight的所有生物
    private ArrayList enemys=new ArrayList();

    public boolean isDead(){return dead;}
    public void setDead(boolean dead){this.dead=dead;}
    public boolean isFighting(){return fighting;}
    public void setFighting(boolean fighting){this.fighting=fighting;}
    public int getLevel(){return level;}
    public Creature()
    {

    }
    public Creature(int x,int y,BattleField field,String pic)
    {
        super(x,y);
        this.field=field;
        URL imageurl=this.getClass().getClassLoader().getResource(pic);
        ImageIcon imageIcon=new ImageIcon(imageurl);
        this.setImage(imageIcon.getImage());
    }
    public void moveTo(int delta_x,int delta_y)
    {
        setX(getX()+delta_x);
        setY(getY()+delta_y);
    }
    public void randomMove()
    {
        int target_x=getX()+(random.nextInt(3)-1)*100;
        int target_y=getY()+(random.nextInt(3)-1)*100;
        if (target_x>1100 || target_x<0)
            target_x=getX();
        if (target_y>800 || target_y<0)
            target_y=getY();
        setX(target_x);
        setY(target_y);
    }
    public void fightWith(Creature another)
    {
        int win=random.nextInt(2);
        if (win==0)
            dead=true;
        else if (win==1)
            another.dead=true;
        fighting=true;
        another.fighting=true;
    }
    //public void moveTo(int x,int y);
}
class HuluBoy extends Creature
{
    public HuluBoy(int x,int y,BattleField field,String pic)
    {
        super(x,y,field,pic);
    }
    public boolean isConflictWith(Creature another)
    {
        if (!another.isDead() && another instanceof Monster)
        {
            if (getX()==another.getX() && getY()==another.getY())
                return true;
        }
        return false;
    }
}
class Monster extends Creature
{
    public Monster(int x,int y,BattleField field,String pic)
    {
        super(x,y,field,pic);
    }
}
