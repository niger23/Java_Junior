package random;

import java.lang.reflect.Field;
import java.util.Random;

public class RandomIntegerInit {
    private final static Random rnd = new Random();

    public static void init(Object o) {
        Class<?> obClass = o.getClass();
        for (Field field : obClass.getDeclaredFields()) {
            RandomInteger anno = field.getAnnotation(RandomInteger.class);
            if (anno != null) {
                if (int.class.equals(field.getType())) {
                    int min = anno.min();
                    int max = anno.max();
                    int randomValue = rnd.nextInt(min, max);
                    field.setAccessible(true);
                    try {
                        field.set(o, randomValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}
