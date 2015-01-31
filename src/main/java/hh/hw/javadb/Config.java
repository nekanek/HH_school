package hh.hw.javadb;

import hh.hw.javadb.employers.Employer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.service.ServiceRegistryBuilder;

public class Config {

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().addAnnotatedClass(Employer.class);
//        configuration.configure();
//        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
//        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/employers");
//        configuration.setProperty("hibernate.connection.username", "neko");        
//        configuration.setProperty("hibernate.connection.password", "1u1z");        
//        configuration.setProperty("hibernate.id.new_generator_mappings", "true");

//         ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
//                .applySettings(configuration.getProperties())
//                .buildServiceRegistry();
//        return configuration.buildSessionFactory(serviceRegistry);

        return configuration.buildSessionFactory();
    }

    public static Connection getConnection() throws SQLException, IOException {
        String url = "jdbc:postgresql:hh_hw_javadb";
        String username = "neko";
        String password = "1u1z";
        String drivers = "org.postgresql.Driver"; 
        System.setProperty("jdbc.drivers", drivers);
        return DriverManager.getConnection(url, username, password);
    }

} 

