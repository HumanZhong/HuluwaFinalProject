package huluproject.replay;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Replayer
{
    File file;
    ArrayList<ReplayScreen> screens;
    int sizeofscreens=0;
    boolean result=false;//true:葫芦娃胜,false:妖精胜
    public Replayer(boolean result)
    {
        this.result=result;
        screens=new ArrayList<>();
    }
    public boolean getResult()
    {
        return result;
    }
    public void setResult(boolean result)
    {
        this.result = result;
    }

    public ArrayList<ReplayScreen> getScreens()
    {
        return screens;
    }

    public void addScreen(ReplayScreen screen)
    {
        screens.add(screen);
    }
    public void writeToFile()
    {
        //long time=System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String filename=df.format(new Date());
        file=new File(filename);
        if (file==null)
        {
            System.out.println("Error in Creating file");
        }
        try
        {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            out.writeBoolean(result);
            out.writeInt(screens.size());
            for (int i=0;i<screens.size();i++)
            {
                screens.get(i).writeTo(out);
            }
            out.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error in Replayer write");
        }
    }
    public void readFromFile(File file)
    {
        this.file = file;
        try
        {
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            result=in.readBoolean();
            sizeofscreens=in.readInt();
            System.out.println("here");
            for (int i=0;i<sizeofscreens;i++)
            {
                ReplayScreen screen=new ReplayScreen();
                //screens.get(i).read(in);
                screen.read(in);
                screens.add(screen);
            }
        }
        catch (Exception ex)
        {
            //System.out.println("Error in Replayer read");
            ex.printStackTrace();
        }
    }
}
