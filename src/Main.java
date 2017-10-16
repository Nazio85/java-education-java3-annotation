import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;

public class Main {
    public static final String BEFORE_SUITE = "BeforeSuite";
    public static final String AFTER_SUITE = "AfterSuite";

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
        Cat barsik = new Cat("Matroskin", 4);
        Dog barbos = new Dog("Barbos", 7);

        try {
            start(barsik);
            start(barbos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void start(Object object) throws InvocationTargetException, IllegalAccessException {
        Class cl = object.getClass();

        Map map = new HashMap();

        for (Method method : cl.getDeclaredMethods()) {

            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (map.containsValue(BEFORE_SUITE)) {
                    throw new RuntimeException();
                } else {
                    map.put(method, BEFORE_SUITE);
                }
            }
            if (method.isAnnotationPresent(Test.class)) {
                map.put(method, method.getAnnotation(Test.class).priority());
            }

            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (map.containsValue(AFTER_SUITE)) {
                    throw new RuntimeException();
                } else {
                    map.put(method, AFTER_SUITE);
                }
            }
        }

        readMap(cl, map, object);
    }

    private static <KEY, VALUE> void readMap(Class cl, Map<KEY, VALUE> map, Object object) throws InvocationTargetException, IllegalAccessException {
        int count = 0;
        boolean tmp = true;
        int fullMap = map.size();
        Map<KEY, VALUE> tmpMap = new HashMap<>(map);

        try {
            while (tmp) {
                for (Map.Entry<KEY, VALUE> entry : map.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        if (entry.getValue().equals(BEFORE_SUITE) & tmpMap.size() == fullMap) {
                            invokeMethod(tmpMap, object, entry);
                        } else if (entry.getValue().equals(AFTER_SUITE) & tmpMap.size() == 1) {
                            invokeMethod(tmpMap, object, entry);
                        }
                    } else if (entry.getValue() instanceof Integer) {
                        int priority = (Integer) entry.getValue();
                        if (fullMap != tmpMap.size()) {
                            if (count == priority) invokeMethod(tmpMap, object, entry);
                        } else if (!map.containsValue(BEFORE_SUITE)){
                            if (count == priority) invokeMethod(tmpMap, object, entry);
                        }
                    }

                    if (tmpMap.size() == 0) {
                        tmp = false;
                        break;
                    }
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Object is not supported");
        }

    }

    private static <KEY, VALUE> void invokeMethod(Map<KEY, VALUE> map, Object object, Map.Entry<KEY, VALUE> entry) throws IllegalAccessException, InvocationTargetException {
        Method method = (Method) entry.getKey();
        method.invoke(object);
        map.remove(entry.getKey());
    }
}
