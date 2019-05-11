package com.school.domain.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Student extends User {

    private Set<Mark> marks;

    private Set<Absence> absences;

    private int grade;

    private char classLetter;

    public Student() {
        this.absences = new HashSet<>();
        this.marks = new HashSet<>();
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "students_marks",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "mark_id", referencedColumnName = "id"))
    public Set<Mark> getMarks() {
        return marks;
    }

    public void setMarks(Set<Mark> marks) {
        this.marks = marks;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "students_absences",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "absence_id", referencedColumnName = "id"))
    public Set<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(Set<Absence> absences) {
        this.absences = absences;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Column(name = "class_letter")
    public char getClassLetter() {
        return classLetter;
    }

    public void setClassLetter(char classLetter) {
        this.classLetter = classLetter;
    }

    @Transient
    public void addMark(Mark mark) {
        this.marks.add(mark);
    }
}
