import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.stream.Stream;

public class Lambdas {
    public static void main(String[] args) {
//        lambdasIntro();
//        comparatorDemo();
//        methodReferences();

        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            integers.add(ThreadLocalRandom.current().nextInt(100));
        }
//        Все числа меньше 50 нужно умножить на 2 и распечатать
//        for (Integer integer : integers) {
//            if(integer < 50) {
//                System.out.println(integer *2);
//            }
//        }
        integers.stream()
                .parallel()
                .filter(x -> x< 50)
                .map(x -> x * 2)
                .forEach(System.out::println);
//  Создатиь стрим с рандомными числами, и сохранить первые 5 в список
        List<Integer> list = Stream.generate(() -> ThreadLocalRandom.current().nextInt(100))
                .limit(5)
                .toList();
        System.out.println(list);


    }

    private static void methodReferences() {
        Function<String, Integer> stringLength = String::length;
        System.out.println(stringLength.apply("Hello"));

//        Предикат который сравнивает все сходящие строки с "java"
        Predicate<String> javaTester = "java"::equals;
        System.out.println(javaTester.test("java"));
        System.out.println(javaTester.test("java2"));

        Supplier<ArrayList<Integer>> arrayListSupplier = ArrayList::new;
//        new ArrayList<> ();
        IntFunction<ArrayList<Integer>> arrayListIntFunction = ArrayList::new;
//        new ArrayList<> (int capacity);
        ArrayList<Integer> arrayList = arrayListSupplier.get();
        ArrayList<Integer> arrayList2 = arrayListIntFunction.apply(10);
    }

    private static void comparatorDemo() {
        List<Integer> integers = new ArrayList<>();
        Random random = new Random();
        integers.add(5);
        integers.add(2);
        integers.add(10);
        integers.add(15);
        integers.add(23);
        integers.add(55);
        integers.add(63);

//        Collections.sort(integers);
//        Collections.reverse(integers);
//        System.out.println(integers);

        Collections.sort(integers, Lambdas::compareIntegers);
        System.out.println(integers);
    }

    private static int compareIntegers (int x, int y) {
        if (x % 2 == 0 && y % 2 != 0) return -1;
        else if (x % 2 != 0 && y % 2 == 0) return 1;
        return Integer.compare(x, y);
    }

    private static void lambdasIntro() {
//  Лямбда принимает число и возвращает его квадрат
        UnaryOperator<Integer> square = x -> x*x;
        System.out.println(square.apply(5));
//  Лямбда принимает строку и возвращает длину
        Function<String, Integer> length = str -> str.length();
        System.out.println(length.apply("Hello!"));
//  Лямбда принимает строку и печатает
        Consumer<String> printer = str -> System.out.println(str);
        printer.accept("Hello, world!");
//  Лямбда генерации рандомных чисел
        Supplier<Integer> randomer = () ->  new Random().nextInt(101);
        System.out.println(randomer.get());
//  Лямбда вовыдит на консоль рандомные числа
        Runnable runnable = () -> printer.accept(String.valueOf(randomer.get()));
        runnable.run();
//  Лямбда предикат проверка целое число на чётность
        Predicate<Integer> evenTester = n -> n % 2 ==0;
        System.out.println(evenTester.test(10));
    }

    interface SquareInterface {
        int square(int x);
    }
    interface Foo {
        void foo();
    }
}
