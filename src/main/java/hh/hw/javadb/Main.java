package hh.hw.javadb;

import hh.hw.javadb.employers.*;
import hh.hw.javadb.vacancies.*;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.IOException;
import java.sql.SQLException;
import org.hibernate.SessionFactory;

class Main {

    public static void main(final String[] args) throws IOException, ClassNotFoundException, SQLException {
        // Hibernate CRU operations
        final SessionFactory sessionFactory = getSessionFactory();
        try {
            final EmployerService employerService = getEmployerService(sessionFactory);

            // Starting JDBC connection
            final PGSimpleDataSource dataSource = Config.pgSimpleDataSource();
            VacancyDAO vacancyServ = new VacancyService(dataSource);                  
            // clear both tables
            employerService.dropEmployersTable(vacancyServ);
            
            // Create in Hibernate
            Employer e1 = new Employer("Augmented future");
            employerService.addEmployer(e1);
            Employer e2 = new Employer("ML implementers");
            employerService.addEmployer(e2);
            System.out.println("Added two employers");
            System.out.println("employers in db: " + employerService.getAllEmployers());
            
            // Update in Hibernate
            e1.setTitle("Augmented present");
            employerService.updateEmployer(e1);
            System.out.println("Updated employer name");
            System.out.println("employers in db: " + employerService.getAllEmployers());

            // Create in JDBC
            Vacancy coolGuy = new Vacancy("Awesome job", e2.getId());
            vacancyServ.addVacancy(coolGuy);
            Vacancy aiBuilder = new Vacancy("AI construct developer", e1.getId());
            vacancyServ.addVacancy(aiBuilder);
            Vacancy aiEngineer = new Vacancy("AI construct engineer", e1.getId());
            vacancyServ.addVacancy(aiEngineer);
            System.out.println("3 vacancies created");
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());
            
            // Update in JDBC
            coolGuy.setTitle("Not so awesome job");
            vacancyServ.updateVacancy(coolGuy);
            System.out.println("Changed vacancy title");
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());            
            
            // Delete in Hibernate
            // Calls VacancyService to delete all vacancies added by this employer
            employerService.deleteEmployer(e2, vacancyServ);
            System.out.println("deleted employer " + e2.getId() + " and all corresponfing vacancies");
            System.out.println("employers in db: " + employerService.getAllEmployers());
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies()); 
            
//            e1 = new Employer("Augmented present");
//            employerService.addEmployer(e1);
//            System.out.println("Added employer again");
//            System.out.println("employers in db: " + employerService.getAllEmployers());

            
//            vacancyServ.deleteVacancy(coolGuy);
//            System.out.println("deleted vacancy with id " + coolGuy.getId());
//            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());

            // Delete in JDBC
            vacancyServ.deleteAllEmployersVacancies(e1);
            System.out.println("deleted all vacancies by employer with id " + e1.getId());
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());
            
            // cleaning up
            employerService.deleteEmployer(e1, vacancyServ);
            System.out.println("employers in db: " + employerService.getAllEmployers());
            
        } finally {
            sessionFactory.close();
        }
    }

    private static SessionFactory getSessionFactory() {
        return Config.getSessionFactory();
    }

    private static EmployerService getEmployerService(final SessionFactory sessionFactory) {        return new EmployerService(sessionFactory);
    }

}



