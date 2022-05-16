import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * 窗口拖动工具
 * @author Java小组
 * @createTime 2022/05/16
 */

public class DragUtil {

    /** addDragListener
     * 创建DragLister以及调用enableDrag方法
     * @param stage and root
     * @return None
     */

    public static void addDragListener(Stage stage, Node root){
        new DragListener(stage).enableDrag(root);
    }

    /** DragListener
     * 内部类DragLister，处理鼠标事件
     * @return None
     */

    static class DragListener implements EventHandler<MouseEvent>{
        //定义偏移量，处理窗口移动
        private double oldStageX;
        private double oldStageY;
        private double oldScreenX;
        private double oldScreenY;
        private final Stage stage;

        public DragListener(Stage stage){
            this.stage = stage;
        }

        @Override
        public void handle(MouseEvent event){
            event.consume();
            if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
                oldStageX = stage.getX();
                oldStageY = stage.getY();
                oldScreenX = event.getScreenX();
                oldScreenY = event.getScreenY();
            }
            else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED){
                stage.setX(event.getScreenX() - oldScreenX+oldStageX);
                stage.setY(event.getScreenY() - oldScreenY+oldStageY);
            }
        }
        public void enableDrag(Node node){
            node.setOnMousePressed(this);
            node.setOnMouseDragged(this);
        }
    }
}
