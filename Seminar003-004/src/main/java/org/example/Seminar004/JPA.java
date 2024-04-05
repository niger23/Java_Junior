package org.example.Seminar004;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;

//            try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
//                 Statement st = connection.createStatement()) {
//                ResultSet rs = st.executeQuery("select id, name from persons");
//                while (rs.next()) {
//                    System.out.println("id = " + rs.getInt("id"));
//                    System.out.println("name = " + rs.getString("name"));
//                }
//            }

public class JPA {
    public static void main(String[] args) throws SQLException {
        Configuration configure = new Configuration().configure();
        try (SessionFactory sessionFactory = configure.buildSessionFactory()) {
            insertNewPerson(sessionFactory);

            try (Session session = sessionFactory.openSession()) {
                Person person = session.find(Person.class, 1L);
                System.out.println(person);

                Transaction tx = session.beginTransaction();
                person.setName("new name");
                session.merge(person);
                System.out.println(person);
//                session.remove(person);
                tx.commit();

                person = session.find(Person.class, 1L);
                System.out.println(person);

            }

            try (Session session = sessionFactory.openSession()) {
                Query<Person> query = session.createQuery("select p from Person p where id > 5 ", Person.class);
                List<Person> resultList = query.getResultList();
                Transaction tr = session.beginTransaction();
                for (Person it : resultList) {
                    it.setName("UPDATED");
                    session.merge(it);
                }
                tr.commit();
                System.out.println(session.createQuery("from Person p", Person.class).getResultList());




            }

        }
    }

    private static void insertNewPerson(SessionFactory sessionFactory) {

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Department department = new Department();
            department.setId(555L);
            department.setName("DEP NAME");
            session.persist(department);
            for (long i = 1; i <= 10; i++) {
                Person person = new Person();
                person.setId(i);
                person.setName("Person #" + i);
                person.setDepartment(department);
                session.persist(person);

            }


            tx.commit();

        }
    }
}
