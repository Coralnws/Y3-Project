import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/** Clock class
 * 显示日期与时间
 * @author Nws
 */

public class Clock {
    /** Clock构造函数
     * 读取日期时间并显示，实现每秒重写时间
     * @param leftPane
     */
    public Clock(AnchorPane leftPane){
        Text timeDate = (Text)leftPane.lookup("#timeDate");
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy \nHH : mm : ss");
        leftPane.setBottomAnchor(timeDate,15.0);
        leftPane.setLeftAnchor(timeDate,20.0);
        timeDate.setText(df.format(new Date()));
        EventHandler<ActionEvent> eventHandler = e ->{
            timeDate.setText(df.format(new Date()));
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000),eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
}