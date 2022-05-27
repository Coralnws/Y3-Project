import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * 程序入口Main
 * @author Java小组
 */

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(loader.getClassLoader().getResource("fxml/main2.fxml"));
        Parent root = loader.load();
        root.setStyle("-fx-background-image:url('file:/C:/Users/USER/Desktop/北航/面向对象Java/Y3/src/pic/bgsetround.png');-fx-background-size:cover;-fx-background-radius: 20;");
        Scene scene = new Scene(root);
        scene.setFill(Paint.valueOf("#ffffff00"));

        DragUtil.addDragListener(primaryStage,root);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Y3");

        //消除边框以及实现圆角
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

    }

        public static void main (String[]args){
            Application.launch(args);
        }
    }


