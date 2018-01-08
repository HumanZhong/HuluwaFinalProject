# 葫芦娃FinalProject

### 151220174 钟虎门
---
## 内容介绍
### (1)开始界面

打开程序后，进入开始界面，右上角将会提示
> 'SPACE'开始游戏   'L'装载记录 

### (2)战斗界面
+ 战斗界面一开始由7个葫芦娃和8只妖精组成，葫芦娃在左侧摆成蛇阵（Snake），妖精在右侧摆成锋矢阵（Arrow），每个生物将朝着距离自己最近的敌方生物移动，当二者之间的距离小于某个数（此处为100）时，双方发生战斗，由各自生物中的`private Random random`生成一个随机数并通过随机数的取值判断哪一个生物取胜。
+ 死亡的生物将会在屏幕上显示尸体一段时间，之后会被生物回收线程回收并销毁。
+ 当所有葫芦娃或所有妖精死亡后，根据场上形势宣布其中一方获胜，然后跳转到结束界面

### (3)结束界面
结束界面右上角将会提示
> 'R'重新开始游戏   'S'存储记录

---
## 功能描述
* 战斗记录
每次进入战斗时，Replayer每隔50ms自动抓取一次屏幕信息并存储在Replayer的内存中，战斗结束后按‘S’键可以存储刚才发生的战斗，存储文件名字的形式为
>year-month-day-hour-minute-second.dat
* 战斗回放
在程序的准备界面，按‘L’可以装载之前存储的战斗记录并将自动根据文件中存储的内容回放这次战斗，回放完毕后会自动跳转到准备界面。
* 尸体回收线程
当葫芦娃和妖怪交战完毕后，死亡一方将在屏幕上显示尸体，一段时间后（程序中为1s）尸体将会消失；这些工作将由尸体回收线程完成，核心机制是每个生物体有一个自己的`private int state;//0:moing,1:fighting,2:recycable`，其中当state==2时，判定该生物已死亡且尸体可回收，生物回收线程每隔1000ms对整个BattleField遍历一次，清楚recycable的生物体。

---
## 逻辑结构
+ RunnableCreature
描述葫芦世界中所有可以动的生物体，并实现了Runnable接口(这个后面会讲)，具有所有生物共通的接口例如
> public void moveTo(int delta_x, int delta_y);
> public void decideNextMove();//寻找最近的敌人并决定下一步
+ BattleField
描述葫芦世界中的战斗区域，其中的战斗人员由goodGroup（葫芦娃、爷爷）和badGroup（蝎子精、蛇精）组成。有一个屏幕刷新线程，用于每隔50ms调用`repaint()`重绘一次战斗场景。BattleField的状态分别为

```java
public enum BattleFieldState
{
    PREPARE,RUN,END,REPLAYING//Prepare：准备界面，Run：战斗界面，End：结束界面，Replaying：回放界面
}
```

+ Replayer——回放器
在战斗时每隔50ms自动记录当前屏幕上的所有元素（ReplayObject）存放在内存中。

+ Former——阵型构造器
package former中定义了一系列static的阵型构造器，调用方法如下
`SnakeFormer.form(ArrayList list,int x,int y)//将ArrayList中的东西以（x，y）为顶点摆成蛇形阵`

+ 其余具体代码请看源码 

---
## 线程代码

+ RunnableCreature
```java
@Override
public void run()
    {
        while (true)
        {
            if (field.state==BattleFieldState.END || field.state==BattleFieldState.PREPARE)
                break;
            if (dead)
                break;
            decideNextMove();//决定下一步位置
            moveTo(deltax,deltay);//移动到所决定的位置处
            try
            {
                TimeUnit.MILLISECONDS.sleep(100+random.nextInt(200));
            } catch (Exception ex)
            {

            }
        }
        try
        {
            TimeUnit.MILLISECONDS.sleep(1000);//尸体显示1000ms后标记为可回收
        }
        catch (Exception ex)
        {

        }
        finally
        {
            state=2;//标记为可回收
        }
    }
```
---
## 线程代码
+ 生物回收线程
```java
executorService.execute(()->{//生物回收线程
            while (isBattleExists())
            {
                for (int i=0;i<goodGroup.size();i++)
                {
                    RunnableCreatrue c=(RunnableCreatrue)goodGroup.get(i);
                    if (c.state==2)//已死亡且可回收
                    {
                        goodGroup.remove(i);
                        i--;
                    }
                    else if (c.getX()>1100 || c.getX()<0 || c.getY()<0 || c.getY()>800)//越界回收
                    {
                        goodGroup.remove(i);
                        i--;
                    }
                }
                for (int i=0;i<badGroup.size();i++)
                {
                    RunnableCreatrue c=(RunnableCreatrue)badGroup.get(i);
                    if (c.state==2)//已死亡且可回收
                    {
                        badGroup.remove(i);
                        i--;
                    }
                    else if (c.getX()>1100 || c.getX()<0 || c.getY()<0 || c.getY()>800)
                    {
                        badGroup.remove(i);
                        i--;
                    }
                }
                repaint();
                try
                {
                    TimeUnit.MILLISECONDS.sleep(500);
                }
                catch (Exception ex)
                {

                }
            }
        });
```
---
## 线程代码
+ 屏幕刷新线程
```java
executorService.execute(()->{//屏幕刷新线程//++++++++++++++++++++++尝试将屏幕刷新线程设置为高Priority
            while (isBattleExists())
            {
                repaint();
                try
                {
                    TimeUnit.MILLISECONDS.sleep(50);
                }
                catch (Exception ex)
                {
                    System.out.println("error in refresh thread");
                }
                //state=BattleFieldState.END;
                repaint();
            }
            state=BattleFieldState.END;
            repaint();
        });
```

---
## 遇到的问题
+ 这次葫芦娃本来最开始打算用定时器刷新屏幕以及移动生物体，后来发现Timer的每次触发都会消耗较多的时间进行每个生物体下一步的运算，造成画面偶尔卡顿。
+ 同时，使用Timer也带来另外一个问题，那就是每一个时间段后，每个生物体同步移动，大大减少了游戏的随机性。
+ 如果使用每个生物体一个线程，然后让他们自主运行，将极大的提升游戏的随机性，而decideNextMove的算法又决定了他们运动的大体趋势，这样使得生物体们不至于乱动而又保留了随机的乐趣。

---
## 总结体会
+ 这个葫芦娃小程序带来的最重要的一点体会就是
> ‘拖延症’真的是太可怕了，但是还好deadline比起来要更可怕一些，虽然程序不算尽善尽美，但好歹还是在ddl之前将我所能够想到的点子都揉进去了...
+ 希望以后在学习过程中能够克服万恶的拖延症吧（虽然希望不大）...毕竟今天写完了所有东西还要拖到明天交...



