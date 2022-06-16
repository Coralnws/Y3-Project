import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;

public class TimeTable {
    private Calendar calendar;
    private Calendar today;
    private int dayOfWeek;
    private int weekNum;
    private LocalDate semesterStart;
    private LocalDate semesterEnd;
    private String semester;
    private String[] allSemester = {"AUTUMN", "WINTER", "SPRING", "SUMMER"};
    private String[] allMonth = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    private String[] allWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    private Course[] course = new Course[30];
    private int courseCount = 0;

    public TimeTable(Parent root){
        GridPane tt = (GridPane) root.lookup("#timeTable");
        Button editTimeTable = (Button) root.lookup("#editTimeTable");
        DialogPane editPanel = (DialogPane) root.lookup("#editPanel");
        DialogPane newCourse = (DialogPane) root.lookup("#newCourse");
        VBox vBox = (VBox) root.lookup("#courseVBox");
        Button addCourse = (Button) root.lookup("#addCourse");
        Button nextWeek = (Button) root.lookup("#nextWeek");
        Button prevWeek = (Button) root.lookup("#prevWeek");

        semester = allSemester[2];
        semesterStart = LocalDate.of(2022, 2, 28);
        semesterEnd = LocalDate.of(2022, 7, 3);

        today = Calendar.getInstance();
        dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        LocalDate todayDate = LocalDate.ofInstant(today.toInstant(), ZoneOffset.systemDefault());
        weekNum = (int) ((todayDate.toEpochDay() - semesterStart.toEpochDay()) / 7) + 1;

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek+2);

        course[0] = new Course("course1", 2, 1, 2, 3, 16, true, true, Color.WHITE);
        course[1] = new Course("course2", 3, 1, 2, 3, 16, false, true, Color.BISQUE);
        course[2] = new Course("course33333", 5, 5, 6, 3, 16, true, false, Color.LAVENDER);
        course[3] = new Course("course4", 2, 7, 9, 3, 9, true, true, Color.FLORALWHITE);
        courseCount+=4;

        paint(root);

        editTimeTable.setOnMouseClicked(mouseEvent -> {
            editPanel.setVisible(true);
            tt.setDisable(true);

            ChoiceBox<String> choiceBox = (ChoiceBox<String>) root.lookup("#semesterChoice");
            DatePicker startDate = (DatePicker) root.lookup("#startDate");
            DatePicker endDate = (DatePicker) root.lookup("#endDate");
            Button submitTimeTable = (Button) root.lookup("#submitTimeTable");
            Button cancel = (Button) root.lookup("#cancel");
            choiceBox.setItems(FXCollections.observableArrayList(allSemester));
            choiceBox.setValue(semester);
            startDate.setValue(semesterStart);
            endDate.setValue(semesterEnd);
            Course[] courseTemp = course.clone();

            for(int i = 0; i < courseCount; i++){
                int index = i;
                Label label = new Label(course[i].getName());
                Button deleteBtn = new Button("X");
                label.setFont(Font.font("DengXian",16));
                label.setStyle("-fx-pref-width: 100;" + "-fx-padding: 1 5;");
                deleteBtn.setStyle("-fx-background-color: none;" + "-fx-text-fill: #903950;");
                deleteBtn.setCursor(Cursor.HAND);
                deleteBtn.setOnMouseClicked(event -> {
                    int x = deleteCourse(course[index], courseTemp);
                    vBox.getChildren().remove(x);
                });
                HBox hBox = new HBox(label, deleteBtn);
                vBox.getChildren().add(hBox);
            }

            addCourse.setOnMouseClicked(event -> {
                newCourse.setVisible(true);
                editPanel.setVisible(false);

                Button submitNewCourse = (Button) root.lookup("#submitNewCourse");
                Button cancelAdd = (Button) root.lookup("#cancelAdd");
                TextField name = (TextField) root.lookup("#courseName");
                ChoiceBox dayOfWeek = (ChoiceBox) root.lookup("#dayOfWeek");
                ChoiceBox startPeriod = (ChoiceBox) root.lookup("#startPeriod");
                ChoiceBox endPeriod = (ChoiceBox) root.lookup("#endPeriod");
                ChoiceBox startWeek = (ChoiceBox) root.lookup("#startWeek");
                ChoiceBox endWeek = (ChoiceBox) root.lookup("#endWeek");
                ChoiceBox weekChoice = (ChoiceBox) root.lookup("#weekChoice");
                ColorPicker colorPicker = (ColorPicker) root.lookup("#courseColor");
                String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                String[] periods = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};
                String[] weeks = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
                String[] weekChoices = {"All Week", "Odd Week", "Even Week"};

                dayOfWeek.setItems(FXCollections.observableArrayList(weekdays));
                startPeriod.setItems(FXCollections.observableArrayList(periods));
                endPeriod.setItems(FXCollections.observableArrayList(periods));
                startWeek.setItems(FXCollections.observableArrayList(weeks));
                endWeek.setItems(FXCollections.observableArrayList(weeks));
                weekChoice.setItems(FXCollections.observableArrayList(weekChoices));
                weekChoice.setValue(weekChoices[0]);

                System.out.println(colorPicker.getValue());

                submitNewCourse.setOnMouseClicked(eventHandle -> {
                    if (name.getText().equals("") ||
                            dayOfWeek.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                            startPeriod.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                            endPeriod.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                            startWeek.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                            endWeek.getSelectionModel().selectedIndexProperty().intValue() == -1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Add Course Failed");
                        alert.setContentText("Every field can't be empty!");
                        alert.showAndWait();
                        return;
                    }
                    Course c = new Course(name.getText(), dayOfWeek.getSelectionModel().selectedIndexProperty().intValue() + 1,
                            startPeriod.getSelectionModel().selectedIndexProperty().intValue() + 1, endPeriod.getSelectionModel().selectedIndexProperty().intValue() + 1,
                            startWeek.getSelectionModel().selectedIndexProperty().intValue() + 1, endWeek.getSelectionModel().selectedIndexProperty().intValue() + 1,
                            (weekChoice.getSelectionModel().selectedIndexProperty().intValue() != 2), (weekChoice.getSelectionModel().selectedIndexProperty().intValue() != 1),
                            colorPicker.getValue());
                    int i;
                    for(i = 0; courseTemp[i]!=null;i++);
                    courseTemp[i] = c;

                    Label label = new Label(c.getName());
                    Button deleteBtn = new Button("X");
                    label.setFont(Font.font("DengXian",16));
                    label.setStyle("-fx-pref-width: 100;" + "-fx-padding: 1 5;");
                    deleteBtn.setStyle("-fx-background-color: none;" + "-fx-text-fill: #903950;");
                    deleteBtn.setCursor(Cursor.HAND);
                    deleteBtn.setOnMouseClicked(ev -> {
                        int x = deleteCourse(c, courseTemp);
                        vBox.getChildren().remove(x);
                    });
                    HBox hBox = new HBox(label, deleteBtn);
                    vBox.getChildren().add(hBox);

                    name.setText("");
                    colorPicker.setValue(Color.WHITE);
                    newCourse.setVisible(false);
                    editPanel.setVisible(true);
                });
                cancelAdd.setOnMouseClicked(eventHandle ->{
                    name.setText("");
                    colorPicker.setValue(Color.WHITE);
                    newCourse.setVisible(false);
                    editPanel.setVisible(true);
                });
            });

            submitTimeTable.setOnMouseClicked(event -> {
                semester = allSemester[choiceBox.getSelectionModel().selectedIndexProperty().intValue()];
                semesterStart = startDate.getValue();
                semesterEnd = endDate.getValue();
                course = courseTemp.clone();
                for(courseCount = 0; course[courseCount] != null; courseCount++);

                editPanel.setVisible(false);
                tt.setDisable(false);
                vBox.getChildren().clear();
                tt.getChildren().removeIf(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) != 0);
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                paint(root);
            });

            cancel.setOnMouseClicked(event -> {
                editPanel.setVisible(false);
                tt.setDisable(false);
                vBox.getChildren().clear();
            });
        });

        nextWeek.setOnMouseClicked(mouseEvent -> {
            weekNum ++;
            tt.getChildren().removeIf(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) != 0);
            paint(root);
        });
        prevWeek.setOnMouseClicked(mouseEvent -> {
            weekNum--;
            calendar.add(Calendar.DAY_OF_MONTH, -14);
            tt.getChildren().removeIf(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) != 0);
            paint(root);
        });
    }
    public void paint(Parent root){
        GridPane tt = (GridPane) root.lookup("#timeTable");
        Text month = (Text) root.lookup("#month");
        Text title = (Text) root.lookup("#timeTableTitle");
        boolean isOddWeek = weekNum % 2 != 0;

        title.setText(semesterStart.getYear() + " " + semester + "  |  WEEK " + weekNum);
        month.setText(allMonth[calendar.get(Calendar.MONTH)]);

        //填写日期栏
        for(int i = 0; i < 7; i++){
            Label label1 = new Label(allWeek[i]);
            Label label2 = new Label(calendar.get(Calendar.DAY_OF_MONTH) + "");
            label1.setFont(Font.font("DengXian",12));
            label2.setFont(Font.font("DengXian",12));
            if(calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)){
                label1.setStyle("-fx-pref-width: 100;" + "-fx-alignment: center;");
                label2.setStyle("-fx-pref-width: 100;" + "-fx-alignment: center;");
            }
            else {
                label1.setStyle("-fx-pref-width: 100;" + "-fx-alignment: center;" + "-fx-text-fill: #888888");
                label2.setStyle("-fx-pref-width: 100;" + "-fx-alignment: center;" + "-fx-text-fill: #888888");
            }
            VBox vBox = new VBox(label1, label2);
            tt.add(vBox, i + 1, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //填写对应课程
        for(int i = 0; i < courseCount; i++){
            if(weekNum >= course[i].getStartWeek() && weekNum <= course[i].getEndWeek()){
                if((isOddWeek && course[i].isOddWeek()) || (!isOddWeek && course[i].isEvenWeek())){
                    for(int j = course[i].getPeriodStart(); j <= course[i].getPeriodEnd(); j++){
                        Label label = new Label();
                        label.setFont(Font.font("DengXian",12));
                        label.setStyle("-fx-pref-width: 100;" + "-fx-pref-height: 100;" + "-fx-alignment: center;" + "-fx-background-color: #" + course[i].getColor().toString().substring(2) + ";");
                        if(j == course[i].getPeriodStart()) label.setText(course[i].getName());
                        tt.add(label, course[i].getWeekDay(), j);
                    }
                }
            }
        }
    }
    public int deleteCourse(Course c, Course[] arr){
        int i, j;
        for(i = 0; !arr[i].equals(c); i++);
        for(j = i; arr[j + 1] != null; j++) arr[j] = arr[j + 1];
        arr[j] = null;
        return i;
    }
}
