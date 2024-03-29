package tests;

public class TestRunnerDemo {
    public static void main(String[] args) {
        TestRunner.run(TestRunnerDemo.class);
    }
    @BeforeAll
    void beforeAll1() {
        System.out.println("Перед всеми тестами");
    }
    @BeforeAll
    void beforeAll2() {
        System.out.println("Делаем это");
    }
    @BeforeEach
    void beforeEach() {
        System.out.println("Перед каждым тестом");
    }
    @AfterAll
    void afterAll1() {
        System.out.println("После всех тестов");
    }
    @AfterAll
    void afterAll2() {
        System.out.println("Делаем это");
    }
    @AfterEach
    void afterEach() {
        System.out.println("После каждого теста");
    }

    @Test(order = 1)
    void test1() {
        System.out.println("--- тест №1 ---");
    }
    @Test(order = 2)
    void test2() {
        System.out.println("--- тест №2 ---");
    }
    @Test(order = 3)
     void test3() {
        System.out.println("--- тест №3 ---");
    }
}
