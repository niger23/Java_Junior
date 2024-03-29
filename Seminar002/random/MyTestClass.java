package random;

public class MyTestClass {
    public static void main(String[] args) {
        MyTestClass myTestClass = new MyTestClass();

        RandomIntegerInit.init(myTestClass);

        System.out.println(myTestClass.getValue());
    }
    @RandomInteger(min = 50, max = 51)
    private int value;

    public int getValue() {
        return value;
    }
}
