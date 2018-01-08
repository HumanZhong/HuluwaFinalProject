import javax.swing.*;
import huluproject.*;
public class GUITest extends JFrame
{
    public GUITest()
    {
        InitAll();
    }
    public void InitAll()
    {
        BattleField field=new BattleField();
        add(field);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(field.getFieldWidth()+20,field.getFieldHeight()+40);
        setLocationRelativeTo(null);
        setTitle("Test");
    }
    public static void main(String []args)
    {
        GUITest g=new GUITest();
        g.setVisible(true);
    }
}
