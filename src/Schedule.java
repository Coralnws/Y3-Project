import java.time.LocalDate;

public class Schedule {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private String textColor;

    public Schedule(String title, LocalDate start, LocalDate end, String color, String textColor){
        this.title = title;
        this.startDate = start;
        this.endDate = end;
        this.color = color;
        this.textColor = textColor;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public String getTitle() {
        return title;
    }
    public String getColor() {
        return color;
    }
    public String getTextColor() {
        return textColor;
    }
}
