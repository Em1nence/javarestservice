package model;

import java.util.ArrayList;
import java.util.List;

public class Course {

    public Course(int id, String title) {
        this.id = id;
        this.title = title;
    }
    private int id;
    private String title;
    private String description;
    private Instructor instructor;

    private List<Student> students;

    public Course() {
        this.students = new ArrayList<>();
    }

    public Course(int id, String title, String description, Instructor instructor, List<Student> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }


}
