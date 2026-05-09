package model;

public class Comment {

    public String comment_id;
    public String message;
    public boolean isAccepted;
    public String student_id;

    public Comment(String comment_id, String message, boolean isAccepted, String student_id) {
        this.comment_id = comment_id;
        this.message = message;
        this.isAccepted = isAccepted;
        this.student_id = student_id;
    }

    public Comment() {
    }

    public String getComment_id() {
        return comment_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public String getStudent_id() {
        return student_id;
    }

}
