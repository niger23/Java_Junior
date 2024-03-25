import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Homework {
    public static void main(String[] args) {
        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            departments.add(new Department("Depart #" + i));
        }
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            persons.add(new Person(
                    "Person #" + i,
                    ThreadLocalRandom.current().nextInt(20,61),
                    ThreadLocalRandom.current().nextInt(20000, 100000)*1.0,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
            ));
        }
        printNamesOrdered(persons);
        System.out.println("====");
        System.out.println(printDepartmentOldestPerson(persons));
        System.out.println("====");
        System.out.println(findFirstPersons(persons));
        System.out.println("====");
        System.out.println(findTopDepartment(persons));

    }

    /**
     * Реалзиовать методы, описанные ниже:
     */

    /**
     * Вывести на консоль отсортированные (по алфавиту) имена персонов
     */
    public static void printNamesOrdered(List<Person> persons) {
        persons.stream()
                .sorted(Comparator.comparing(Person::getName))
                .forEach(System.out::println);
    }

    /**
     * В каждом департаменте найти самого взрослого сотрудника.
     * Вывести на консоль мапипнг department -> personName
     * Map<Department, Person>
     */
    public static Map<Department, Person> printDepartmentOldestPerson(List<Person> persons) {
        Comparator<Person> comparing = Comparator.comparing(Person::getAge);
        return persons.stream()
                .collect(Collectors.toMap(Person::getDepartment, Function.identity(), (first, second) -> {
                    if (comparing.compare(first, second) > 0) {
                        return first;
                    }
                    return second;
                }));
    }

    /**
     * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
     */
    public static List<Person> findFirstPersons(List<Person> persons) {
        return persons.stream()
                .filter(it -> it.getSalary() > 50_000)
                .filter(it -> it.getAge() < 30)
                .limit(10)
                .toList();
    }

    /**
     * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
     */
    public static Optional<Department> findTopDepartment(List<Person> persons) {
        return persons.stream().collect(Collectors.groupingBy(Person::getDepartment, Collectors.summingDouble(Person::getSalary))).
                entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).map(Map.Entry::getKey);

    }

}