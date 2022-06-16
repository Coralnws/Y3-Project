import javafx.scene.paint.Color;

public class Course {
    private String name;
    private int weekDay;
    private int periodStart;
    private int periodEnd;
    private int startWeek;
    private int endWeek;
    private boolean isOddWeek;
    private boolean isEvenWeek;
    private String color;

    public Course(String name, int weekDay, int periodStart, int periodEnd, int startWeek, int endWeek, boolean isOddWeek, boolean isEvenWeek, String color){
        this.name = name;
        this.weekDay = weekDay;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.isOddWeek = isOddWeek;
        this.isEvenWeek = isEvenWeek;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public int getWeekDay() {
        return weekDay;
    }
    public int getPeriodStart() {
        return periodStart;
    }
    public int getPeriodEnd() {
        return periodEnd;
    }
    public int getStartWeek() {
        return startWeek;
    }
    public int getEndWeek() {
        return endWeek;
    }
    public boolean isEvenWeek() {
        return isEvenWeek;
    }
    public boolean isOddWeek() {
        return isOddWeek;
    }
    public String getColor() {
        return color;
    }
}
