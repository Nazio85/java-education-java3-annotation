public class Dog {
    private String name;
    private int age;

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Test(priority = 2)
    public String eat() {
        System.out.println("Пес " + name + " пошел есть");
        return name;
    }

    @Test(priority = 15)
    public String walk() {
        System.out.println("Пес " + name + " пошел гулять");
        return name;
    }

    public int getAge() {
        return age;
    }

    @BeforeSuite
    public void prepareMethod(){
        System.out.println("Пес " + name + " проснулся!");
    }

    @AfterSuite
    public void endMethod (){
        System.out.println("Пес " + name + " пошел спать.");
    }
}
