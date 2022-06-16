import java.io.*;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** Calendar class
 * 展示日历
 * @author Ly
 */
public class CalendarPanel {
    Calendar calendar;
    Calendar calendarNow;
    Calendar today;
    private String[] weekdays = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    private String[] allMonth = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    private LinkedList<Schedule> schedules = new LinkedList<>();

    public CalendarPanel(Parent root){
        Button nextMonth = (Button) root.lookup("#nextMonth");
        Button prevMonth = (Button) root.lookup("#prevMonth");
        Button addSchedule = (Button) root.lookup("#addSchedule");

        today = Calendar.getInstance();
        calendar = Calendar.getInstance();
        calendarNow = Calendar.getInstance();
        calendarNow.set(Calendar.DAY_OF_MONTH, 1);

        readSchedule();
        paint(root);
        paintSchedule(root);

        writeSchedule();

        prevMonth.setOnMouseClicked(mouseEvent -> {
            calendarNow.add(Calendar.MONTH, -1);
            paint(root);
        });
        nextMonth.setOnMouseClicked(mouseEvent -> {
            calendarNow.add(Calendar.MONTH, 1);
            paint(root);
        });
        addSchedule.setOnMouseClicked(mouseEvent -> addSchedule(root));
    }

    /**
     * 渲染左侧schedule list
     * @param root
     */
    public void paintSchedule(Parent root){
        VBox scheduleList = (VBox) root.lookup("#scheduleList");
        LocalDate todayDate = LocalDate.ofInstant(today.toInstant(), ZoneOffset.systemDefault());

        scheduleList.getChildren().clear();
        Label l = new Label("Schedule List");
        l.setFont(Font.font("DengXian", 20));
        l.setStyle("-fx-pref-width: 200;" + "-fx-pref-height: 22;" + "-fx-background-color: #222222;" + "-fx-text-fill: #FFFFFF;" + "-fx-alignment: center;");
        scheduleList.getChildren().add(l);
        int i = 0;
        for(Schedule s : schedules){
            if(s.getEndDate().isBefore(todayDate)) continue;
            if(i == 15) break;
            Label label = new Label(s.getTitle());
            label.setFont(Font.font("DengXian",16));
            if(todayDate.isAfter(s.getStartDate()))
                label.setStyle("-fx-pref-width: 200;" + "-fx-pref-height: 20;" + "-fx-background-color: #AAAAAA;" + "-fx-text-fill: #000000;" + "-fx-padding: 1 5;");
            else label.setStyle("-fx-pref-width: 200;" + "-fx-pref-height: 20;" + "-fx-text-fill: #EEEEEE;" + "-fx-padding: 1 5;");

            label.setOnMouseClicked(mouseEvent -> {
                editSchedule(root, s);
            });
            scheduleList.getChildren().add(label);
            i++;
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

        cp.getChildren().clear();
        boolean[] isThisMonth = new boolean[49];

        //填写header
        for(int i = 0; i < 7; i++){
            Label label = new Label(weekdays[i]);
            label.setFont(Font.font("DengXian",12));
            label.setStyle("-fx-pref-width: 100;" + "-fx-pref-height: 100;" + "-fx-alignment: center;" + "-fx-background-color: #903950;" + "-fx-text-fill: #FFFFFF;");
            cp.add(label, i, 0);
        }
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
        boolean[][] dayScheduleCount = new boolean[49][4];
        for(Schedule s : schedules){
            if(s.getEndDate().isBefore(firstDisplayDay) || s.getStartDate().isAfter(lastDisplayDay)){
                continue;
            }

            int startIndex, length;

            // 若起始天是否可视
            if(!s.getStartDate().isAfter(firstDisplayDay)){
                startIndex = 7;
                length = (int)(s.getEndDate().toEpochDay() - firstDisplayDay.toEpochDay());
            }
            else {
                startIndex = (int)(s.getStartDate().toEpochDay() - firstDisplayDay.toEpochDay()) + 7;
                length = (int)(s.getEndDate().toEpochDay() - s.getStartDate().toEpochDay());
            }

            //查找schedule条位置
            int schedulePlace = -1;
            for(int n = 0; n < 4; n++){
                for (int k = startIndex; k <= startIndex + length && k < 49; k++) {
                    if (dayScheduleCount[k][n]) break;
                    if (k == startIndex + length || k == 48) schedulePlace = n;
                }
                if (schedulePlace != -1) break;
            }
            if (schedulePlace == -1) continue;

            for (int k = startIndex; k <= startIndex + length && k < 49; k++) {
                Label label = new Label(s.getTitle());
                if(k!=startIndex) label.setText("");
                label.setFont(Font.font("DengXian", 9));
                switch (schedulePlace) {
                    case 0:
                        label.setStyle("-fx-pref-width: 100;" + "-fx-translate-y: -10;" + "-fx-background-color: " + s.getColor() + ";" + "-fx-text-fill:  " + s.getTextColor() + ";" + "-fx-label-padding: 0 0 0 3;");
                        break;
                    case 1:
                        label.setStyle("-fx-pref-width: 100;" + "-fx-translate-y: 2;" + "-fx-background-color: " + s.getColor() + ";" + "-fx-text-fill:  " + s.getTextColor() + ";" + "-fx-label-padding: 0 0 0 3;");
                        break;
                    case 2:
                        label.setStyle("-fx-pref-width: 100;" + "-fx-translate-y: 14;" + "-fx-background-color: " + s.getColor() + ";" + "-fx-text-fill:  " + s.getTextColor() + ";" + "-fx-label-padding: 0 0 0 3;");
                        break;
                    case 3:
                        label.setStyle("-fx-pref-width: 100;" + "-fx-translate-y: 26;" + "-fx-background-color: " + s.getColor() + ";" + "-fx-text-fill:  " + s.getTextColor() + ";" + "-fx-label-padding: 0 0 0 3;");
                        break;
                }
                label.setOnMouseClicked(mouseEvent -> {
                    editSchedule(root, s);
                });
                if(!isThisMonth[k]) label.setDisable(true);
                cp.add(label, k % 7, k / 7);
                dayScheduleCount[k][schedulePlace] = true;
            }
        }
    }

    /**
     * 用户新建schedule
     * @param root
     */
    private void addSchedule(Parent root){
        GridPane cp = (GridPane) root.lookup("#calendarTable");
        Button addSchedule = (Button) root.lookup("#addSchedule");
        VBox scheduleList = (VBox) root.lookup("#scheduleList");
        DialogPane newSchedule = (DialogPane) root.lookup("#newSchedule");
        Button cancel = (Button) root.lookup("#cancel");
        Button setSchedule = (Button) root.lookup("#setSchedule");

        TextField title = (TextField) root.lookup("#title");
        DatePicker startDatePicker = (DatePicker) root.lookup("#startDate");
        DatePicker endDatePicker = (DatePicker) root.lookup("#endDate");
        ColorPicker scheduleColor = (ColorPicker) root.lookup("#scheduleColor");
        ColorPicker textColor = (ColorPicker) root.lookup("#textColor");
        textColor.setValue(Color.BLACK);

        newSchedule.setVisible(true);
        cp.setDisable(true);
        addSchedule.setDisable(true);
        scheduleList.setDisable(true);

        setSchedule.setOnMouseClicked(event ->{
            if(title.getText().equals("") || startDatePicker.getValue() == null || endDatePicker.getValue() == null){
                inputFail(root, "Add", "Title and Date can't be empty!", newSchedule);
                return;
            }
            if(title.getText().contains(";")){
                inputFail(root, "Add", "Title can't contain character \";\"", newSchedule);
                return;
            }
            Schedule sch = new Schedule(title.getText(), startDatePicker.getValue(), endDatePicker.getValue(), "#"+scheduleColor.getValue().toString().substring(2), "#"+textColor.getValue().toString().substring(2));
            insertSchedule(sch);

            title.setText("");
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            newSchedule.setVisible(false);
            cp.setDisable(false);
            addSchedule.setDisable(false);
            scheduleList.setDisable(false);
            paint(root);
            paintSchedule(root);
        });
        cancel.setOnMouseClicked(event ->{
            title.setText("");
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            newSchedule.setVisible(false);
            cp.setDisable(false);
            addSchedule.setDisable(false);
            scheduleList.setDisable(false);
        });
    }

    public void editSchedule(Parent root, Schedule s){
        GridPane cp = (GridPane) root.lookup("#calendarTable");
        Button addSchedule = (Button) root.lookup("#addSchedule");
        VBox scheduleList = (VBox) root.lookup("#scheduleList");
        DialogPane editPanel = (DialogPane) root.lookup("#editSchedule");
        Button cancelEdit = (Button) root.lookup("#cancelEdit");
        Button saveEdit = (Button) root.lookup("#saveEdit");
        Button deleteSchedule = (Button) root.lookup("#deleteSchedule");

        TextField title = (TextField) root.lookup("#titleEdit");
        DatePicker startDatePicker = (DatePicker) root.lookup("#startDateEdit");
        DatePicker endDatePicker = (DatePicker) root.lookup("#endDateEdit");
        ColorPicker scheduleColor = (ColorPicker) root.lookup("#scheduleColorEdit");
        ColorPicker textColor = (ColorPicker) root.lookup("#textColorEdit");
        title.setText(s.getTitle());
        startDatePicker.setValue(s.getStartDate());
        endDatePicker.setValue(s.getEndDate());
        scheduleColor.setValue(Color.valueOf(s.getColor()));
        textColor.setValue(Color.valueOf(s.getTextColor()));

        editPanel.setVisible(true);
        cp.setDisable(true);
        addSchedule.setDisable(true);
        scheduleList.setDisable(true);

        saveEdit.setOnMouseClicked(mouseEvent -> {
            if(title.getText().equals("") || startDatePicker.getValue() == null || endDatePicker.getValue() == null){
                inputFail(root, "Edit", "Title and Date can't be empty!", editPanel);
                return;
            }
            if(title.getText().contains(";")){
                inputFail(root, "Edit", "Title can't contain character \";\"", editPanel);
                return;
            }
            Schedule sch = new Schedule(title.getText(), startDatePicker.getValue(), endDatePicker.getValue(), "#"+scheduleColor.getValue().toString().substring(2), "#"+textColor.getValue().toString().substring(2));
            schedules.remove(s);
            insertSchedule(sch);
            paint(root);
            paintSchedule(root);
            writeSchedule();

            editPanel.setVisible(false);
            cp.setDisable(false);
            addSchedule.setDisable(false);
            scheduleList.setDisable(false);
        });
        deleteSchedule.setOnMouseClicked(mouseEvent -> {
            removeSchedule(root, s);
            editPanel.setVisible(false);
            cp.setDisable(false);
            addSchedule.setDisable(false);
            scheduleList.setDisable(false);
        });
        cancelEdit.setOnMouseClicked(mouseEvent -> {
            editPanel.setVisible(false);
            cp.setDisable(false);
            addSchedule.setDisable(false);
            scheduleList.setDisable(false);
        });
    }

    /**
     * 插入schedule
     * @param schedule
     */
    public void insertSchedule(Schedule schedule){
        int i = 0;
        for(Schedule s : schedules){
            if(s.getStartDate().isAfter(schedule.getStartDate())) break;
            i++;
        }
        schedules.add(i, schedule);
        writeSchedule();
    }

    /**
     * 移除schedule
     * @param root
     * @param s
     */
    public void removeSchedule(Parent root, Schedule s){
        GridPane cp = (GridPane) root.lookup("#calendarTable");
        DialogPane deleteConfirmation = (DialogPane) root.lookup("#deleteConfirmation");
        Label confirmationText = (Label) root.lookup("#confirmationText");
        Button confirm = (Button) root.lookup("#sureDelete");
        Button cancel = (Button) root.lookup("#cancelDelete");

        confirmationText.setText("Are you sure to delete schedule \"" + s.getTitle() + "\"?");
        deleteConfirmation.setVisible(true);
        cp.setDisable(true);
        confirm.setOnMouseClicked(event -> {
            schedules.remove(s);
            deleteConfirmation.setVisible(false);
            cp.setDisable(false);
            paintSchedule(root);
            paint(root);
            writeSchedule();
        });
        cancel.setOnMouseClicked(event -> {
            deleteConfirmation.setVisible(false);
            cp.setDisable(false);
        });
    }

    /**
     * 输入错误提示框
     * @param root
     * @param header
     * @param text
     * @param dp
     */
    private void inputFail(Parent root, String header, String text, DialogPane dp){
        DialogPane failPanel = (DialogPane) root.lookup("#failed");
        Label failTitle = (Label) root.lookup("#failTitle");
        Label failText = (Label) root.lookup("#failText");
        Button failOK = (Button) root.lookup("#failOK");
        failText.setText(text);
        failTitle.setText(header + " Schedule Failed");
        failPanel.setVisible(true);
        dp.setDisable(true);

        failOK.setOnMouseClicked(mouseEvent -> {
            failPanel.setVisible(false);
            dp.setDisable(false);
        });
    }

    /**
     * 将数据存入文件
     */
    private void writeSchedule(){
        String filename = "src/data/schedule.csv";
        String csvDelimiter = ";";
        BufferedWriter bw;
        File file = new File(filename);
        if(file.exists()) file.delete();
        try {
            file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("sep=" + csvDelimiter);
            for(Schedule s : schedules){
                bw.newLine();
                bw.write(s.getTitle() + csvDelimiter + s.getStartDate() + csvDelimiter + s.getEndDate() + csvDelimiter + s.getColor() + csvDelimiter + s.getTextColor());
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
    private void readSchedule(){
        String filename = "src/data/schedule.csv";
        BufferedReader br = null;
        String str;
        String csvDelimiter = ";";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            br = new BufferedReader(new FileReader(filename));
            br.readLine();  //去掉第一行
            while((str=br.readLine()) != null){
                String[] temp = str.split(csvDelimiter);
                Schedule sch = new Schedule(temp[0], LocalDate.parse(temp[1], dateFormat), LocalDate.parse(temp[2], dateFormat), temp[3], temp[4]);
                schedules.add(sch);
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
