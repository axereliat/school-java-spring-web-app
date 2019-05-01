package com.school.domain.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "absences")
public class Absence {

    private Integer id;

    private LocalDate date;

    private boolean isExcused;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Column(nullable = false, name = "is_excused")
    public boolean isExcused() {
        return isExcused;
    }

    public void setExcused(boolean excused) {
        isExcused = excused;
    }
}
