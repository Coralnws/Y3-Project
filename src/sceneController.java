
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;

/** sceneController
 * 切换不同功能页面
 * @author Java小组
 */

public class sceneController {

    @FXML
    ImageView close;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public Button button1;

    /** currentPage variable
     * To avoid reload same page
     * 1:calendar,2:timetable,3:notes,4:timer,5:game
     */
    private static int currentPage=0;
    private static boolean flag = false;

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
        }
    }

    public void switchTimer(ActionEvent event) throws IOException {
        if(currentPage!=4) {
            root = FXMLLoader.load(getClass().getResource("fxml/timer.fxml"));
            currentPage=4;
            showScene(event, root);
            Button stopWatch = new Button();
            Button timer = new Button();
            timer = (Button)root.lookup("#timer");
            stopWatch = (Button)root.lookup("#stopwatch");
            int[] mode = {1,0,0};
            /**int[] mode
             * To avoid reload same function
             * mode[1] : Stopwatch ; mode[2] : Timer
             */
            stopWatch.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (mode[1] !=1){
                        Stopwatch stopwatch = new Stopwatch(root);
                        mode[1] = 1;
                    }
                }
            });
            timer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if(mode[2]!=1){
                        Timer timer  = new Timer(root);
                        mode[2] = 1;
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
        }
    }

    public void switchNotes(ActionEvent event) throws IOException {
        if(currentPage!=3) {
            root = FXMLLoader.load(getClass().getResource("fxml/notes.fxml"));
            showScene(event, root);
            currentPage=3;
            Notes notes = new Notes(root);
        }
    }
    public void switchGame(ActionEvent event) throws IOException {
        if(currentPage!=5) {
            root = FXMLLoader.load(getClass().getResource("fxml/game.fxml"));
            showScene(event, root);
            currentPage=5;
            Minigame minigame = new Minigame(root);
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

}
