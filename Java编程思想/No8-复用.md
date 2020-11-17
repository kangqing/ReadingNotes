_代码复用是面向对象编程(OOP)最具魅力的原因之一。_

1. 如何在不污染源代码的基础上复用现存代码？

    (1).在新类中创建现有类的对象。这种方式直接了当，叫做`组合`.
    (2).第二种方式更为微妙，创建现有类类型的新类，也就是`继承`.
  
2. 子类会自动获取父类的方法，即使没有在子类中看到这些方法的显式的定义。这样一来，继承可以看做复用类。在新类中你可以调用父类的方法，但是，
    不能只是简单的调用，因为会产生递归调用。为了解决这个问题，Java的关键字`super`引用了当前类继承的基类，因此，例如`super.toString()`
    指的是调用基类的`toString()`方法。
    
3. 初始化基类：
    Java自动在派生类的构造函数中插入对基类构造函数的调用。例如：
```java
class Art {
  Art() {
    System.out.println("Art constructor");
  }
}

class Drawing extends Art {
  Drawing() {
    System.out.println("Drawing constructor");
  }
}

public class Cartoon extends Drawing {
  public Cartoon() {
    System.out.println("Cartoon constructor");
  }
  public static void main(String[] args) {
    Cartoon x = new Cartoon();
  }
}
/* Output:
Art constructor
Drawing constructor
Cartoon constructor
*/
```

4. 带参数的构造函数：
    上面的例子中的构造函数都是无参数的；编译器很容易调用这些构造函数，因为不需要参数，如果没有无参构造函数，或者必须调用具有参数的基类的
    构造函数，则必须使用`super关键字`和适当的参数列表`显式的`编写对基类构造函数的调用：
```java
class Game {
  Game(int i) {
    System.out.println("Game constructor");
  }
}

class BoardGame extends Game {
  BoardGame(int i) {
    super(i);
    System.out.println("BoardGame constructor");
  }
}

public class Chess extends BoardGame {
  Chess() {
    super(11);
    System.out.println("Chess constructor");
  }
  public static void main(String[] args) {
    Chess x = new Chess();
  }
}
/* Output:
Game constructor
BoardGame constructor
Chess constructor
*/
```
如果你没有在`BoardGame构造函数`中调用基类构造函数，编译器就会报错找不到`Game()`的构造函数，此外，对基类构造函数的调用必须是派生类构造
函数中的`第一个操作`。(如果你写错了，编译器会提醒你)

5. 委托：
    Java不直接支持的第三种重用关系称为委托。它介于组合重用和继承重用之间。
    委托模式是软件设计模式中的一项基本技巧。在委托模式中，有两个对象参与处理同一个请求，接受请求的对象将请求委托给另一个对象来处理。
    委托模式是一项基本技巧，许多其他的模式，如状态模式、策略模式、访问者模式本质上是在更特殊的场合采用了委托模式。委托模式使得我们可以
    用聚合来替代继承，它还使我们可以模拟mixin。
    “委托”在C#中是一个语言级特性，而在Java语言中没有直接的对应，`但是我们可以通过动态代理来实现委托！`
```java
/**
 * 委托者接口
 */
public interface Subject {
    
    /**
     * 添加被委托对象
     * @param obj 被委托的对象
     */
    void addObserver(Observer obj);

    /**
     * 移除所有对象对象
     */
    void removeAll();

    /**
     * 委托的事件
     * @param subject 委托对象
     * @param observer 被委托对象
     * @param obj 传递给被委托者的数据
     */
    void event(Subject subject, Observer observer, Object obj);

    /**
     * 委托很多事件
     * @param subject 委托对象
     * @param obj 被委托的对象
     */
    void addObserver(Subject subject, Observer obj);

    /**
     * 委托对象的唯一标识
     */
    String getName();
}
```

```java
/**
 * 被委托者接口
 */
public interface Observer {

    /**
     * 被委托者所要执行的事件（方法即处理程序）
     * @param subject 委托者对象
     * @param obj 委托的事情的数据
     */
    void doEvent(Subject subject, Object obj);
}
```

```java
/**
 * 委托对象实现类
 */
public class SubjectObject implements Subject {
    /**
     * 本类对象唯一标识
     */
    private String name;

    /**
     * 存储被委托者对象的集合，位于java.util包中
     */
    List<Observer> list = new ArrayList<>();
    /**
     * 构造方法
     */
    SubjectObject(String name){
        this.name = name;
    }
    /**
     * 添加被委托对象的方法
     * @param obj:被委托对象
     */
    @Override
    public void addObserver(Observer obj) {
        // TODO Auto-generated method stub
        if (list==null) {
            throw new NullPointerException();
        } else {
            if (!list.contains(obj)) {
                list.add(obj);
            }    
        }
    }    
    /**
     * 委托的事件方法
     */
    @Override
    public void event(Subject s, Observer obj, Object o) {
        // TODO Auto-generated method stub
        obj.doEvent(s,o);
    }
    /**
     * 移除所有被委托对象
     */
    @Override
    public void removeAll() {
        // TODO Auto-generated method stub
        list.clear();
    }
    /**
     * 全部被委托者要做的事件方法
     */
    @Override
    public void eventAll(Subject s,Object obj) {
        // TODO Auto-generated method stub
        for(Observer o:list) {
            o.doEvent(s,obj);
        }
    }
    /**
     * 获取唯一标识name
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }
}
```

```java
/**
 * 被委托的对象的实现类
 */
class ObserverObject implements Observer{
    /**
     * 被委托的对象的唯一标识
     */
    private String name;
    /**
     * 构造函数
     */
    ObserverObject(String name){
        this.name = name;
    }
    /**
     * 被委托对象要做的事情
     * @param data:事情数据
     */
    @Override
    public void doEvent(Subject s,Object data) {
        // TODO Auto-generated method stub
        System.out.println(s.getName() + "你好，" + "我是" + name + "，你委托我" + data + "的事我已经做完了！");
    }
    
}
```

```java
/**
 * 测试类，整个程序的入口
 * @author 张三
 *
 */
public class demo {
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // 委托者对象
        SubjectObject s = new SubjectObject("张三");
        // 被委托者对象a
        ObserverObject a = new ObserverObject("李四");
        // 委托对象添加被委托对象
        s.addObserver(a);
        // 委托对象  委托 被委托对象 买早餐
        s.event(s, a, "买早餐");
        // 被委托对象b
        ObserverObject b = new ObserverObject("王五");
        // 委托对象添加被委托对象b
        s.addObserver(b);
        // 委托对象 委托 所有被委托对象 要美女的联系方式
        s.eventAll(s, "要美女的联系方式");
    }
}
```
代码编写有个这样的原则：能不用继承就不用继承，能使用委托实现的就不使用继承。两个类有明显示的层级关系时使用继承，没有明显的层级关系，
仅仅是为了在一个类中使用另一个类的方法时应该使用委托。

根据《重构》中写道：现在有滥用继承的趋势，JDK 中 Stack 就是一个滥用继承的典型！
java.util.Stack 继承自 java.util.Vector，其实 Stack 与 Vector 在用途上完全是风马牛不相及的两个容器。

6. 