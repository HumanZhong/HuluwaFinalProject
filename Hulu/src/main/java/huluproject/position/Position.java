package huluproject.position;

import huluproject.RunnableCreatrue;

public class Position
{
    private int x,y;
    private boolean empty;
    private RunnableCreatrue creatrue;

    public synchronized boolean isEmpty()
    {
        return empty;
    }
    public synchronized void setEmpty(boolean empty)
    {
        this.empty = empty;
    }

    public synchronized void setCreatrue(RunnableCreatrue creatrue)
    {
        this.creatrue = creatrue;
    }
    public synchronized RunnableCreatrue getCreatrue()
    {
        return creatrue;
    }
}
