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
            
            // CRUD example
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

            // Delete in JDBC
            vacancyServ.deleteAllEmployersVacancies(e1);
            System.out.println("deleted all vacancies by employer with id " + e1.getId());
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());
            System.out.println("employers in db: " + employerService.getAllEmployers());
            
            // cleaning up
            System.out.println("deleted employer with id " + e1.getId());
            employerService.deleteEmployer(e1, vacancyServ);
            System.out.println("employers in db: " + employerService.getAllEmployers());
            
            // Testing unroll of commits in transactions
            /* to test,
             * - comment CRUD example
             * - uncomment this example
             * - uncomment two statements "throw new SQLException("exception check");" in VacancyService.deleteVacancy() method
             * - also comment conn.commit(); in VacancyService.deleteVacancy() method
             * ..and it's all because someone didn't learn to do tests properly yet. sorry.(
            */
            
//            Employer e3 = new Employer("AI Black Box");
//            employerService.addEmployer(e3);
//            Vacancy jonny = new Vacancy("Data carrier", e3.getId());
//            vacancyServ.addVacancy(jonny);
//            Vacancy dolphin = new Vacancy("AI construct friend", e3.getId());
//            vacancyServ.addVacancy(dolphin);   
//            
//            // should see db with employer and two vacancies
//            employerService.deleteEmployer(e3, vacancyServ);
//            System.out.println("tried to delete employer with id " + e3.getId() + " (vacancies and employer should still b present in db)");
//            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());
//            System.out.println("employers in db: " + employerService.getAllEmployers());
            
            
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



