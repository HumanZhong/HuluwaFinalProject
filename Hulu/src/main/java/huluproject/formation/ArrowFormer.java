package huluproject.formation;

import huluproject.RunnableCreatrue;

import java.util.ArrayList;

public class ArrowFormer
{
    public static int Form(ArrayList list,int x,int y)
    {
        if (list.size()==0)
            return -1;
        if (list.size()<4)
            return -1;
        int index=(int)(list.size()-4);
        for (int i=0;i<index;i++)
        {
            RunnableCreatrue c=(RunnableCreatrue)list.get(i);
            c.setX(x+i*80);
            c.setY(y);
        }
        for (int i=0;i<4;i++)
        {
            RunnableCreatrue c1 = (RunnableCreatrue) list.get(index + i);
            RunnableCreatrue c2 = (RunnableCreatrue) list.get(index+i+1);
            c1.setX(x+(i/2+1)*80);
            c1.setY(y+(i/2+1)*80);
            c2.setX(x+(i/2+1)*80);
            c2.setY(y-(i/2+1)*80);
            i++;
        }
        return 1;
    }
}
