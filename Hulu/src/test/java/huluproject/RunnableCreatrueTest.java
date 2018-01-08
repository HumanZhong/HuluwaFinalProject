package huluproject;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class RunnableCreatrueTest
{
    @Test(timeout = 100)
    public void testmoveTo() throws Exception
    {
        RunnableCreatrue c = new RunnableCreatrue();
        Random random=new Random();
        for (int i = 0; i < 10; i++)
        {
            int deltax=random.nextInt(10);
            int deltay=random.nextInt(10);
            int x=c.getX();
            int y=c.getY();
            c.moveTo(deltax, deltay);
            assertEquals(c.getX(), x+deltax);
            assertEquals(c.getY(), y+deltay);
        }
        System.out.println("Move功能测试完毕");
    }
    public void testDecideNextMove() throws Exception
    {


    }

}