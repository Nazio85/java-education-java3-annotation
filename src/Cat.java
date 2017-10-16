public class Cat {
    private String name;
    private int age;

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Test(priority = 1)
    public String getName() {
        System.out.println("Cat name " + name);
        return name;
    }

    public int getAge() {
        return age;
    }

    @BeforeSuite
    public void prepareMethod(){
        System.out.println("Что то подготавливаю");
    }

    @AfterSuite
    public void endMethod (){
        System.out.println("закрытие программы");
    }
}
