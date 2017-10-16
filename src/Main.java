import com.sun.xml.internal.ws.server.provider.ProviderInvokerTube;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;

public class Main {

//      Создать класс, который может выполнять «тесты», в качестве тестов выступают классы
//      с наборами методов с аннотациями @Test. Для этого у него должен быть статический метод
//      start(), которому в качестве параметра передается или объект типа Class, или имя класса.
//      Из «класса-теста» вначале должен быть запущен метод с аннотацией @BeforeSuite, если такой имеется,
//      далее запущены методы с аннотациями @Test, а по завершению всех тестов – метод с аннотацией @AfterSuite.
//      К каждому тесту необходимо также добавить приоритеты (int числа от 1 до 10), в соответствии с которыми будет
//      выбираться порядок их выполнения, если приоритет одинаковый, то порядок не имеет значения. Методы с
//      аннотациями @BeforeSuite и @AfterSuite должны присутствовать в единственном экземпляре, иначе необходимо
//      бросить RuntimeException при запуске «тестирования».

//      (Это домашнее задание никак не связано с темой тестирования через JUnit и использованием этой библиотеки,
//      то есть проект пишется с нуля)

    public static void main(String[] args) {

        Class cat = Cat.class;

        try {
            start(cat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void start(Class cl) throws InvocationTargetException, IllegalAccessException, BrokenBarrierException, InterruptedException {
        Cat barsik = new Cat("Barsik", 4);
        List<Method> beforeListMethod = new ArrayList<>();
        List<Method> afterListMethod = new ArrayList<>();
        Map<Method, Integer> testListMethod = new HashMap<>();


        for (Method method : cl.getDeclaredMethods()) {

            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeListMethod.size() > 0) {
                    throw new RuntimeException();
                } else {
                    beforeListMethod.add(method);
                }
            }
            if (method.isAnnotationPresent(Test.class)) {
                testListMethod.put(method, method.getAnnotation(Test.class).priority());
            }

            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterListMethod.size() > 0) {
                    throw new RuntimeException();
                } else {
                    afterListMethod.add(method);
                }
            }
        }


        invokeMethod(testListMethod, barsik);
    }

    private static void invokeMethod(Map<Method, Integer> map, Object object) throws InvocationTargetException, IllegalAccessException {
        int count = 0;
        boolean tmp = true;
        while (tmp) {
            for (Map.Entry<Method, Integer> entry : map.entrySet()) {
                if (entry.getValue() == count) {
                    entry.getKey().invoke(object);
                    map.remove(entry.getKey());
                } else count++;

                if (map.size() == 0) tmp = false;
            }
        }
    }
}
