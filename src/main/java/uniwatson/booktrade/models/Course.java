package uniwatson.booktrade.models;

public class Course {
    private int courseId;      // Course_ID
    private String isbn;       // Used_book
    private String professor;  // Professor
    private String name;       // Course_name
    private String bookName;   // 顯示用：對應 book.Name

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
}
