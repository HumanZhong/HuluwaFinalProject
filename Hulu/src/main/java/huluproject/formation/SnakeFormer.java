package huluproject.formation;

import huluproject.RunnableCreatrue;

import java.util.ArrayList;

public class SnakeFormer
{
    public SnakeFormer()
    {

    }
    public static int Form(ArrayList list,int x,int y)
    {
        if (list.size()==0)
        {
            return -1;
        }
        int exp=0;
        for (int i=0;i<list.size();i++)
        {
            RunnableCreatrue c=(RunnableCreatrue)list.get(i);
            c.setX(x+i*80);
            c.setY(y+exp*100);
            exp=(exp+1)%2;
        }
        return 1;
    }
}
