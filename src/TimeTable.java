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

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.LinkedList;

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
    private String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String[] periods = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};
    private String[] weeks = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private String[] weekChoices = {"All Week", "Odd Week", "Even Week"};
    private LinkedList<Course> courses = new LinkedList<>();

    public TimeTable(Parent root){
        GridPane tt = (GridPane) root.lookup("#timeTable");
        Button editTimeTable = (Button) root.lookup("#editTimeTable");
        Button nextWeek = (Button) root.lookup("#nextWeek");
        Button prevWeek = (Button) root.lookup("#prevWeek");

        today = Calendar.getInstance();
        dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        LocalDate todayDate = LocalDate.ofInstant(today.toInstant(), ZoneOffset.systemDefault());
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek+2);

        readTimeTable();
        weekNum = (int) ((todayDate.toEpochDay() - semesterStart.toEpochDay()) / 7) + 1;
        paint(root);

        editTimeTable.setOnMouseClicked(mouseEvent -> {
            editTimeTable(root);
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

    /**
     * 渲染课表
     * @param root
     */
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
            if(calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)){
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

        for(Course c : courses){
            if(weekNum >= c.getStartWeek() && weekNum <= c.getEndWeek()){
                if((isOddWeek && c.isOddWeek()) || (!isOddWeek && c.isEvenWeek())){
                    for(int j = c.getPeriodStart(); j <= c.getPeriodEnd(); j++){
                        Label label = new Label();
                        label.setFont(Font.font("DengXian",12));
                        label.setStyle("-fx-pref-width: 100;" + "-fx-pref-height: 100;" + "-fx-alignment: center;" + "-fx-background-color: " + c.getColor() + ";");
                        if(j == c.getPeriodStart()) label.setText(c.getName());
                        label.setOnMouseClicked(mouseEvent -> {
                            editCourse(root, c);
                        });
                        tt.add(label, c.getWeekDay(), j);
                    }
                }
            }
        }
    }

    /**
     * 修改课表
     * @param root
     */
    private void editTimeTable(Parent root){
        GridPane tt = (GridPane) root.lookup("#timeTable");
        DialogPane editPanel = (DialogPane) root.lookup("#editPanel");
        Button addCourse = (Button) root.lookup("#addCourse");

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) root.lookup("#semesterChoice");
        DatePicker startDate = (DatePicker) root.lookup("#startDate");
        DatePicker endDate = (DatePicker) root.lookup("#endDate");
        Button submitTimeTable = (Button) root.lookup("#submitTimeTable");
        Button cancel = (Button) root.lookup("#cancel");

        editPanel.setVisible(true);
        tt.setDisable(true);

        choiceBox.setItems(FXCollections.observableArrayList(allSemester));
        choiceBox.setValue(semester);
        startDate.setValue(semesterStart);
        endDate.setValue(semesterEnd);

        LinkedList<Course> listTemp = (LinkedList<Course>) courses.clone();
        paintCourseList(root, listTemp);

        addCourse.setOnMouseClicked(event -> {
            addCourse(root, listTemp);
        });

        submitTimeTable.setOnMouseClicked(event -> {
            semester = allSemester[choiceBox.getSelectionModel().selectedIndexProperty().intValue()];
            semesterStart = startDate.getValue();
            semesterEnd = endDate.getValue();
            courses = (LinkedList<Course>) listTemp.clone();
            writeTimeTable();

            editPanel.setVisible(false);
            tt.setDisable(false);
            tt.getChildren().removeIf(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) != 0);
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            paint(root);
        });

        cancel.setOnMouseClicked(event -> {
            editPanel.setVisible(false);
            tt.setDisable(false);
        });
    }

    /**
     * 用户新增课程
     * @param root
     * @param listTemp
     */
    private void addCourse(Parent root, LinkedList<Course> listTemp){
        DialogPane editPanel = (DialogPane) root.lookup("#editPanel");
        DialogPane newCourse = (DialogPane) root.lookup("#newCourse");
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

        newCourse.setVisible(true);
        editPanel.setVisible(false);

        dayOfWeek.setItems(FXCollections.observableArrayList(weekdays));
        startPeriod.setItems(FXCollections.observableArrayList(periods));
        endPeriod.setItems(FXCollections.observableArrayList(periods));
        startWeek.setItems(FXCollections.observableArrayList(weeks));
        endWeek.setItems(FXCollections.observableArrayList(weeks));
        weekChoice.setItems(FXCollections.observableArrayList(weekChoices));
        weekChoice.setValue(weekChoices[0]);

        submitNewCourse.setOnMouseClicked(eventHandle -> {
            if (name.getText().equals("") ||
                    dayOfWeek.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                    startPeriod.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                    endPeriod.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                    startWeek.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                    endWeek.getSelectionModel().selectedIndexProperty().intValue() == -1) {
                inputFail(root, "Add", "All fields can't be empty!", newCourse);
                return;
            }
            if(name.getText().contains(";")){
                inputFail(root, "Add", "Course name can't contain character \";\"", newCourse);
                return;
            }
            Course c = new Course(name.getText(), dayOfWeek.getSelectionModel().selectedIndexProperty().intValue() + 1,
                    startPeriod.getSelectionModel().selectedIndexProperty().intValue() + 1, endPeriod.getSelectionModel().selectedIndexProperty().intValue() + 1,
                    startWeek.getSelectionModel().selectedIndexProperty().intValue() + 1, endWeek.getSelectionModel().selectedIndexProperty().intValue() + 1,
                    (weekChoice.getSelectionModel().selectedIndexProperty().intValue() != 2), (weekChoice.getSelectionModel().selectedIndexProperty().intValue() != 1),
                    "#"+colorPicker.getValue().toString().substring(2));
            listTemp.add(c);
            paintCourseList(root, listTemp);

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
    }

    /**
     * 修改课程
     * @param root
     * @param c
     */
    private void editCourse(Parent root, Course c){
        GridPane tt = (GridPane) root.lookup("#timeTable");
        Button editTimeTable = (Button) root.lookup("#editTimeTable");
        DialogPane editCourse = (DialogPane) root.lookup("#editCourse");
        Button submitEditCourse = (Button) root.lookup("#submitEditCourse");
        Button cancelEdit = (Button) root.lookup("#cancelEdit");
        TextField name = (TextField) root.lookup("#courseNameEdit");
        ChoiceBox dayOfWeek = (ChoiceBox) root.lookup("#dayOfWeekEdit");
        ChoiceBox startPeriod = (ChoiceBox) root.lookup("#startPeriodEdit");
        ChoiceBox endPeriod = (ChoiceBox) root.lookup("#endPeriodEdit");
        ChoiceBox startWeek = (ChoiceBox) root.lookup("#startWeekEdit");
        ChoiceBox endWeek = (ChoiceBox) root.lookup("#endWeekEdit");
        ChoiceBox weekChoice = (ChoiceBox) root.lookup("#weekChoiceEdit");
        ColorPicker colorPicker = (ColorPicker) root.lookup("#courseColorEdit");

        editCourse.setVisible(true);
        tt.setDisable(true);
        editTimeTable.setDisable(true);

        name.setText(c.getName());
        dayOfWeek.setItems(FXCollections.observableArrayList(weekdays));
        dayOfWeek.setValue(weekdays[c.getWeekDay()-1]);
        startPeriod.setItems(FXCollections.observableArrayList(periods));
        startPeriod.setValue(periods[c.getPeriodStart()-1]);
        endPeriod.setItems(FXCollections.observableArrayList(periods));
        endPeriod.setValue(periods[c.getPeriodEnd()-1]);
        startWeek.setItems(FXCollections.observableArrayList(weeks));
        startWeek.setValue(weeks[c.getStartWeek()-1]);
        endWeek.setItems(FXCollections.observableArrayList(weeks));
        endWeek.setValue(weeks[c.getEndWeek()-1]);
        weekChoice.setItems(FXCollections.observableArrayList(weekChoices));
        if(c.isOddWeek() && c.isEvenWeek()) weekChoice.setValue(weekChoices[0]);
        else if(c.isOddWeek() && !c.isEvenWeek()) weekChoice.setValue(weekChoices[1]);
        else weekChoice.setValue(weekChoices[2]);
        colorPicker.setValue(Color.valueOf(c.getColor()));

        submitEditCourse.setOnMouseClicked(eventHandle -> {

            if (name.getText().equals("") ||
                    dayOfWeek.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                    startPeriod.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                    endPeriod.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                    startWeek.getSelectionModel().selectedIndexProperty().intValue() == -1 ||
                    endWeek.getSelectionModel().selectedIndexProperty().intValue() == -1) {
                inputFail(root, "Edit", "All fields can't be empty!", editCourse);
                return;
            }
            if(name.getText().contains(";")){
                inputFail(root, "Edit", "Course name can't contain character \";\"", editCourse);
                return;
            }
            Course newCourse = new Course(name.getText(), dayOfWeek.getSelectionModel().selectedIndexProperty().intValue() + 1,
                    startPeriod.getSelectionModel().selectedIndexProperty().intValue() + 1, endPeriod.getSelectionModel().selectedIndexProperty().intValue() + 1,
                    startWeek.getSelectionModel().selectedIndexProperty().intValue() + 1, endWeek.getSelectionModel().selectedIndexProperty().intValue() + 1,
                    (weekChoice.getSelectionModel().selectedIndexProperty().intValue() != 2), (weekChoice.getSelectionModel().selectedIndexProperty().intValue() != 1),
                    "#"+colorPicker.getValue().toString().substring(2));
            int index = courses.indexOf(c);
            courses.set(index, newCourse);
            tt.getChildren().removeIf(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) != 0);
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            paint(root);
            writeTimeTable();

            name.setText("");
            colorPicker.setValue(Color.WHITE);
            editCourse.setVisible(false);
            tt.setDisable(false);
            editTimeTable.setDisable(false);
        });
        cancelEdit.setOnMouseClicked(eventHandle ->{
            name.setText("");
            colorPicker.setValue(Color.WHITE);
            editCourse.setVisible(false);
            tt.setDisable(false);
            editTimeTable.setDisable(false);
        });
    }

    /**
     * 渲染课程列表
     * @param root
     * @param listTemp
     */
    private void paintCourseList(Parent root, LinkedList<Course> listTemp){
        VBox vBox1 = (VBox) root.lookup("#courseVBox1");
        VBox vBox2 = (VBox) root.lookup("#courseVBox2");
        vBox1.getChildren().clear();
        vBox2.getChildren().clear();
        int i = 0;
        for (Course c : listTemp) {
            if(i >= 10) vBox2.setVisible(true);
            Label label = new Label(c.getName());
            Button deleteBtn = new Button("X");
            label.setFont(Font.font("DengXian", 14));
            label.setStyle("-fx-pref-width: 150;" + "-fx-pref-height: 16;" + "-fx-padding: 1 5;");
            deleteBtn.setStyle("-fx-background-color: none;" + "-fx-text-fill: #903950;");
            deleteBtn.setCursor(Cursor.HAND);
            deleteBtn.setOnMouseClicked(event -> {
                listTemp.remove(c);
                paintCourseList(root, listTemp);
            });
            HBox hBox = new HBox(label, deleteBtn);
            if(i < 10) vBox1.getChildren().add(hBox);
            else vBox2.getChildren().add(hBox);
            i++;
        }
    }

    /**
     * 输入错误提示框
     * @param root
     * @param header
     * @param text
     * @param dp
     */
    private void inputFail(Parent root, String header, String text, DialogPane dp){
        DialogPane failDialog = (DialogPane) root.lookup("#failed");
        Label failTitle = (Label) root.lookup("#failTitle");
        Label failText = (Label) root.lookup("#failText");
        Button failOK = (Button) root.lookup("#failOK");
        failText.setText(text);
        failTitle.setText(header + " Course Failed");
        dp.setDisable(true);
        failDialog.setVisible(true);
        failOK.setOnMouseClicked(mouseEvent -> {
            failDialog.setVisible(false);
            dp.setDisable(false);
        });
    }

    /**
     * 将数据存入文件
     */
    private void writeTimeTable(){
        String filename = "src/data/timetable.csv";
        String csvDelimiter = ";";
        BufferedWriter bw;
        File file = new File(filename);
        if(file.exists()) file.delete();
        try {
            file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("sep=" + csvDelimiter);
            bw.newLine();
            bw.write(semester + csvDelimiter + semesterStart + csvDelimiter + semesterEnd);
            for(Course c : courses){
                String isOddWeek, isEvenWeek;
                if(c.isOddWeek()) isOddWeek = "t";
                else isOddWeek = "f";
                if(c.isEvenWeek()) isEvenWeek = "t";
                else isEvenWeek = "f";
                bw.newLine();
                bw.write(c.getName() + csvDelimiter + c.getWeekDay() + csvDelimiter + c.getPeriodStart() + csvDelimiter + c.getPeriodEnd() + csvDelimiter + c.getStartWeek() + csvDelimiter + c.getEndWeek() + csvDelimiter + isOddWeek + csvDelimiter + isEvenWeek + csvDelimiter + c.getColor());
            }
            bw.flush();
            bw.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 从文件中读取数据
     */
    private void readTimeTable(){
        String filename = "src/data/timetable.csv";
        BufferedReader br = null;
        String str;
        String csvDelimiter = ";";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            br = new BufferedReader(new FileReader(filename));
            br.readLine();  //去掉第一行
            str = br.readLine();
            String[] temp = str.split(csvDelimiter);
            semester = temp[0];
            semesterStart = LocalDate.parse(temp[1], dateFormat);
            semesterEnd = LocalDate.parse(temp[2], dateFormat);
            while((str=br.readLine()) != null){
                temp = str.split(csvDelimiter);
                Course c = new Course(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), temp[6].equals("t"), temp[7].equals("t"), temp[8]);
                courses.add(c);
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(br != null){
                try{
                    br.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
