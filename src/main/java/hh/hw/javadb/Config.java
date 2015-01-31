package hh.hw.javadb;

import hh.hw.javadb.employers.Employer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.postgresql.ds.PGSimpleDataSource;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.service.ServiceRegistryBuilder;

public class Config {
    private static final String url = "jdbc:postgresql:hh_hw_javadb";
    private static final String username = "neko";
    private static final String password = "1u1z";    
    

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().addAnnotatedClass(Employer.class);
//        configuration.configure();
//        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
//        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/employers");
//        configuration.setProperty("hibernate.connection.username", username);        
//        configuration.setProperty("hibernate.connection.password", password);        
//        configuration.setProperty("hibernate.id.new_generator_mappings", "true");

//         ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
//                .applySettings(configuration.getProperties())
//                .buildServiceRegistry();
//        return configuration.buildSessionFactory(serviceRegistry);

        return configuration.buildSessionFactory();
    }

//    public static Connection getConnection() throws SQLException, IOException {

//        String drivers = "org.postgresql.Driver"; 
//        System.setProperty("jdbc.drivers", drivers);
//        return DriverManager.getConnection(url, username, password);
//    }

    public static PGSimpleDataSource pgSimpleDataSource()
            throws SQLException {

        final PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(url);
        pgSimpleDataSource.setUser(username);
        pgSimpleDataSource.setPassword(password);
        return pgSimpleDataSource;
    }
}

