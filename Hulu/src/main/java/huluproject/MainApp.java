package huluproject;

public class MainApp
{
    public static void main(String []args)
    {
        System.out.println("Hello Maven");
        //System.out.println(this.getClass());
        //HuluBoy creature=new HuluBoy();
        //Creature c=new HuluBoy();
        //c.moveTo(0,0);
        //creature.fightWith();

        /*huluproject.Sleeper
                sleepy = new huluproject.Sleeper("Sleepy", 1500),
                grumpy = new huluproject.Sleeper("Grumpy", 1500);
        huluproject.Joiner
                dopey = new huluproject.Joiner("Dopey", sleepy),
                doc = new huluproject.Joiner("Doc", grumpy);
        grumpy.interrupt();*/
    }
}

class Sleeper extends Thread {
    private int duration;

    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }

    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted. " +
                    "isInterrupted(): " + isInterrupted());
            return;
        }
        System.out.println(getName() + " has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;

    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    public void run() {
        try {
            sleeper.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(getName() + " join completed");
    }
}