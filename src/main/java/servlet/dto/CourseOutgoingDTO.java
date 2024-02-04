package servlet.dto;

import java.util.List;

public class CourseOutgoingDTO {
    private int id;
    private String title;
    private String description;
    private int instructorId;

    public List<StudentOutgoingDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentOutgoingDTO> students) {
        this.students = students;
    }

    private List<StudentOutgoingDTO> students;

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

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }


}
