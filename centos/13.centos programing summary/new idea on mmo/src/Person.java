import java.io.IOException;

public class Person  
    implements java.io.Serializable  
{  
    private String name;  
    private int age;  
 
    public Person(String name , int age)  
    {  
        System.out.println("有参数的构造器");  
        this.name = name;  
        this.age = age;  
    }  
 
    public void setName(String name)  
    {  
        this.name = name;  
    }  
    public String getName()  
    {  
         return this.name;  
    }  
 
    public void setAge(int age)  
    {  
        this.age = age;  
    }  
    public int getAge()  
    {  
         return this.age;  
    }  
 //这里定义了对person对象的序列化机制
    private void writeObject(java.io.ObjectOutputStream out)  
         throws IOException  
    {  
        out.writeObject(new StringBuffer(name).reverse());  
        out.writeInt(age);  
    }  
//这里定义了对person对象的序列化机制
    private void readObject(java.io.ObjectInputStream in)  
         throws IOException, ClassNotFoundException  
    {  
        this.name = ((StringBuffer)in.readObject()).reverse().toString();  
        this.age = in.readInt();  
    }  
} 
