package tests;

import random.RandomInteger;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    public static void run(Class<?> testClass) {
        final Object testObj = initTestObj(testClass);

//        for (Method method : testClass.getDeclaredMethods()) {
//            if (method.getAnnotation(Test.class) != null) {
//                System.out.println(method.getAnnotation(Test.class).order());
//            }
//        }
//        final List<Method> methods = new ArrayList<>();
//        for (Method method : testClass.getDeclaredMethods()) {
//            if (method.accessFlags().contains(AccessFlag.PRIVATE)) {
//                continue;
//            }
//            if (method.getAnnotation(Test.class) != null) {
//                methods.add(method);
//                System.out.println(method.getAnnotation(Test.class).order());
//            }
//        }
//        methods.sort(Comparator.comparingInt(o -> o.getAnnotation(Test.class).order()));

        try {
            for (Method declaredMethod : testClass.getDeclaredMethods()) {
                if (declaredMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
                    continue;
                }
                if (declaredMethod.getAnnotation(BeforeAll.class) != null) {
                    declaredMethod.invoke(testObj);

                }
            }

            for (Method declaredMethod : testClass.getDeclaredMethods()) {
                if (declaredMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
                    continue;
                }
                if (declaredMethod.getAnnotation(Test.class) != null) {
                    for (Method method : testClass.getDeclaredMethods()) {
                        if (method.accessFlags().contains(AccessFlag.PRIVATE)) {
                            continue;
                        }
                        if (method.getAnnotation(BeforeEach.class) != null) {
                            method.invoke(testObj);
                        }
                    }
                        declaredMethod.invoke(testObj);

                    for (Method method : testClass.getDeclaredMethods()) {
                        if (method.accessFlags().contains(AccessFlag.PRIVATE)) {
                            continue;
                        }
                        if (method.getAnnotation(AfterEach.class) != null) {
                            method.invoke(testObj);

                        }
                    }

                }
            }


            for (Method declaredMethod : testClass.getDeclaredMethods()) {
                if (declaredMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
                    continue;
                }
                if (declaredMethod.getAnnotation(AfterAll.class) != null) {
                    declaredMethod.invoke(testObj);

                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    private static Object initTestObj(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора по умолчанию");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект тест класса");
        }
    }
}
