public class Cat {
    private String name;
    private int age;

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Test(priority = 1)
    public String eat() {
        System.out.println("Кот " + name + " пошел есть");
        return name;
    }

    @Test(priority = 0)
    public String tighten() {
        System.out.println("Кот " + name + " подтянулся");
        return name;
    }

    public int getAge() {
        return age;
    }

    @BeforeSuite
    public void prepareMethod(){
        System.out.println("Кот " + name + " проснулся!");
    }

    @AfterSuite
    public void endMethod (){
        System.out.println("Кот " + name + " пошел спать.");
    }
}
