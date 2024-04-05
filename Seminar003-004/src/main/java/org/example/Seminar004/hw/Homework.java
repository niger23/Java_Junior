package org.example.Seminar004.hw;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 1. Создать сущность Student с полями:
 * 1.1 id - int
 * 1.2 firstName - string
 * 1.3 secondName - string
 * 1.4 age - int
 * 2. Подключить hibernate. Реализовать простые запросы: Find(by id), Persist, Merge, Remove
 * 3. Попробовать написать запрос поиска всех студентов старше 20 лет (session.createQuery)
 */
public class Homework {
    public static void main(String[] args) {
        Configuration configure = new Configuration().configure();
        try (SessionFactory sessionFactory = configure.buildSessionFactory()) {
            insertNewStudents(sessionFactory);
            findStudentsForAge(sessionFactory).forEach(System.out::println);
            addStudent(sessionFactory, new Student(99L, "NewStudent", "new", 25));
            printAllStudent(sessionFactory);
            removeStudent(sessionFactory, 99L);
            printAllStudent(sessionFactory);
            System.out.println(findStudent(sessionFactory, 3));
            modifyStudent(sessionFactory, 2, "mergeUser","new", 22);
            printAllStudent(sessionFactory);

        }

    }
    private static void modifyStudent(SessionFactory sessionFactory, long id, String firstName, String lastName, int age) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = session.find(Student.class, id);
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setAge(age);
            session.merge(student);
            tx.commit();
        }
    }
    private static Student findStudent(SessionFactory sessionFactory, long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Student.class, id);
        }
    }
    private static void printAllStudent(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.createQuery("from Student s", Student.class).getResultList().forEach(System.out::println);
        }
    }
    private static void removeStudent(SessionFactory sessionFactory, long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student delStudent = session.find(Student.class, id);
            session.remove(delStudent);
            tx.commit();
        }
    }
    private static void addStudent(SessionFactory sessionFactory, Student student) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
        }
    }
    private static List<Student> findStudentsForAge(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select s from Student s where age > 20 ", Student.class).getResultList();
        }
    }
    private static void insertNewStudents(SessionFactory sessionFactory) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            for (long i = 1; i <= 10; i++) {
                Student student = new Student();
                student.setId(i);
                student.setFirstName("Student #" + i);
                student.setLastName("Smith");
                student.setAge(ThreadLocalRandom.current().nextInt(16, 23));
                session.persist(student);
            }
            tx.commit();
        }
    }
}
