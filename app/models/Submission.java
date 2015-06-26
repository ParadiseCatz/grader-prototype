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
    public Verdict verdict = null;
    public Date timestamp;

    public Submission(int id) {
        this.id = id;
        this.timestamp = new Date();
    }

    public void setVerdict(Verdict verdict) {
        this.verdict = verdict;
    }

}
