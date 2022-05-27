import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

/**
 * 程序入口Main
 * @author Java小组
 */

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        /*
        StackPane sp = new StackPane();
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-image:url('file:/C:/Users/USER/Desktop/北航/面向对象Java/Y3/src/pic/bgsetround.png');-fx-background-size:cover;-fx-background-radius: 20;");
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);

        DragUtil.addDragListener(primaryStage,root);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Y3");

        //消除边框以及实现圆角
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
         */

        StackPane sp = new StackPane();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(loader.getClassLoader().getResource("fxml/main2.fxml"));
        AnchorPane root = loader.load();
        root.setStyle("-fx-background-image:url('pic/bgsetround.png');-fx-background-size:cover;-fx-background-radius: 20;");
        Scene scene = new Scene(root);
        scene.setFill(Paint.valueOf("#ffffff00"));

        DragUtil.addDragListener(primaryStage,root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Y3");

        //消除边框以及实现圆角
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        //root.getChildren().add(sp);
        Node snowview = getSnowView(20,(int)scene.getWidth(),(int)scene.getHeight(),2500);
        root.getChildren().addAll(snowview);


    }

    public Node getSnowView(int number,int w,int h ,int z){

        Random random = new Random();
        ArrayList<ImageView> img_list = new ArrayList<ImageView>();
        int location_x;
        int location_y;
        int location_z;

        for(int i=0;i<number;i++){
            ImageView iv = new ImageView("pic/dandelion.png");
            iv.setPreserveRatio(true);

            if(random.nextBoolean() == true){
                location_x = random.nextInt(w) + random.nextInt(300) + 300;
            }
            else{
                location_x = random.nextInt(w) - random.nextInt(300) - 300;
            }

            location_y = random.nextInt(50);
            location_z = random.nextInt(z);

            iv.setTranslateX(location_x);
            iv.setTranslateY(location_y);
            iv.setTranslateZ(location_z);

            img_list.add(iv);
        }

        AnchorPane ap = new AnchorPane();
        ap.setStyle("-fx-background-color:#FFB6C100");
        ap.getChildren().addAll(img_list);
        SubScene subscene = new SubScene(ap,w,h,true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera();
        subscene.setCamera(camera);

        img_list.forEach(new Consumer<ImageView>() {
            @Override
            public void accept(ImageView t) {
                double time = random.nextDouble()*10+8;
                TranslateTransition tt = new TranslateTransition(Duration.seconds(time));


                tt.setFromX(t.getTranslateX());
                //tt.setFromY(t.getTranslateY());
                tt.setFromY(1200);

                tt.setByX(400);
                //tt.setByY(1200);
                tt.setByY(-1200);

                FadeTransition ft1 = new FadeTransition(Duration.seconds(time/2));
                ft1.setFromValue(0);
                ft1.setToValue(1);
                FadeTransition ft2 = new FadeTransition(Duration.seconds(time/2));
                ft2.setFromValue(1);
                ft2.setToValue(0);

                SequentialTransition st = new SequentialTransition();
                st.getChildren().addAll(ft1,ft2);

                ParallelTransition pt = new ParallelTransition();
                pt.setNode(t);
                pt.getChildren().addAll(tt,st);
                pt.setCycleCount(Animation.INDEFINITE);

                pt.play();
            }
        });

        return subscene;
    }
        public static void main (String[]args){
            Application.launch(args);
        }
    }


