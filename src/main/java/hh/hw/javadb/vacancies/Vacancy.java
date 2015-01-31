package hh.hw.javadb.vacancies;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;


public class Vacancy {
    private int id;
    private String title;
    private int employer_id;
//    private Date postDate;  
//
    public Vacancy(String title, int employer_id) {
        this(-1, title, employer_id); // ??????
    }
    
    public  Vacancy(int id, String title, int employer_id) {
        this.id = id;
        this.title = title;
        this.employer_id = employer_id;
//        this.postDate =  new Date();
    }    

    /// hax (((( ?????
//    public  Vacancy(int id, String title, int employer_id, Date date) {
//        this.id = id;
//        this.title = title;
//        this.employer_id = employer_id;
//        this.postDate =  date;
//    }      
    
    public int getId() {
        return this.id;
    }
    
    void setId(final int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getEmployer_id() {
        return employer_id;
    }

//    public Date getPostDate() {
//        return postDate;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEmployer_id(int employer_id) {
        this.employer_id = employer_id;
    }

    public void setPostDate(Date postDate) {
        System.out.println("Registration date cannot b changed");
//        this.postDate = postDate;
    }
        
    @Override
    public String toString() {
        return "Vacancy{" + "id=" + id + ", title=" + title + ", employer_id=" + employer_id + "}";
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vacancy other = (Vacancy) obj;
        return this.id == other.id;
    }

    
    
}
