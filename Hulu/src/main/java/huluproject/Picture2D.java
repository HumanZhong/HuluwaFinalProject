package huluproject;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Picture2D
{
    private int x=0,y=0;
    private Image image;
    private Image deadimage;
    public Picture2D()
    {

    }
    public Picture2D(String imageName)
    {
        URL loc = this.getClass().getClassLoader().getResource(imageName);
        ImageIcon iia = new ImageIcon(loc);
        this.image = iia.getImage();
    }
    public Picture2D(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    public Image getImage()
    {
        return image;
    }
    public Image getDeadimage() {return deadimage;}
    public void setImage(Image image)
    {
        this.image=image;
    }
    public void setDeadimage(Image image){this.deadimage=image;}
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public void setX(int x)
    {
        this.x=x;
    }
    public void setY(int y)
    {
        this.y=y;
    }
}
