import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Minigame class
 * 实现小游戏功能（数学华容道）
 * @author Qing You
 */
public class Minigame {
    Parent root;
    AnchorPane centerPane;
    Pane gamePane;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12, button13, button14, button15;
    Button game1;
    Circle restart, restart1;
    ImageView restartImg, restartImg1;
    Text minigameText, puzzleText;
    Text win1, win2, win3;

    int x = 0;
    int y = 0;
    boolean isDone = false;
    String moveTo;
    int[][] emptyTile = {{0,0,0,0,0,0},{0,1,1,1,1,0}, {0,1,1,1,1,0}, {0,1,1,1,1,0}, {0,1,1,1,2,0},{0,0,0,0,0,0}};
//    int[][] currentTile = {{1,2,3,4}, {5,6,7,8}, {9,10,15,11}, {13,14,12,0}};
    int[][] currentTile = new int[4][4];
    List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

    /** Minigame构造函数
     * 初始化页面及其他工具，将数字方块次序打乱且设置按钮（方块）事件
     * @param root 从sceneController传入的fxml场景
     */
    public Minigame(Parent root) {
        /**
         * 从fxml读取Node,初始化必要属性
         */
        this.root = root;
        centerPane = (AnchorPane) root.lookup("#centerPane");
        gamePane = (Pane) root.lookup("#gamePane");

        button1 = (Button)root.lookup("#button1");
        button2 = (Button)root.lookup("#button2");
        button3 = (Button)root.lookup("#button3");
        button4 = (Button)root.lookup("#button4");
        button5 = (Button)root.lookup("#button5");
        button6 = (Button)root.lookup("#button6");
        button7 = (Button)root.lookup("#button7");
        button8 = (Button)root.lookup("#button8");
        button9 = (Button)root.lookup("#button9");
        button10 = (Button)root.lookup("#button10");
        button11 = (Button)root.lookup("#button11");
        button12 = (Button)root.lookup("#button12");
        button13 = (Button)root.lookup("#button13");
        button14 = (Button)root.lookup("#button14");
        button15 = (Button)root.lookup("#button15");

        game1 = (Button)root.lookup("#minigame1");
        restart = (Circle)root.lookup("#restart");
        restartImg = (ImageView) root.lookup("#restartImg");
        restart1 = (Circle)root.lookup("#restart1");
        restartImg1 = (ImageView) root.lookup("#restartImg1");
        minigameText = (Text) root.lookup("#minigameText");
        puzzleText = (Text) root.lookup("#puzzleText");
        win1 = (Text) root.lookup("#win1");
        win2 = (Text) root.lookup("#win2");
        win3 = (Text) root.lookup("#win3");

        initialButton();
        randomTile();

        /** button1~15.setOnAction
         * 读取 button1~15 event，进行相应的判断工作
         */
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 1");
                moveTo = checkEmptyTile(button1.getText());
                moveTile(button1, moveTo, button1.getText());
                isDone = isComplete();
            }
        });

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 2");
                checkEmptyTile(button2.getText());
                moveTo = checkEmptyTile(button2.getText());
                moveTile(button2, moveTo, button2.getText());
                isDone = isComplete();
            }
        });

        button3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 3");
                checkEmptyTile(button3.getText());
                moveTo = checkEmptyTile(button3.getText());
                moveTile(button3, moveTo, button3.getText());
                isDone = isComplete();
            }
        });

        button4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 4");
                checkEmptyTile(button4.getText());
                moveTo = checkEmptyTile(button4.getText());
                moveTile(button4, moveTo, button4.getText());
                isDone = isComplete();
            }
        });

        button5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 5");
                checkEmptyTile(button5.getText());
                moveTo = checkEmptyTile(button5.getText());
                moveTile(button5, moveTo, button5.getText());
                isDone = isComplete();
            }
        });

        button6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 6");
                checkEmptyTile(button6.getText());
                moveTo = checkEmptyTile(button6.getText());
                moveTile(button6, moveTo, button6.getText());
                isDone = isComplete();
            }
        });

        button7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 7");
                checkEmptyTile(button7.getText());
                moveTo = checkEmptyTile(button7.getText());
                moveTile(button7, moveTo, button7.getText());
                isDone = isComplete();
            }
        });

        button8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 8");
                checkEmptyTile(button8.getText());
                moveTo = checkEmptyTile(button8.getText());
                moveTile(button8, moveTo, button8.getText());
                isDone = isComplete();
            }
        });

        button9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 9");
                checkEmptyTile(button9.getText());
                moveTo = checkEmptyTile(button9.getText());
                moveTile(button9, moveTo, button9.getText());
                isDone = isComplete();
            }
        });

        button10.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 10");
                checkEmptyTile(button10.getText());
                moveTo = checkEmptyTile(button10.getText());
                moveTile(button10, moveTo, button10.getText());
                isDone = isComplete();
            }
        });

        button11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 11");
                checkEmptyTile(button11.getText());
                moveTo = checkEmptyTile(button11.getText());
                moveTile(button11, moveTo, button11.getText());
                isDone = isComplete();
            }
        });

        button12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 12");
                checkEmptyTile(button12.getText());
                moveTo = checkEmptyTile(button12.getText());
                moveTile(button12, moveTo, button12.getText());
                isDone = isComplete();
            }
        });

        button13.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 13");
                checkEmptyTile(button13.getText());
                moveTo = checkEmptyTile(button13.getText());
                moveTile(button13, moveTo, button13.getText());
                isDone = isComplete();
            }
        });

        button14.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 14");
                checkEmptyTile(button14.getText());
                moveTo = checkEmptyTile(button14.getText());
                moveTile(button14, moveTo, button14.getText());
                isDone = isComplete();
            }
        });

        button15.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING BUTTON 15");
                checkEmptyTile(button15.getText());
                moveTo = checkEmptyTile(button15.getText());
                moveTile(button15, moveTo, button15.getText());
                isDone = isComplete();
            }
        });

        /** restart.addEventHandler, restart1.addEventHandler
         * 读取 restart Button event，重新开始游戏且打乱数字方块次序
         */
        restart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("CLICKING RESTART BUTTON");
                restartGame();
                randomTile();
            }
        });

        /** restartImg.addEventHandler, restartImg1.addEventHandler
         * 读取 restart ImageView event，重新游戏且打乱数字方块次序
         */
        restartImg.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("CLICKING RESTART BUTTON");
                restartGame();
                randomTile();
            }
        });

        restart1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("CLICKING RESTART BUTTON");
                restartGame();
                randomTile();
            }
        });

        restartImg1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("CLICKING RESTART BUTTON");
                restartGame();
                randomTile();
            }
        });

    }

    /** checkEmptyTile
     * 检查是否有空的方块
     * @param tileNum 方块上的数字
     * @return 对应走向（上、下、左、右、不移动）
     */
    private String checkEmptyTile(String tileNum) {
        int currentX = 0;
        int currentY = 0;
        int tileNumber = Integer.parseInt(tileNum);

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j ++) {
                if(currentTile[i][j] == tileNumber) {
                    currentX = i+1;
                    currentY = j+1;
                    break;
                }
            }
        }

//        System.out.println("X:" + currentX + "Y:" + currentY);
        try {
            if (emptyTile[currentX + 1][currentY] == 2) {
//                System.out.println("bottomEmpty");
                return "bottom";
            } else if (emptyTile[currentX - 1][currentY] == 2) {
//                System.out.println("topEmpty");
                return "top";
            } else if (emptyTile[currentX][currentY + 1] == 2) {
//                System.out.println("rightEmpty");
                return "right";
            } else if (emptyTile[currentX][currentY - 1] == 2) {
//                System.out.println("leftEmpty");
                return "left";
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("ITS FINE");
        }
        
        return "none";
    }

    /** moveTile
     * 对方块进行移动操作
     * @param button 方块按钮
     * @param moveTo 移动的方向
     * @param tileNum 方块上的数字
     */
    private void moveTile(Button button, String moveTo, String tileNum) {
        int temp = 0;
        int currentX = 0; //current use on currentTile;
        int currentY = 0;
        int emptyX = 0; //empty use on emptyTile
        int emptyY = 0;
        int tileNumber = Integer.parseInt(tileNum);

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(currentTile[i][j] == tileNumber) {
                    currentX = i;
                    currentY = j;
                    emptyX = i+1;
                    emptyY = j+1;
                    break;
                }
            }
        }

        if(moveTo.equals("top")) {
            button.setLayoutY(button.getLayoutY() - 100);
            temp = currentTile[currentX][currentY];
            currentTile[currentX][currentY] = currentTile[currentX - 1][currentY];
            currentTile[currentX - 1][currentY] = temp;

            temp = emptyTile[emptyX][emptyY];
            emptyTile[emptyX][emptyY] = emptyTile[emptyX - 1][emptyY];
            emptyTile[emptyX - 1][emptyY] = temp;
            System.out.println("MOVE TO TOP");

            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 4; j ++) {
                    System.out.print(currentTile[i][j] + " ");
                }
                System.out.println();
            }

            for(int i = 0; i < 6; i++) {
                for(int j = 0; j < 6; j ++) {
                    System.out.print(emptyTile[i][j] + " ");
                }
                System.out.println();
            }

        }
        else if(moveTo.equals("bottom")) {
            button.setLayoutY(button.getLayoutY() + 100);
            temp = currentTile[currentX][currentY];
            currentTile[currentX][currentY] = currentTile[currentX + 1][currentY];
            currentTile[currentX + 1][currentY] = temp;

            temp = emptyTile[emptyX][emptyY];
            emptyTile[emptyX][emptyY] = emptyTile[emptyX + 1][emptyY];
            emptyTile[emptyX + 1][emptyY] = temp;
            System.out.println("MOVE TO BOTTOM");

            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 4; j ++) {
                    System.out.print(currentTile[i][j] + " ");
                }
                System.out.println();
            }

            for(int i = 0; i < 6; i++) {
                for(int j = 0; j < 6; j ++) {
                    System.out.print(emptyTile[i][j] + " ");
                }
                System.out.println();
            }
        }
        else if(moveTo.equals("left")) {
            button.setLayoutX(button.getLayoutX() - 100);
            temp = currentTile[currentX][currentY];
            currentTile[currentX][currentY] = currentTile[currentX][currentY - 1];
            currentTile[currentX][currentY - 1] = temp;

            temp = emptyTile[emptyX][emptyY];
            emptyTile[emptyX][emptyY] = emptyTile[emptyX][emptyY - 1];
            emptyTile[emptyX][emptyY - 1] = temp;
            System.out.println("MOVE TO LEFT");

            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 4; j ++) {
                    System.out.print(currentTile[i][j] + " ");
                }
                System.out.println();
            }

            for(int i = 0; i < 6; i++) {
                for(int j = 0; j < 6; j ++) {
                    System.out.print(emptyTile[i][j] + " ");
                }
                System.out.println();
            }
        }
        else if(moveTo.equals("right")) {
            button.setLayoutX(button.getLayoutX() + 100);
            temp = currentTile[currentX][currentY];
            currentTile[currentX][currentY] = currentTile[currentX][currentY + 1];
            currentTile[currentX][currentY + 1] = temp;

            temp = emptyTile[emptyX][emptyY];
            emptyTile[emptyX][emptyY] = emptyTile[emptyX][emptyY + 1];
            emptyTile[emptyX][emptyY + 1] = temp;
            System.out.println("MOVE TO RIGHT");

            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 4; j ++) {
                    System.out.print(currentTile[i][j] + " ");
                }
                System.out.println();
            }

            for(int i = 0; i < 6; i++) {
                for(int j = 0; j < 6; j ++) {
                    System.out.print(emptyTile[i][j] + " ");
                }
                System.out.println();
            }
        }
        else {
            System.out.println("NOT MOVING");
        }
    }

    /** initialButton
     * 初始化按钮、文字与图像
     */
    private void initialButton() {
        restart.setVisible(false);
        restartImg.setVisible(false);
        minigameText.setVisible(false);
        game1.setVisible(false);
        win1.setVisible(false);
        win2.setVisible(false);
        win3.setVisible(false);
    }

    /** randomTile
     * 打乱方块的次序
     */
    private void randomTile() {
        System.out.println("List before shuffle" + numberList);
        Collections.shuffle(numberList);
        System.out.println("List after shuffle" + numberList);
        button1.setText(String.valueOf(numberList.get(0)));
        button2.setText(String.valueOf(numberList.get(1)));
        button3.setText(String.valueOf(numberList.get(2)));
        button4.setText(String.valueOf(numberList.get(3)));
        button5.setText(String.valueOf(numberList.get(4)));
        button6.setText(String.valueOf(numberList.get(5)));
        button7.setText(String.valueOf(numberList.get(6)));
        button8.setText(String.valueOf(numberList.get(7)));
        button9.setText(String.valueOf(numberList.get(8)));
        button10.setText(String.valueOf(numberList.get(9)));
        button11.setText(String.valueOf(numberList.get(10)));
        button12.setText(String.valueOf(numberList.get(11)));
        button13.setText(String.valueOf(numberList.get(12)));
        button14.setText(String.valueOf(numberList.get(13)));
        button15.setText(String.valueOf(numberList.get(14)));

        currentTile[0][0] = numberList.get(0);
        currentTile[0][1] = numberList.get(1);
        currentTile[0][2] = numberList.get(2);
        currentTile[0][3] = numberList.get(3);
        currentTile[1][0] = numberList.get(4);
        currentTile[1][1] = numberList.get(5);
        currentTile[1][2] = numberList.get(6);
        currentTile[1][3] = numberList.get(7);
        currentTile[2][0] = numberList.get(8);
        currentTile[2][1] = numberList.get(9);
        currentTile[2][2] = numberList.get(10);
        currentTile[2][3] = numberList.get(11);
        currentTile[3][0] = numberList.get(12);
        currentTile[3][1] = numberList.get(13);
        currentTile[3][2] = numberList.get(14);
        currentTile[3][3] = 0;

        System.out.println("CURRENT:" + Arrays.deepToString(currentTile));
        System.out.println("EMPTY:" + Arrays.deepToString(emptyTile));

    }

    /** isComplete
     * 判断游戏是否已完成
     * @return true表示完成游戏，false表示仍未完成
     */
    private boolean isComplete() {
        int count = 0;

        if(currentTile[0][0] == 1) {
            count++;
        }
        if(currentTile[0][1] == 2) {
            count++;
        }
        if(currentTile[0][2] == 3) {
            count++;
        }
        if(currentTile[0][3] == 4) {
            count++;
        }
        if(currentTile[1][0] == 5) {
            count++;
        }
        if(currentTile[1][1] == 6) {
            count++;
        }
        if(currentTile[1][2] == 7) {
            count++;
        }
        if(currentTile[1][3] == 8) {
            count++;
        }
        if(currentTile[2][0] == 9) {
            count++;
        }
        if(currentTile[2][1] == 10) {
            count++;
        }
        if(currentTile[2][2] == 11) {
            count++;
        }
        if(currentTile[2][3] == 12) {
            count++;
        }
        if(currentTile[3][0] == 13) {
            count++;
        }
        if(currentTile[3][1] == 14) {
            count++;
        }
        if(currentTile[3][2] == 15) {
            count++;
        }
        if(currentTile[3][3] == 0) {
            count++;
        }

        if(count == 16) {
            System.out.println("ITS DONEEEEEEEEEEEEEEEEEEEE!");
            button1.setDisable(true);
            button2.setDisable(true);
            button3.setDisable(true);
            button4.setDisable(true);
            button5.setDisable(true);
            button6.setDisable(true);
            button7.setDisable(true);
            button8.setDisable(true);
            button9.setDisable(true);
            button10.setDisable(true);
            button11.setDisable(true);
            button12.setDisable(true);
            button13.setDisable(true);
            button14.setDisable(true);
            button15.setDisable(true);
//            restart1.setDisable(true);
//            restartImg1.setDisable(true);
            restart1.setVisible(false);
            restartImg1.setVisible(false);
            restart.setVisible(true);
            restartImg.setVisible(true);
            win1.setVisible(true);
            win2.setVisible(true);
            win3.setVisible(true);

            return true;
        }

        return false;
    }

    /** restartGame
     * 重新开始游戏
     */
    private void restartGame() {
        button1.setDisable(false);
        button2.setDisable(false);
        button3.setDisable(false);
        button4.setDisable(false);
        button5.setDisable(false);
        button6.setDisable(false);
        button7.setDisable(false);
        button8.setDisable(false);
        button9.setDisable(false);
        button10.setDisable(false);
        button11.setDisable(false);
        button12.setDisable(false);
        button13.setDisable(false);
        button14.setDisable(false);
        button15.setDisable(false);
//        restart1.setDisable(false);
//        restartImg1.setDisable(false);
        restart1.setVisible(true);
        restartImg1.setVisible(true);
        restart.setVisible(false);
        restartImg.setVisible(false);
        win1.setVisible(false);
        win2.setVisible(false);
        win3.setVisible(false);

        button1.setLayoutX(0);
        button1.setLayoutY(0);
        button2.setLayoutX(100);
        button2.setLayoutY(0);
        button3.setLayoutX(200);
        button3.setLayoutY(0);
        button4.setLayoutX(300);
        button4.setLayoutY(0);
        button5.setLayoutX(0);
        button5.setLayoutY(100);
        button6.setLayoutX(100);
        button6.setLayoutY(100);
        button7.setLayoutX(200);
        button7.setLayoutY(100);
        button8.setLayoutX(300);
        button8.setLayoutY(100);
        button9.setLayoutX(0);
        button9.setLayoutY(200);
        button10.setLayoutX(100);
        button10.setLayoutY(200);
        button11.setLayoutX(200);
        button11.setLayoutY(200);
        button12.setLayoutX(300);
        button12.setLayoutY(200);
        button13.setLayoutX(0);
        button13.setLayoutY(300);
        button14.setLayoutX(100);
        button14.setLayoutY(300);
        button15.setLayoutX(200);
        button15.setLayoutY(300);

        emptyTile = new int [][]{{0,0,0,0,0,0},{0,1,1,1,1,0}, {0,1,1,1,1,0}, {0,1,1,1,1,0}, {0,1,1,1,2,0},{0,0,0,0,0,0}};
    }


}
