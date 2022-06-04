import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;

/** Stopwatch class
 * 实现秒表功能
 * @author Nws
 */
public class Stopwatch {
    public Button Stopwatch, Timer,buttonY, Pause, Resume, buttonB, Reset;
    Timeline timeline;
    AnchorPane centerPane,lapPane;
    Parent root;
    public Boolean run = true;
    int min = 0, sec = 0, centi = 0;
    Text mins,secs,centis;
    ArrayList<Text> textList = new ArrayList<Text>();
    Boolean newRun;
    Font font = new Font("Rockwell Nova",14);  //font for lap
    Font buttonFont = new Font("Rockwell Nova",18);
    static int layoutText = 18; //+24
    static int layoutRec = 1; //+24
    static int cnt = 1;
    String currentStyle;
    StringBuilder buttonStyle = new StringBuilder("-fx-background-radius: 20;-fx-border-color: linear-gradient(to top,#7C7575,#B8B0B0,#DFD3D3,#FBF0F0); -fx-border-radius: 20; -fx-border-width: 2;");

    /** Stopwatch构造函数
     * 初始化页面及其他工具，设置Start按键事件和Timer按键事件
     * @param root
     */
    public Stopwatch(Parent root) {
        /**
         * 从fxml读取Node,初始化必要属性
         */
        this.root = root;
        newRun=true;
        centerPane = (AnchorPane)root.lookup("#centerPane");
        Stopwatch = (Button)root.lookup("#stopwatch");
        Timer = (Button)root.lookup("#timer");
        lapPane = (AnchorPane)root.lookup("#lapPane");

        //Initialize
        initializeButton(root);
        initializeText(root);
        visibleRec(root);
        buttonY.setVisible(true);
        buttonB.setVisible(false);

        //Change style
        currentStyle = Stopwatch.getStyle();
        String newStyle = currentStyle.replace("-fx-border-color: linear-gradient(to top,#7C7575,#B8B0B0,#DFD3D3,#FBF0F0);", "-fx-border-color: #FFBC97;");
        Stopwatch.setStyle(newStyle);

        /** Timeline
         * 使用timeline实现每秒时间递减和重写Stopwatch时间
         */
        timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reWrite();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        /** buttonY.setOnAction
         * 读取 Start button event,start Stopwatch
         */
        buttonY.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(newRun) {
                    startPauseStopwatch();
                }
            }
        });

        /** Timer.setOnAction
         * 读取Timer button event,转Timer功能
         */
        Timer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeTimer();
            }
        });
    }

    /** initializeButton
     * 初始化Button及美化
     * @param root
     */
    private void initializeButton(Parent root){
        //Yellow button : start,resume,pause
        buttonY = new Button("Start");
        buttonY.setStyle(String.valueOf(buttonStyle.append("-fx-background-color: #FFEEDB;")));
        buttonY.setFont(buttonFont);
        buttonY.setLayoutX(421);
        buttonY.setLayoutY(400);
        buttonY.setPrefHeight(55);
        buttonY.setPrefWidth(122);

        //Blue button : lap,reset
        buttonB = new Button("Lap");
        buttonB.setStyle(String.valueOf(buttonStyle.append("-fx-background-color: #CCF3EE;")));
        buttonB.setLayoutX(324);
        buttonB.setLayoutY(400);
        buttonB.setFont(buttonFont);
        buttonB.setPrefHeight(55);
        buttonB.setPrefWidth(122);

        centerPane.getChildren().addAll(buttonY, buttonB);
    }

    /** initializeText
     * 初始化Text及美化
     * @param root
     */
    public void initializeText(Parent root){
        mins = new Text("00");
        secs = new Text("00");
        centis = new Text("00");
        textList.add(mins);
        textList.add(secs);
        textList.add(centis);
        for(int i=0;i<3;i++){
            Text text = new Text();
            text = textList.get(i);
            text.setStyle("-fx-fill:WHITE;-fx-font-family: 'Bookman Old Style';-fx-font-size: 56;");
            text.setLayoutY(278);
        }
        mins.setLayoutX(296);
        secs.setLayoutX(437);
        centis.setLayoutX(579);
        centerPane.getChildren().addAll(mins,secs,centis);
    }

    /**reWrite
     * 重写时间
     */
    private void reWrite() {
        if (centi == 100) {
            sec++;
            centi = 0;
        }
        if (sec == 60) {
            min++;
            sec = 0;
        }
        mins.setText((((min / 10) == 0) ? "0" : "") + min);
        secs.setText((((sec / 10) == 0) ? "0" : "") + sec);
        centis.setText((((centi / 10) == 0) ? "0" : "") + centi++);
    }

    /** startPauseStopwatch
     * 开始计时以及控制Node
     */
    public void startPauseStopwatch() {
        if (run) {
            timeline.play();
            run = false;
            newRun=false;
            buttonY.setText("Pause");
            buttonY.setLayoutX(512);
            buttonY.setLayoutY(400);
            buttonB.setVisible(true);
            buttonB.setText("Lap");
            buttonY.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    startPauseStopwatch();
                }
            });

            buttonB.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    addLap(min+" : "+sec+" : "+centi);
                }
            });
        } else {
            timeline.pause();
            run = true;
            buttonY.setText("Resume");
            buttonY.setFont(new Font("Rockwell Nova",17));
            buttonB.setText("Reset");
            buttonY.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    startPauseStopwatch();
                }
            });
            buttonB.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    min = 0;
                    sec = 0;
                    centi = 0;
                    timeline.pause();
                    mins.setText("00");
                    secs.setText("00");
                    centis.setText("00");
                    lapPane.getChildren().clear();
                    cnt=1;
                    newRun=true;
                    layoutText = 18;
                    layoutRec = 1;
                    buttonB.setVisible(false);
                    buttonY.setText("Start");
                    buttonY.setLayoutX(421);
                    buttonY.setLayoutY(400);
                }
            });
        }
    }

    /** addLap
     * Show elapsed time to that point
     * @param time
     */
    public void addLap(String time){
        if (cnt > 13) {
            return;
        }
        Text text = new Text();
        text.setFont(font);
        text.setText(min+" : "+sec+" : "+centi);
        text.setLayoutX(9);
        text.setLayoutY(layoutText);
        layoutText += 24;

        Rectangle rec = new Rectangle();
        if(cnt%2 == 0)
            rec.setFill(Color.web("#f2f2f2"));
        else
            rec.setFill(Color.WHITE);
        cnt++;

        rec.setX(0);
        rec.setY(layoutRec);
        rec.setHeight(26);
        rec.setWidth(158);
        rec.setStyle("-fx-border-color:grey;");
        layoutRec += 24;
        lapPane.getChildren().addAll(rec,text);
    }

    /** visibleRec
     * 使fxml中的时间显示器可视
     * @param root
     */
    public void visibleRec(Parent root){
        Rectangle rec = (Rectangle)root.lookup("#panel");
        Line line1 = (Line)root.lookup("#line1");
        Line line2 = (Line)root.lookup("#line2");
        rec.setVisible(true);
        line1.setVisible(true);
        line2.setVisible(true);
    }

    /** changeTimer
     * 切换Timer功能
     */
    public void changeTimer(){
        centerPane.getChildren().removeAll(buttonY, buttonB,mins,secs,centis);
        Stopwatch.setStyle(currentStyle);
        Timer timer = new Timer(root);
        timer.buttonY.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timer.startTimer();
            }
        });
    }
}
