package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by anthony on 6/23/15.
 */
@Entity
public class Submission {

    @Id
    public int id;
    public Verdict verdict;
    public Date timestamp;

    public Submission(int id, Verdict verdict) {
        this.id = id;
        this.verdict = verdict;
        this.timestamp = new Date();
    }

}
