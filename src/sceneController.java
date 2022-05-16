import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.text.html.ImageView;
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

    /**
     * 读取各功能页面
     * @author Java小组
     * @param event
     */

    public void switchCalendar(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml/calendar.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        DragUtil.addDragListener(stage,root);
        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.show();

    }
    public void switchTimer(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml/timer.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        DragUtil.addDragListener(stage,root);
        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.show();
    }
    public void switchTimeTable(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml/timetable.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        DragUtil.addDragListener(stage,root);
        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.show();
    }
    public void switchNotes(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml/notes.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        DragUtil.addDragListener(stage,root);
        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.show();
    }
    public void switchGame(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml/game.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        DragUtil.addDragListener(stage,root);
        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.show();
    }

    public void closeApp(ActionEvent event) throws IOException{
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Platform.exit();
    }
}
