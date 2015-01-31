package hh.hw.javadb.vacancies;

import hh.hw.javadb.employers.Employer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;


public class VacancyService implements VacancyDAO {
    
    private final DataSource dataSource;

    public VacancyService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void addVacancy(Vacancy vacancy) throws SQLException {
        if (vacancy.getId() > 0) {
            // TODO PROBLEM runtime exception, can we move to compile time?
            // try commenting out constructor with vacancy_id ?/
            throw new IllegalArgumentException("cannot add vacancy with already assigned id");
        }
        try (Connection conn = dataSource.getConnection()) {

            String query = "INSERT INTO vacancies (title, employer_id) VALUES (?, ?)";
            try (final PreparedStatement statement
                    = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, vacancy.getTitle());
                statement.setInt(2, vacancy.getEmployer_id());
//                statement.setDate(3, (Date) vacancy.getPostDate());

                statement.executeUpdate();
                try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    vacancy.setId(generatedKeys.getInt(1));  // TODO problem: what if user is already in some set?
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            System.out.println(e.getMessage());
            throw new RuntimeException("failed to add vacancy " + vacancy);
            
        }
    }

    @Override
    public void updateVacancy(Vacancy vacancy) throws SQLException {
        if (vacancy.getId() <= 0) {
            // TODO PROBLEM runtime exception, can we move to compile time?
            // try commenting out constructor with vacancy_id ?/
            throw new IllegalArgumentException("cannot update vacancy without id");
        }

        try (final Connection connection = dataSource.getConnection()) {

            final String query = "UPDATE vacancies SET title = ?, employer_id = ? WHERE vacancy_id = ?";
          try (final PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, vacancy.getTitle());
            statement.setInt(2, vacancy.getEmployer_id());
            statement.setInt(3, vacancy.getId());

            statement.executeUpdate();
          }

        } catch (SQLException e) {
          System.out.println(e.getMessage());
          throw new RuntimeException("failed to update vacancy");
        }        
    }


    @Override
    public List getAllVacancies() throws SQLException {
        try (final Connection connection = dataSource.getConnection()) {
            try (final Statement statement = connection.createStatement()) {
                final String query = "SELECT * FROM vacancies";
                try (final ResultSet resultSet = statement.executeQuery(query)) {
                    List<Vacancy> vacancies = new ArrayList<>();
                    int id;
                    int e_id;
                    String title;
//                    Timestamp date;
                    while (resultSet.next()) {
//                        Vacancy v = resultSet.unwrap(Vacancy.class) ;
                                id = resultSet.getInt("vacancy_id");
                                title = resultSet.getString("title");
                                e_id = resultSet.getInt("employer_id");
//                                date = resultSet.getTimestamp("post_date");
//                        );
                        vacancies.add(new Vacancy(id, title, e_id));
                    }
//                    System.out.println(vacancies);
                    return vacancies;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to get vacancies", e);
        }
    }


    @Override
    public List getAllEmployersVacancies(Employer employer) throws SQLException {
        try (final Connection connection = dataSource.getConnection()) {
            final String query = "SELECT * FROM vacancies WHERE employer_id = ?";
            try (final PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, employer.getId());

                try (final ResultSet resultSet = statement.executeQuery();) {
                    List<Vacancy> vacancies = new ArrayList<>();
                    int id;
                    int e_id;
                    String title;
                    while (resultSet.next()) {
                        id = resultSet.getInt("vacancy_id");
                        title = resultSet.getString("title");
                        e_id = resultSet.getInt("employer_id");
                        vacancies.add(new Vacancy(id, title, e_id));
                    }
                    return vacancies;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to get vacancies for one employer", e);
        }
    }

    @Override
    public void deleteVacancy(Vacancy vacancy) throws SQLException {
        try (final Connection connection = dataSource.getConnection()) {

            final String query = "DELETE FROM vacancies WHERE vacancy_id = ?";
            try (final PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, vacancy.getId());

                statement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("failed to remove vacancy by id " + vacancy.getId());
        }

    }

    @Override
    public void deleteAllEmployersVacancies(Employer employer) throws SQLException {
        
        List<Vacancy> vacancies = getAllEmployersVacancies(employer);
        for (Vacancy v : vacancies) {
            deleteVacancy(v);
        }
        
        
    }
    
}
