import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;

/** Timer class
 * 实现倒计时器功能
 * @author Nws
 */
public class Timer {
    public Button Stopwatch,Timer, buttonY,buttonB, button1,button5,button10,setHr,setMin,setSec,button_1,button_5,button_10;
    Timeline timeline;
    AnchorPane centerPane;
    Parent root;
    public Boolean run = true,pause=false;
    static int hr = 0, min = 0, sec = 0;
    static int hr2 = 0,min2 = 0,sec2 = 0;
    Boolean editable,newRun,choosen;
    Text hrs,mins,secs;
    Font font = new Font("Rockwell Nova",14);
    Font buttonFont = new Font("Rockwell Nova",18);
    ArrayList<Button> buttonaddList = new ArrayList<Button>();
    ArrayList<Button> buttonsubList = new ArrayList<Button>();
    ArrayList<Text> textList = new ArrayList<Text>();
    String currentStyle,newStyle;
    AudioClip notis;
    String stopwatchStyle="-fx-background-radius: 30; -fx-background-color: Black; -fx-border-color: linear-gradient(to top,#7C7575,#B8B0B0,#DFD3D3,#FBF0F0); -fx-border-radius: 30; -fx-border-width: 3;";
    StringBuilder buttonStyle = new StringBuilder("-fx-background-radius: 20;-fx-border-color: linear-gradient(to top,#7C7575,#B8B0B0,#DFD3D3,#FBF0F0); -fx-border-radius: 20; -fx-border-width: 2;");
    StringBuilder textStyle = new StringBuilder("-fx-font-family: 'Bookman Old Style';-fx-font-size: 56;");

    /** Timer构造函数
     * 初始化页面及其他工具，设置Start按键事件和Stopwatch按键事件
     */
    public Timer(Parent root){
        /**
         * 从fxml读取Node,初始化必要属性
         */
        this.root = root;
        editable = true;
        choosen=false;
        newRun=true;
        URL url = this.getClass().getClassLoader().getResource("audio/timerEnd2.wav");
        notis = new AudioClip(url.toExternalForm());
        centerPane = (AnchorPane)root.lookup("#centerPane");
        setHr = (Button)root.lookup("#setHr");
        setMin = (Button)root.lookup("#setMin");
        setSec = (Button)root.lookup("#setSec");
        Stopwatch = (Button)root.lookup("#stopwatch");
        Timer = (Button)root.lookup("#timer");

        /**
         * 初始化新Button和Text
         */
        initializeText(root);
        initializeButton(root);

        /**
         * 获取按键样式，用于后续替代
         */
        currentStyle = Timer.getStyle();
        newStyle = currentStyle.replace("-fx-border-color: linear-gradient(to top,#7C7575,#B8B0B0,#DFD3D3,#FBF0F0);", "-fx-border-color: #FFBC97;");

        /** buttonY.setOnAction
         * 读取 Start button event,start Timer
         */
        buttonY.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(newRun){
                    startTimer();
                }
            }
        });

        /** setHr,setMin,setSec
         * 对特定时间域进行增减
         */
        setHr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(editable){
                    choosen=true;
                    hrs.setStyle(String.valueOf(textStyle.append("-fx-fill: #08D9D6;")));
                    mins.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
                    secs.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
                    setTime(hrs,1);
                }
            }
        });
        setMin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(editable) {
                    choosen = true;
                    mins.setStyle(String.valueOf(textStyle.append("-fx-fill: #08D9D6;")));
                    hrs.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
                    secs.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
                    setTime(mins, 2);
                }
            }
        });
        setSec.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(editable) {
                    choosen = true;
                    secs.setStyle(String.valueOf(textStyle.append("-fx-fill: #08D9D6;")));
                    mins.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
                    hrs.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
                    setTime(secs, 3);
                }
            }
        });


        /** Timeline
         * 使用timeline实现每秒时间递减和重写Timer时间
         */
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reWrite();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }

    /** setScene
     * 转换Stopwatch功能时，判断上次离开时是处于运行状态，将隐藏的组件设为可视
     */
    public void setScene(){
        visibleRec(root);
        buttonY.setVisible(true);
        if(run){
            buttonB.setVisible(false);
        }
        else{
            buttonB.setVisible(true);
        }
        if(pause){
            buttonB.setVisible(true);
        }

        for(int i=0;i<3;i++){
            Button button = new Button();
            button = buttonaddList.get(i);
            button.setVisible(true);
        }
        for(int i=0;i<3;i++){
            Button button = new Button();
            button = buttonsubList.get(i);
            button.setVisible(true);
        }
        for(int i=0;i<3;i++){
            Text text = new Text();
            text = textList.get(i);
            text.setVisible(true);
        }

        //Change style
        Timer.setStyle(newStyle);
        Stopwatch.setStyle(stopwatchStyle);

        /** Stopwatch.setOnAction
         * 读取Stopwatch button event,转Stopwatch功能
         */
        Stopwatch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeStopwatch();
            }
        });

        /** Stopwatch.setOnAction
         * 重复按键事件时，读取事件并且不做任何操作
         */
        Timer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Press Timer Again");
            }
        });
    }

    /** changeScene
     * 离开此功能时，将组件设为隐藏
     */
    public void changeScene(){
        buttonY.setVisible(false);
        buttonB.setVisible(false);
        for(int i=0;i<3;i++){
            Button button = new Button();
            button = buttonaddList.get(i);
            button.setVisible(false);
        }
        for(int i=0;i<3;i++){
            Button button = new Button();
            button = buttonsubList.get(i);
            button.setVisible(false);
        }
        for(int i=0;i<3;i++){
            Text text = new Text();
            text = textList.get(i);
            text.setVisible(false);
        }
    }

    /** initializeButton
     * 初始化Button及美化
     * @param root
     */
    private void initializeButton(Parent root){
        //Yellow button:Start,pause,resume
        buttonY = new Button("Start");
        buttonY.setStyle(String.valueOf(buttonStyle.append("-fx-background-color: #FFEEDB;")));
        buttonY.setFont(buttonFont);
        buttonY.setLayoutX(410);
        buttonY.setLayoutY(400);
        buttonY.setPrefHeight(55);
        buttonY.setPrefWidth(122);
        buttonY.setVisible(false);

        //Blue button: Stop
        buttonB = new Button("Stop");
        buttonB.setStyle(String.valueOf(buttonStyle.append("-fx-background-color: #CCF3EE;")));
        buttonB.setLayoutX(315);
        buttonB.setLayoutY(400);
        buttonB.setFont(buttonFont);
        buttonB.setPrefHeight(55);
        buttonB.setPrefWidth(122);
        buttonB.setVisible(false);

        button1 = new Button("+1");
        button5 = new Button("+5");
        button10 = new Button("+10");
        button_1 = new Button("-1");
        button_5 = new Button("-5");
        button_10 = new Button("-10");
        buttonaddList.add(button1);
        buttonaddList.add(button5);
        buttonaddList.add(button10);
        buttonsubList.add(button_1);
        buttonsubList.add(button_5);
        buttonsubList.add(button_10);

        for(int i=0;i<3;i++){
            Button button = new Button();
            button = buttonaddList.get(i);
            button.setStyle(String.valueOf(buttonStyle.append("-fx-background-color: #F2C9E1;")));
            button.setLayoutX(620);
            button.setFont(font);
            button.setPrefHeight(50);
            button.setPrefWidth(50);
            button.setVisible(false);
        }

        for(int i=0;i<3;i++){
            Button button = new Button();
            button = buttonsubList.get(i);
            button.setStyle(String.valueOf(buttonStyle.append("-fx-background-color: #95E1D3;")));
            button.setLayoutX(680);
            button.setFont(font);
            button.setPrefHeight(50);
            button.setPrefWidth(50);
            button.setVisible(false);
        }
        button1.setLayoutY(360);
        button5.setLayoutY(420);
        button10.setLayoutY(480);
        button_1.setLayoutY(360);
        button_5.setLayoutY(420);
        button_10.setLayoutY(480);

        centerPane.getChildren().addAll(buttonY, buttonB,button1,button5,button10,button_1,button_5,button_10);
    }
    /** initializeText
     * 初始化Text及美化
     * @param root
     */
    private void initializeText(Parent root){
        hrs = new Text("00");
        mins = new Text("00");
        secs = new Text("00");
        hrs.setMouseTransparent(true);
        textList.add(hrs);
        textList.add(mins);
        textList.add(secs);
        for(int i=0;i<3;i++){
            Text text = new Text();
            text = textList.get(i);
            text.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
            text.setLayoutY(278);
            text.setMouseTransparent(true);
            text.setVisible(false);
        }
        hrs.setLayoutX(296);
        mins.setLayoutX(437);
        secs.setLayoutX(579);
        centerPane.getChildren().addAll(mins,secs,hrs);
    }

    /** visibleRec
     * 使fxml中的TimerBox可视
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

    /** setTime
     * 读取Button event，设置Timer的值（ +- 1,5,10）
     * @param text
     * @param type
     */
    public void setTime(Text text,int type){
        newRun = true;
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(choosen) {
                    if (type == 1 && hr < 23) {
                        hr++;
                        text.setText((((hr / 10) == 0) ? "0" : "") + hr);
                    }
                    if (type == 2 && min < 59) {
                        min++;
                        text.setText((((min / 10) == 0) ? "0" : "") + min);
                    }
                    if (type == 3 && sec < 59) {
                        sec++;
                        text.setText((((sec / 10) == 0) ? "0" : "") + sec);
                    }
                }
            }
        });
        button5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (choosen) {
                    if (type == 1) {
                        hr += 5;
                        if (hr > 23) {
                            hr = 23;
                        }
                        text.setText((((hr / 10) == 0) ? "0" : "") + hr);
                    }
                    if (type == 2) {
                        min += 5;
                        if (min > 59) {
                            min = 59;
                        }
                        text.setText((((min / 10) == 0) ? "0" : "") + min);
                    }
                    if (type == 3) {
                        sec += 5;
                        if (sec > 59) {
                            sec = 59;
                        }
                        text.setText((((sec / 10) == 0) ? "0" : "") + sec);
                    }
                }
            }
        });
        button10.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (choosen) {
                    if (type == 1) {
                        hr += 10;
                        if (hr > 23) {
                            hr = 23;
                        }
                        text.setText((((hr / 10) == 0) ? "0" : "") + hr);
                    }
                    if (type == 2) {
                        min += 10;
                        if (min > 59) {
                            min = 59;
                        }
                        text.setText((((min / 10) == 0) ? "0" : "") + min);
                    }
                    if (type == 3) {
                        sec += 10;
                        if (sec > 59) {
                            sec = 59;
                        }
                        text.setText((((sec / 10) == 0) ? "0" : "") + sec);
                    }
                }
            }
        });
        button_1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(choosen) {
                    if (type == 1 && hr > 0) {
                        hr--;
                        text.setText((((hr / 10) == 0) ? "0" : "") + hr);
                    }
                    if (type == 2 && min > 0) {
                        min++;
                        text.setText((((min / 10) == 0) ? "0" : "") + min);
                    }
                    if (type == 3 && sec >0 ) {
                        sec++;
                        text.setText((((sec / 10) == 0) ? "0" : "") + sec);
                    }
                }
            }
        });
        button_1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(choosen) {
                    if (type == 1 && hr > 0) {
                        hr--;
                        text.setText((((hr / 10) == 0) ? "0" : "") + hr);
                    }
                    if (type == 2 && min > 0) {
                        min--;
                        text.setText((((min / 10) == 0) ? "0" : "") + min);
                    }
                    if (type == 3 && sec >0 ) {
                        sec--;
                        text.setText((((sec / 10) == 0) ? "0" : "") + sec);
                    }
                }
            }
        });
        button_5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (choosen) {
                    if (type == 1) {
                        hr -= 5;
                        if (hr < 0) {
                            hr = 0;
                        }
                        text.setText((((hr / 10) == 0) ? "0" : "") + hr);
                    }
                    if (type == 2) {
                        min -= 5;
                        if (min < 0) {
                            min = 0;
                        }
                        text.setText((((min / 10) == 0) ? "0" : "") + min);
                    }
                    if (type == 3) {
                        sec -= 5;
                        if (sec < 0 ) {
                            sec = 0;
                        }
                        text.setText((((sec / 10) == 0) ? "0" : "") + sec);
                    }
                }
            }
        });
        button_10.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (choosen) {
                    if (type == 1) {
                        hr -= 10;
                        if (hr < 0) {
                            hr = 0;
                        }
                        text.setText((((hr / 10) == 0) ? "0" : "") + hr);
                    }
                    if (type == 2) {
                        min -= 10;
                        if (min < 0) {
                            min = 0;
                        }
                        text.setText((((min / 10) == 0) ? "0" : "") + min);
                    }
                    if (type == 3) {
                        sec -= 10;
                        if (sec < 0) {
                            sec = 0;
                        }
                        text.setText((((sec / 10) == 0) ? "0" : "") + sec);
                    }
                }
            }
        });
    }

    /** startTimer
     * Start timer以及控制Node的转变和Button event
     */
    public void startTimer(){
        if(newRun){
            if(hr==0 && min==0 && sec==0){
                sec=1;
            }
            hr2 = hr;
            min2 = min;
            sec2 = sec;
            setText(1);
            editable=false;
            newRun=false;
            choosen=false;
        }

        if (run) {
            timeline.play();
            run = false;
            pause=false;
            buttonY.setText("Pause");
            buttonY.setLayoutX(470);
            buttonY.setLayoutY(400);
            buttonB.setVisible(true);
            buttonB.setText("Stop");
            buttonY.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    startTimer();
                }
            });

            buttonB.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stopTimer();
                }
            });
        } else {
            timeline.pause();
            pause = true;
            run = true;
            buttonY.setText("Resume");
            buttonY.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    startTimer();
                }
            });
            buttonB.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stopTimer();
                }
            });
        }
    }

    /** stopTimer
     * Timer结束或中断时重置Node
     */
    private void stopTimer(){
        timeline.stop();
        newRun=true;
        run=true;
        editable=true;
        choosen=false;
        pause=false;
        setText(1);
        buttonB.setVisible(false);
        buttonY.setText("Start");
        buttonY.setLayoutX(410);
        buttonY.setLayoutY(400);
    }

    /** setText
     * 将设置的时间写入TimerBox
     */
    private void setText(int mode){
        if(mode==1) {
            hrs.setText((((hr / 10) == 0) ? "0" : "") + hr);
            mins.setText((((min / 10) == 0) ? "0" : "") + min);
            secs.setText((((sec / 10) == 0) ? "0" : "") + sec);
        }
        else if(mode==0){
            hrs.setText((((hr / 10) == 0) ? "0" : "") + hr2);
            mins.setText((((min / 10) == 0) ? "0" : "") + min2);
            secs.setText((((sec / 10) == 0) ? "0" : "") + sec2);
        }
        hrs.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
        mins.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
        secs.setStyle(String.valueOf(textStyle.append("-fx-fill:WHITE;")));
    }

    /**reWrite
     * countdown时每秒重写时间
     */
    private void reWrite() {
        while (sec2 == 0) {
            if(hr2 == 0 && min2 == 0){
                notis.play();
                stopTimer();
                return;
            }
            if(min2>0){
                min2--;
                sec2 = 60;
            }
            else if (min2 == 0) {
                if(hr2>0){
                    hr2--;
                    min2 = 60;
                }
            }
        }

        sec2--;
        hrs.setText((((hr2 / 10) == 0) ? "0" : "") + hr2);
        mins.setText((((min2 / 10) == 0) ? "0" : "") + min2);
        secs.setText((((sec2 / 10) == 0) ? "0" : "") + sec2);
    }

    /** changeStopwatch
     * 切换Stopwatch功能
     */
    public void changeStopwatch(){
        changeScene();
        Timer.setStyle(currentStyle);
        sceneController.stopwatch.setScene();
        sceneController.stopwatch.buttonY.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneController.stopwatch.startPauseStopwatch();
            }
        });
    }
}