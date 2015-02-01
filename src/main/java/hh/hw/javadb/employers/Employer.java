package hh.hw.javadb.employers;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
//import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "employers")
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator="increment")
//    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "employer_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    public Employer() {
        this.id = -1; // @QQ is there any other way except this and = null?/
        this.regDate = new Date();
    }

    public Employer(int id, String title) {
        this.id = id;
        this.title = title;
        this.regDate = new Date();
    }

    public Employer(String title) {
        this(-1, title);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRegDate(Date regDate) {
        System.out.println("Registration date cannot b changed");
//        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return String.format("{id='%s', title='%s', regDate='%s'}", id, title, regDate);
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Employer other = (Employer) obj;
        return this.id == other.id;
    }

}
