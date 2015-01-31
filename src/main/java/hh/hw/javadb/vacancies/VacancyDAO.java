package hh.hw.javadb.vacancies;

import hh.hw.javadb.employers.Employer;
import java.sql.SQLException;
import java.util.List;

public interface VacancyDAO {
    public void addVacancy(Vacancy vacancy) throws SQLException;

    public void updateVacancy(Vacancy vacancy) throws SQLException;

    public List getAllVacancies() throws SQLException;
    
    public List getAllEmployersVacancies(Employer employer) throws SQLException;
    
    public void deleteVacancy(Vacancy vacancy) throws SQLException;
    
    public void deleteAllEmployersVacancies(Employer employer) throws SQLException;
    
}
