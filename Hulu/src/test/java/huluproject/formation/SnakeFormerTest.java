package huluproject.formation;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SnakeFormerTest
{
    @Test(timeout = 10)
    public void testFormer() throws Exception
    {
        for (int i=0;i<10;i++)
            SnakeFormer.Form(new ArrayList(10),100,100);
        System.out.println("蛇形阵测试完毕");
    }
}