package hh.hw.javadb;

import hh.hw.javadb.employers.Employer;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateConfig {

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

//    private HibernateConfig() {
//    }
} 
;  

