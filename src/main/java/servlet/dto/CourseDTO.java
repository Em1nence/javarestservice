package servlet.dto;
import java.util.List;

public class CourseDTO {
    public CourseDTO(int id, String title) {
        this.id = id;
        this.title = title;
    }
    private int id;
    private String title;
    private String description;
    private InstructorDTO instructor;
    private List<StudentDTO> students;

    public CourseDTO() {
    }

    public CourseDTO(int id, String title, String description, InstructorDTO instructor, List<StudentDTO> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.students = students;
    }

    // геттеры и сеттеры
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

    public InstructorDTO getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorDTO instructor) {
        this.instructor = instructor;
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
}
