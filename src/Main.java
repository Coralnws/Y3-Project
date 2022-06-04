import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

/** Main
 * 程序入口Main
 * @author Java小组
 */

public class Main extends Application {
    @Override
    /** start
     * start application and play animation
     * @param Stage
     * @author Nws
     */
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(loader.getClassLoader().getResource("fxml/main.fxml"));
        AnchorPane root = loader.load();
        ImageView bg = (ImageView) root.lookup("#bg");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        int arc = 40;
        Rectangle clip = new Rectangle(bg.getFitWidth(),bg.getFitHeight());
        clip.setArcWidth(arc);
        clip.setArcHeight(arc);
        bg.setClip(clip);

        DragUtil.addDragListener(primaryStage,root);

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("pic/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Y3");

        //消除窗口边框以及实现圆角
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        Node snowview = getAnimationView(20,(int)scene.getWidth(),(int)scene.getHeight(),2500);
        snowview.setMouseTransparent(true);
        root.getChildren().addAll(snowview);

        /**
         * 实现主页面Text闪烁效果
         */
        Text text = (Text)root.lookup("#text");
        FadeTransition textFade = new FadeTransition(Duration.seconds(0.8),text);
        textFade.setFromValue(1);
        textFade.setToValue(0.5);
        textFade.setCycleCount(Timeline.INDEFINITE);
        textFade.setAutoReverse(true);
        textFade.play();
    }

    /** getAnimationView
     * 主页面蒲公英动画
     * @author Nws
     */
    public Node getAnimationView(int number,int w,int h ,int z){

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
                tt.setFromY(970);
                tt.setByX(400);
                //tt.setByY(1200);
                tt.setByY(-980);

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


