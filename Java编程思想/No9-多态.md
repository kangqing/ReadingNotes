1. 向上转型回顾：
    你可以把一个对象视作他的自身类型或他的基类类型。这种把一个对象引用当作他的基类引用的做法称作向上转型，因为继承图中基类一般位于上方。
    
2. 方法调用绑定：
    将一个方法调用和一个方法主体关联起来被称为`绑定`;
    若在程序执行前进行绑定（如果有的话，由编译器和链接器实现），叫做`前期绑定`;
    当编译器只有一个引用时，他无法知道酒精调用哪个方法才对，解决的办法就是`后期绑定`,他的含义就是在运行时根据对象的类型进行绑定。也称作
    `动态绑定`或`运行时绑定`.
    Java中除了static方法和final(private也是final方法)之外，其他所有的方法都是`后期绑定`，这意味着我们不需要判断后期绑定是否会发生，
    它自动发生。
    
3. 陷阱`重写private方法`:
    private隐式的加了final关键字，所以不能被重写，尽管这样做编译器不会报错，那是因为创建了一个新的方法，如果加上`@Override`注解，就会
    检测出问题。
    
4. 多态的典型案例：
    形状问题，圆形、三角形、正方形等等都是形状
```java
public class Shape {
    public void draw() {}
    public void erase() {}
}
```

```java
public class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("Circle.draw()");
    }
    @Override
    public void erase() {
        System.out.println("Circle.erase()");
    }
}

// polymorphism/shape/Square.java

public class Square extends Shape {
    @Override
    public void draw() {
        System.out.println("Square.draw()");
    }
    @Override
    public void erase() {
        System.out.println("Square.erase()");
    }
 }

// polymorphism/shape/Triangle.java

public class Triangle extends Shape {
    @Override
    public void draw() {
        System.out.println("Triangle.draw()");
    }
    @Override
    public void erase() {
        System.out.println("Triangle.erase()");
    }
}
```

```java
public class RandomShapes {
    private Random rand = new Random(47);

    public Shape get() {
        switch(rand.nextInt(3)) {
            default:
            case 0: return new Circle();
            case 1: return new Square();
            case 2: return new Triangle();
        }
    }

    public Shape[] array(int sz) {
        Shape[] shapes = new Shape[sz];
        // Fill up the array with shapes:
        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = get();
        }
        return shapes;
    }
}
```

```java
public class Shapes {
    public static void main(String[] args) {
        RandomShapes gen = new RandomShapes();
        // Make polymorphic method calls:
        for (Shape shape: gen.array(9)) {
            shape.draw();
        }
    }
}

/* 输出：
Triangle.draw()
Triangle.draw()
Square.draw()
Triangle.draw()
Square.draw()
Triangle.draw()
Square.draw()
Triangle.draw()
Circle.draw()
*/
```

5. 编写构造器原则：
    做尽量少的事让对象进入良好状态，如果有可能的话，尽量不要调用类中的任何方法，在基类的构造器中能安全调用的只有基类的final方法，（这也
    适用于可看做final的private方法）。这些方法不能被覆写，因此不会产生意想不到的结果。你可能永远无法遵守这条规则，但应该朝着他努力。

    