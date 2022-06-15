import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notes {
    Parent root;
    GridPane notePane;
    Button add;
    List<TextArea> taList = new ArrayList<>(10);

    int curNote = 0;
    int gridPaneX = 0;
    int gridPaneY = 0;

    StringBuilder noteStyle = new StringBuilder("-fx-control-inner-background:#ffffcc; ");
    StringBuilder buttonStyle = new StringBuilder("-fx-background-color:#ffff66; ");
    String saveButtonStyle = new String("-fx-background-color:#ffff66; -fx-border-width:1; -fx-border-color:#000000; ");

    public Notes(Parent root) {
        this.root = root;
        notePane = (GridPane) root.lookup("#notePane");
        notePane.setHgap(10);
        notePane.setVgap(13);
        add = (Button) root.lookup("#add");

        loadNote();

        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("CLICKING ADD");
                addNote();
            }
        });


    }

    private void addNote() {
        if(curNote == 9) {
            System.out.println("FULL CANNOT ADD");
            return;
        }

        Button love = new Button("♥");
        Button delete = new Button("X");
        Button smile = new Button("☺");
        Button save = new Button("保存");
        Region filler = new Region();
        love.setStyle(String.valueOf(buttonStyle));
        delete.setStyle(String.valueOf(buttonStyle));
        smile.setStyle(String.valueOf(buttonStyle));
        filler.setStyle(String.valueOf(buttonStyle));
        save.setStyle(String.valueOf(buttonStyle));
        love.setId("love" + curNote);
        delete.setId("delete" + curNote);
        smile.setId("smile" + curNote);
        save.setId("save" + curNote);

        EventHandler<MouseEvent> clickLove = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("CLICK " + love.getId());
                Integer curNote = Integer.valueOf(love.getId().substring(4,5));
                TextArea tempNote = taList.get(curNote);
                tempNote.appendText("♥");
            }
        };

        EventHandler<MouseEvent> clickDelete = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("CLICK " + delete.getId());
                deleteNote(delete.getId());
            }
        };

        EventHandler<MouseEvent> clickSmile = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("CLICK " + smile.getId());
            }
        };

        EventHandler<MouseEvent> clickSave = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("CLICK " + save.getId());
                saveNote();
            }
        };

        love.addEventHandler(MouseEvent.MOUSE_CLICKED, clickLove);
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, clickDelete);
        smile.addEventHandler(MouseEvent.MOUSE_CLICKED, clickSmile);
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, clickSave);

        HBox.setHgrow(filler, Priority.ALWAYS);
        HBox controlsContainer = new HBox();
        controlsContainer.getChildren().addAll(love, smile, save, filler, delete);

        TextArea tempNote = new TextArea();
        tempNote.setPromptText("Write your text here!");
        tempNote.setWrapText(true);
        tempNote.setStyle(String.valueOf(noteStyle));
        taList.add(tempNote);
        GridPane.setMargin(tempNote, new Insets(0, 0, 10, 0));

        notePane.add(controlsContainer, gridPaneX, gridPaneY++); // 0 0 -> 1 0 -> 2 0
        notePane.add(tempNote, gridPaneX++, gridPaneY--); // 0 1 -> 1 1 -> 2 1
        if(gridPaneX > 2) {
            gridPaneX = 0;
            gridPaneY += 2;
        }
        curNote += 1;


    }

    private void deleteNote(String deleteId) {
        System.out.println(deleteId.length());
    }

    private void loadNote() {

    }

    private void saveNote() {

    }


}
