package com.school.domain.entities;

import com.school.common.enumerations.MarkType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "marks")
public class Mark {

    private Integer id;

    private int value;

    private MarkType type;

    private Subject subject;

    private LocalDateTime writtenOn;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Enumerated(EnumType.STRING)
    public MarkType getType() {
        return type;
    }

    public void setType(MarkType type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Column(name = "written_on")
    public LocalDateTime getWrittenOn() {
        return writtenOn;
    }

    public void setWrittenOn(LocalDateTime writtenOn) {
        this.writtenOn = writtenOn;
    }
}