import java.time.LocalDate;

public class Schedule {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    public Schedule(String title, LocalDate start, LocalDate end){
        this.title = title;
        this.startDate = start;
        this.endDate = end;
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

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
