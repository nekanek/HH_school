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
            
            // Starting JDBC connection
            final PGSimpleDataSource dataSource = Config.pgSimpleDataSource();
            VacancyDAO vacancyServ = new VacancyService(dataSource);

            // Create in JDBC
            Vacancy coolGuy = new Vacancy("Awesome job", e2.getId());
            vacancyServ.addVacancy(coolGuy);
            Vacancy aiBuilder = new Vacancy("AI construct developer", e1.getId());
            vacancyServ.addVacancy(aiBuilder);
            Vacancy aiEngineer = new Vacancy("AI construct engineer", e1.getId());
            vacancyServ.addVacancy(aiEngineer);
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());
            
            // Update in JDBC
            coolGuy.setTitle("Not so awesome job");
            vacancyServ.updateVacancy(coolGuy);
            System.out.println("Changed vacancy title");
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());            
            
            // Delete in Hibernate
            // Calls VacancyService to delete all vacancies added by this employer
            employerService.deleteEmployer(e1, vacancyServ);
            System.out.println("employers in db: " + employerService.getAllEmployers());
            e1 = new Employer("Augmented present");
            employerService.addEmployer(e1);
            System.out.println("employers in db: " + employerService.getAllEmployers());

            // Delete in JDBC
            vacancyServ.deleteVacancy(coolGuy);
            System.out.println("deleted vacancy with id " + coolGuy.getId());
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());

            
            vacancyServ.deleteAllEmployersVacancies(e1);
            System.out.println("deleted all vacancies by employer with id " + e1.getId());
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());            
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



