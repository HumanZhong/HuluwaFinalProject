package huluproject.replay;

public class ReplayObject
{
    int kind=-1;//0-6:huluwa,7:snake,8:scorpion
    boolean dead;
    int x,y;
    public ReplayObject(int kind,boolean dead,int x,int y)
    {
        this.kind=kind;
        this.dead=dead;
        this.x=x;
        this.y=y;
    }
    public int getKind(){return kind;}
    public boolean getDead(){return dead;}
    public int getX(){return x;}
    public int getY(){return y;}
}
