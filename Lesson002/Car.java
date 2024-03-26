public class Car {
    public String name;
    public String price;
    public String engType;
    public String engPower;
    public int maxSpeed;

    public Car(String name) {
        this.name = name;
        this.engType = "v8";
        this.engPower = "123";
        this.maxSpeed = 100;
        this.price = "100000";
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", engType='" + engType + '\'' +
                ", engPower='" + engPower + '\'' +
                ", maxSpeed=" + maxSpeed +
                '}';
    }
}
