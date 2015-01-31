package hh.hw.javadb.employers;

import java.sql.SQLException;
import java.util.List;

public interface EmployerDAO {

    public void addEmployer(Employer employer) throws SQLException;

    public void updateEmployer(Employer employer) throws SQLException;

    public Employer getEmployerById(Long id) throws SQLException;

    public List getAllEmployers() throws SQLException;

    public void deleteEmployer(Employer employer) throws SQLException;
    
}
