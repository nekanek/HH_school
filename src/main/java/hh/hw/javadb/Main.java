package hh.hw.javadb;

import static hh.hw.javadb.Config.getConnection;
import hh.hw.javadb.employers.*;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;

class Main {

    public static void main(final String[] args) throws IOException, ClassNotFoundException, SQLException {
        // Hibernate CRU operations
        final SessionFactory sessionFactory = getSessionFactory();
        try {
            final EmployerService employerService = getEmployerService(sessionFactory);
            Employer e1 = new Employer("Augmented future");
            Employer e2 = new Employer();
            employerService.addEmployer(e1);
            employerService.addEmployer(e2);
            System.out.println("!!! users in db: " + employerService.getAllEmployers());
            e1.setTitle("Augmented present");
            employerService.updateEmployer(e1);
            e2.setTitle("ML implementers");
            employerService.updateEmployer(e2);
            System.out.println("!!! users in db: " + employerService.getAllEmployers());
            employerService.deleteEmployer(e1);
            System.out.println("!!! users in db: " + employerService.getAllEmployers());
            e1 = new Employer("Augmented present");
            employerService.addEmployer(e1);
            System.out.println("!!! users in db: " + employerService.getAllEmployers());
        } finally {
            sessionFactory.close();
        }
        
        // test example
        try (Connection conn = getConnection()) {
            Statement stat = conn.createStatement();
            stat.executeUpdate("CREATE TABLE Greetings (Message CHAR(20))");
            stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");
            try (ResultSet result = stat.executeQuery("SELECT * FROM Greetings")) {
                if (result.next()) {
                    System.out.println(result.getString(1));
                }
            }
            stat.executeUpdate("DROP TABLE Greetings");
        }
    }

    private static SessionFactory getSessionFactory() {
        return Config.getSessionFactory();
    }

    private static EmployerService getEmployerService(final SessionFactory sessionFactory) {
        return new EmployerService(sessionFactory);
    }

}



