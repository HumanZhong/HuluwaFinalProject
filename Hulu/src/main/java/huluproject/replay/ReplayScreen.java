package huluproject.replay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ReplayScreen
{
    ArrayList<ReplayObject> objects=new ArrayList<>();
    public ReplayScreen()
    {

    }
    public void addReplayObject(int kind,boolean dead,int x,int y)
    {
        objects.add(new ReplayObject(kind, dead, x, y));
    }

    public ArrayList<ReplayObject> getObjects()
    {
        return objects;
    }

    public void writeTo(DataOutputStream out)
    {
        try
        {
            out.writeInt(objects.size());
            for (int i=0;i<objects.size();i++)
            {
                out.writeInt(objects.get(i).kind);
                out.writeBoolean(objects.get(i).dead);
                out.writeInt(objects.get(i).x);
                out.writeInt(objects.get(i).y);
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error in replay write");
        }
    }

    public void read(DataInputStream in)
    {
        try
        {
            int size;
            size=in.readInt();
            //System.out.println();
            for (int i=0;i<size;i++)
            {
                addReplayObject(in.readInt(),in.readBoolean(),in.readInt(),in.readInt());
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error in replay read");
        }
    }
}
