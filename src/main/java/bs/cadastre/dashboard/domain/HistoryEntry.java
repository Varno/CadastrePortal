package bs.cadastre.dashboard.domain;

import org.apache.james.mime4j.field.datetime.DateTime;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class HistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 24)
    private String userName;
    @Temporal(TemporalType.DATE)
    private java.util.Date actionTime;
    private String actionType;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public java.util.Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(java.util.Date actionTime) {
        this.actionTime = actionTime;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
