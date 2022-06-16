import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/** sceneController
 * 切换不同功能页面
 * @author Java小组
 */

public class sceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Variable for timer function
     * 用于实现切回Timer页面时计时仍在继续（创建的对象唯一，并且每次切回页面都在继续使用同一个对象）
     */
    static private Scene timerScene;
    static Parent timerRoot;
    static Boolean first=true;
    static Stopwatch stopwatch;
    static Timer timerObj;
    static int[] timerInt = {0,0};

    /** currentPage variable
     * To avoid reload same page
     * 1:calendar,2:timetable,3:notes,4:timer,5:game
     */
    private static int currentPage=0;

    /**
     * 读取各功能页面
     * @author Java小组
     * @param event
     */

    public void switchCalendar(ActionEvent event) throws IOException {
        if(currentPage!=1){
            root = FXMLLoader.load(getClass().getResource("fxml/calendar.fxml"));
            currentPage = 1;
            showScene(event,root);
            CalendarPanel cp = new CalendarPanel(root);
        }
    }

    public void switchTimer(ActionEvent event) throws IOException {
        if(currentPage!=4) {
            timerInt= new int[]{0, 0};
            if(first){
                timerRoot = FXMLLoader.load(getClass().getResource("fxml/timer.fxml"));
                timerScene = new Scene(timerRoot);
                stopwatch = new Stopwatch(timerRoot);
                timerObj = new Timer(timerRoot);
                first=false;
            }
            currentPage=4;
            timerShowScene(event, timerRoot);
            Button stopWatch = new Button();
            Button timer = new Button();
            timer = (Button)timerRoot.lookup("#timer");
            stopWatch = (Button)timerRoot.lookup("#stopwatch");

            stopWatch.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (timerInt[0]==0){
                        timerObj.changeScene();
                        stopwatch.setScene();
                        timerInt[0] = 1;
                    }
                }
            });
            timer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if(timerInt[1]==0){
                        stopwatch.changeScene();
                        timerObj.setScene();
                        timerInt[1] = 1;
                    }
                }
            });
        }
    }

    public void switchTimeTable(ActionEvent event) throws IOException {
        if(currentPage!=2) {
            root = FXMLLoader.load(getClass().getResource("fxml/time_table.fxml"));
            showScene(event, root);
            currentPage=2;
            TimeTable t = new TimeTable(root);

        }
    }

    public void switchNotes(ActionEvent event) throws IOException {
        if(currentPage!=3) {
            root = FXMLLoader.load(getClass().getResource("fxml/notes.fxml"));
            showScene(event, root);
            currentPage=3;
        }
    }
    public void switchGame(ActionEvent event) throws IOException {
        if (timerObj!=null) {
            //timerObj.changeScene();
        }

        if(currentPage!=5) {
            root = FXMLLoader.load(getClass().getResource("fxml/game.fxml"));
            showScene(event, root);
            currentPage=5;
        }
    }

    public void closeApp(ActionEvent event) throws IOException{
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Platform.exit();
    }

    public void showScene(ActionEvent event,Parent root)throws IOException{
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        AnchorPane leftPane = (AnchorPane) root.lookup("#leftPane");
        Clock clock = new Clock(leftPane);
        DragUtil.addDragListener(stage,root);
        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.show();
    }

    /** timerShowScene
     * 显示Timer页面，Timer的TimerScene唯一，每次切回Timer页面都是重新加载同一个Scene
     * @param event
     * @param root
     * @throws IOException
     */
    public void timerShowScene(ActionEvent event,Parent root)throws IOException{
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(timerScene);
        AnchorPane leftPane = (AnchorPane) root.lookup("#leftPane");
        Clock clock = new Clock(leftPane);
        DragUtil.addDragListener(stage,root);
        timerScene.setFill(Paint.valueOf("#ffffff00"));
        stage.show();
    }

}
