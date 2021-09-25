package com.matthewcasperson.azureapi.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "audit")
public class Audit {
    private Long id;
    private String message;
    private java.sql.Timestamp date;

    public Audit() {

    }

    public Audit(String message) {
        this.message = message;
        this.date = new Timestamp(System.currentTimeMillis());
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Column(name = "date")
    public Timestamp getDate() {
        return date;
    }
}
