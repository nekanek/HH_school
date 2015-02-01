package hh.hw.javadb.vacancies;

import hh.hw.javadb.employers.Employer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class VacancyService implements VacancyDAO {

    private final DataSource dataSource;

    public VacancyService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addVacancy(Vacancy vacancy) throws SQLException {
        if (vacancy.getId() > 0) {
            throw new IllegalArgumentException("cannot add vacancy with already assigned id");
        }
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        try {
            String query = "INSERT INTO vacancies (title, employer_id) VALUES (?, ?)";
            try (final PreparedStatement statement
                    = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, vacancy.getTitle());
                statement.setInt(2, vacancy.getEmployer_id());

                statement.executeUpdate();
                try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    vacancy.setId(generatedKeys.getInt(1));  
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            System.out.println(e.getErrorCode());
            System.out.println(e.getMessage());
            throw new RuntimeException("failed to add vacancy " + vacancy);
        }
        finally { conn.close();}
    }

    @Override
    public void updateVacancy(Vacancy vacancy) throws SQLException {
        if (vacancy.getId() <= 0) {
            throw new IllegalArgumentException("cannot update vacancy without id");
        }
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        try  {
            final String query = "UPDATE vacancies SET title = ?, employer_id = ? WHERE vacancy_id = ?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, vacancy.getTitle());
                statement.setInt(2, vacancy.getEmployer_id());
                statement.setInt(3, vacancy.getId());

                statement.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            System.out.println(e.getMessage());
            throw new RuntimeException("failed to update vacancy");
        }
        finally { conn.close();}
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
            throw new RuntimeException("failed to get vacancies", e);
        }
    }

    @Override
    public List getAllEmployersVacancies(Employer employer) throws SQLException {
        try (final Connection conn = dataSource.getConnection()) {
            final String query = "SELECT * FROM vacancies WHERE employer_id = ?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
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
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);        
        try {
            final String query = "DELETE FROM vacancies WHERE vacancy_id = ?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, vacancy.getId());
                statement.executeUpdate();
            }
            // uncomment this for exception test
//            throw new SQLException("exception check"); 
            // comment this for exception test
            conn.commit();
        } catch (Exception e) {
            System.out.println("catch in vcanc");
            conn.rollback();
            System.out.println("vacancies in db: " + getAllVacancies()); 
            System.out.println(e.getMessage());
            // uncomment this for exception test
//            throw new SQLException("exception was catched in vacancies");
        }
        finally { conn.close();}
    }

    @Override
    public void deleteAllEmployersVacancies(Employer employer) throws SQLException {
        List<Vacancy> vacancies = getAllEmployersVacancies(employer);
        for (Vacancy v : vacancies) {
            deleteVacancy(v);
        }

    }

}
