import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

/** Calendar class
 * 展示日历
 * @author Ly
 */
public class CalendarPanel {
    Calendar calendar;
    Calendar calendarNow;
    Calendar today;
    private String[] allMonth = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    private Schedule[] schedules = new Schedule[1000];
    private int scheduleCount = 0;

    public CalendarPanel(Parent root){
        GridPane cp = (GridPane) root.lookup("#calendarTable");
        Button nextMonth = (Button) root.lookup("#nextMonth");
        Button prevMonth = (Button) root.lookup("#prevMonth");
        Button addSchedule = (Button) root.lookup("#addSchedule");
        DialogPane newSchedule = (DialogPane) root.lookup("#newSchedule");
        VBox scheduleList = (VBox) root.lookup("#scheduleList");

        today = Calendar.getInstance();
        calendar = Calendar.getInstance();
        calendarNow = Calendar.getInstance();
        calendarNow.set(Calendar.DAY_OF_MONTH, 1);
        schedules[0] = new Schedule("test1", LocalDate.of(2022, 4, 25), LocalDate.of(2022, 5, 9));
        schedules[1] = new Schedule("test2", LocalDate.of(2022, 5, 24), LocalDate.of(2022, 6, 9));
        schedules[2] = new Schedule("test3", LocalDate.of(2022, 6, 2), LocalDate.of(2022, 6, 11));
        schedules[3] = new Schedule("test4", LocalDate.of(2022, 6, 4), LocalDate.of(2022, 6, 15));
        schedules[4] = new Schedule("test5", LocalDate.of(2022, 6, 9), LocalDate.of(2022, 6, 9));
        schedules[5] = new Schedule("test6", LocalDate.of(2022, 6, 19), LocalDate.of(2022, 7, 20));
        schedules[6] = new Schedule("test7", LocalDate.of(2022, 6, 20), LocalDate.of(2022, 6, 24));
        schedules[7] = new Schedule("test7", LocalDate.of(2022, 6, 20), LocalDate.of(2022, 6, 24));
        schedules[8] = new Schedule("test7", LocalDate.of(2022, 6, 20), LocalDate.of(2022, 6, 24));
        scheduleCount+=9;

        paint(root);
        paintSchedule(root);

        prevMonth.setOnMouseClicked(mouseEvent -> {
            cp.getChildren().remove(7, 49);
            calendarNow.add(Calendar.MONTH, -1);
            paint(root);
        });
        nextMonth.setOnMouseClicked(mouseEvent -> {
            cp.getChildren().remove(7, 49);
            calendarNow.add(Calendar.MONTH, 1);
            paint(root);
        });

        addSchedule.setOnMouseClicked(mouseEvent -> {
            newSchedule.setVisible(true);
            cp.setDisable(true);
            Button cancel = (Button) root.lookup("#cancel");
            Button setSchedule = (Button) root.lookup("#setSchedule");
            TextField title = (TextField) root.lookup("#title");
            DatePicker startDatePicker = (DatePicker) root.lookup("#startDate");
            DatePicker endDatePicker = (DatePicker) root.lookup("#endDate");

            setSchedule.setOnMouseClicked(event ->{
                if(title.getText().equals("")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Add Schedule Failed");
                    alert.setContentText("Title field can't be empty!");
                    alert.showAndWait();
                    return;
                }
                if(startDatePicker.getValue() == null || endDatePicker.getValue() == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Add Schedule Failed");
                    alert.setContentText("Date field can't be empty!");
                    alert.showAndWait();
                    return;
                }
                Schedule sch = new Schedule(title.getText(), startDatePicker.getValue(), endDatePicker.getValue());
                insertSchedule(sch);
                title.setText("");
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                newSchedule.setVisible(false);
                cp.setDisable(false);
                paint(root);
            });
            cancel.setOnMouseClicked(event ->{
                title.setText("");
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                newSchedule.setVisible(false);
                cp.setDisable(false);
            });
        });
    }

    public void paintSchedule(Parent root){
        VBox scheduleList = (VBox) root.lookup("#scheduleList");
        LocalDate todayDate = LocalDate.ofInstant(today.toInstant(), ZoneOffset.systemDefault());
        for(int i = 0; i < scheduleCount; i++){
            if(schedules[i].getEndDate().isBefore(todayDate)) continue;
            Label label = new Label(schedules[i].getTitle());
            label.setFont(Font.font("DengXian",18));
            label.setStyle("-fx-pref-width: 200;" + "-fx-pref-height: 26;" + "-fx-text-fill: #EEEEEE;" + "-fx-padding: 1 5;");
            scheduleList.getChildren().add(label);
        }
    }

    /**
     * paint
     * 渲染当前日历
     * @param root
     */
    public void paint(Parent root){
        GridPane cp = (GridPane) root.lookup("#calendarTable");
        Text title = (Text) root.lookup("#calendarTitle");

        title.setText(allMonth[calendarNow.get(Calendar.MONTH)] + "  |  " + calendarNow.get(Calendar.YEAR));

        calendar.set(calendarNow.get(Calendar.YEAR),calendarNow.get(Calendar.MONTH), 1);
        calendar.add(Calendar.DAY_OF_MONTH, -(calendar.get(Calendar.DAY_OF_WEEK)-1));
        LocalDate firstDisplayDay = LocalDate.ofInstant(calendar.toInstant(), ZoneOffset.systemDefault());

        boolean[] isThisMonth = new boolean[49];

        //填写日期
        for(int i = 7; i < 49; i++){
            AnchorPane ap = new AnchorPane();
            Label date = new Label(calendar.get(Calendar.DAY_OF_MONTH) + "");
            date.setFont(Font.font("DengXian",16));

            if(i % 7 == 0 || i % 7 == 6) date.setStyle("-fx-text-fill:  #903950;" + "-fx-pref-width: 100;" + "-fx-alignment: top-right;" + "-fx-translate-y: -26;"+ "-fx-padding: 2;");
            else date.setStyle("-fx-pref-width: 100;" + "-fx-alignment: top-right;" + "-fx-translate-y: -26;"+ "-fx-padding: 2;");

            if(calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)){
                ap.setStyle("-fx-background-color: #F5C9C9;" + "-fx-pref-width: 100;" + "-fx-pref-height: 100;");
            }
            else{
                ap.setStyle("-fx-background-color: #F5E0E0;" + "-fx-pref-width: 100;" + "-fx-pref-height: 100;");
            }

            if(calendar.get(Calendar.MONTH) == calendarNow.get(Calendar.MONTH)){
                isThisMonth[i] = true;
            }
            else date.setDisable(true);
            cp.add(ap, i % 7, i / 7);
            cp.add(date, i % 7, i / 7);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        LocalDate lastDisplayDay = LocalDate.ofInstant(calendar.toInstant(), ZoneOffset.systemDefault());

        //填写日程
        boolean[][] dayScheduleCount = new boolean[49][3];
        for(int i = 0; i < scheduleCount; i++){
            if(schedules[i].getEndDate().isBefore(firstDisplayDay) || schedules[i].getStartDate().isAfter(lastDisplayDay)){
                continue;
            }

            int startIndex, length;

            //若起始天是否可视
            if(!schedules[i].getStartDate().isAfter(firstDisplayDay)){
                startIndex = 7;
                length = (int)(schedules[i].getEndDate().toEpochDay() - firstDisplayDay.toEpochDay());
            }
            else {
                startIndex = (int)(schedules[i].getStartDate().toEpochDay() - firstDisplayDay.toEpochDay()) + 7;
                length = (int)(schedules[i].getEndDate().toEpochDay() - schedules[i].getStartDate().toEpochDay());
            }

            int schedulePlace = -1;
            for(int n = 0; n < 3; n++){
                for (int k = startIndex; k <= startIndex + length && k < 49; k++) {
                    if (dayScheduleCount[k][n]) break;
                    if (k == startIndex + length || k == 48) schedulePlace = n;
                }
                if (schedulePlace != -1) break;
            }
            if (schedulePlace == -1) continue;

            for (int k = startIndex; k <= startIndex + length && k < 49; k++) {
                Label label = new Label(schedules[i].getTitle());
                label.setFont(Font.font("DengXian", 12));
                switch (schedulePlace) {
                    case 0:
                        label.setStyle("-fx-pref-width: 100;" + "-fx-translate-y: -7;" + "-fx-background-color: #CBCBCB;" + "-fx-label-padding: 0 0 0 3");
                        break;
                    case 1:
                        label.setStyle("-fx-pref-width: 100;" + "-fx-translate-y: 9;" + "-fx-background-color: #E5C6F2;" + "-fx-label-padding: 0 0 0 3");
                        break;
                    case 2:
                        label.setStyle("-fx-pref-width: 100;" + "-fx-translate-y: 25;" + "-fx-background-color: #EDD690;" + "-fx-label-padding: 0 0 0 3");
                        break;
                }
                if(!isThisMonth[k]) label.setDisable(true);
                cp.add(label, k % 7, k / 7);
                dayScheduleCount[k][schedulePlace] = true;
            }
        }
    }

    public void insertSchedule(Schedule s){
        Schedule temp;
        schedules[scheduleCount++] = s;
        for(int i = scheduleCount - 2; i >= 0; i--){
            if(schedules[i].getStartDate().isAfter(s.getStartDate())){
                temp = schedules[i];
                schedules[i] = schedules[i+1];
                schedules[i+1] = temp;
            }
            else break;
        }
    }
}
