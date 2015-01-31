package hh.hw.javadb;

import hh.hw.javadb.employers.*;
import hh.hw.javadb.vacancies.Vacancy;
import hh.hw.javadb.vacancies.VacancyDAO;
import hh.hw.javadb.vacancies.VacancyService;
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
            System.out.println("employers in db: " + employerService.getAllEmployers());
            e1.setTitle("Augmented present");
            employerService.updateEmployer(e1);
            e2.setTitle("ML implementers");
            employerService.updateEmployer(e2);
            System.out.println("employers in db: " + employerService.getAllEmployers());
            employerService.deleteEmployer(e1);
            System.out.println("employers in db: " + employerService.getAllEmployers());
            e1 = new Employer("Augmented present");
            employerService.addEmployer(e1);
            System.out.println("employers in db: " + employerService.getAllEmployers());

            final PGSimpleDataSource dataSource = Config.pgSimpleDataSource();
            VacancyDAO vacancyServ = new VacancyService(dataSource);

            Vacancy coolGuy = new Vacancy("Awesome job", e2.getId());
            vacancyServ.addVacancy(coolGuy);
            Vacancy aiBuilder = new Vacancy("AI construct developer", e1.getId());
            vacancyServ.addVacancy(aiBuilder);
            Vacancy aiEngineer = new Vacancy("AI construct engineer", e1.getId());
            vacancyServ.addVacancy(aiEngineer);
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());

            coolGuy.setTitle("Not so awesome job");
            vacancyServ.updateVacancy(coolGuy);
            System.out.println("Changed vacancy title");
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());

            vacancyServ.deleteVacancy(coolGuy);
            System.out.println("deleted vacancy with id " + coolGuy.getId());
            System.out.println("vacancies in db: " + vacancyServ.getAllVacancies());

            System.out.println("vacancies by employer with id " + e1.getId() + " in db: " + vacancyServ.getAllEmployersVacancies(e1));
            System.out.println("vacancies by employer with id " + e2.getId() + " in db: " + vacancyServ.getAllEmployersVacancies(e2));
            
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



