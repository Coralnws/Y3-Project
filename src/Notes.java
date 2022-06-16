import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Notes {
    Parent root;
    GridPane notePane;
    Button add;
    List<String> saveIdList = new ArrayList<>(10);
    List<TextArea> taList = new ArrayList<>(10);

    int curNote = 0;
    int gridPaneX = 0;
    int gridPaneY = 0;
    static int index = 0;
    ArrayList<StringBuilder> buttonStyleList = new ArrayList<StringBuilder>();
    ArrayList<StringBuilder> noteStyleList = new ArrayList<StringBuilder>();
    StringBuilder noteStyle = new StringBuilder("-fx-font-family:Rockwell Nova;-fx-font-size: 17;");
    StringBuilder buttonStyle = new StringBuilder("-fx-background-color:#ffb566;");
    StringBuilder addButtonStyle = new StringBuilder("-fx-background-radius:20; -fx-background-color:linear-gradient(#DC4570, #f4e2d8);");
    String saveButtonStyle = new String("-fx-background-color:#ffff66; -fx-border-width:1; -fx-border-color:#000000; ");

    public Notes(Parent root) {
        this.root = root;
        notePane = (GridPane) root.lookup("#notePane");
        notePane.setHgap(10);
        notePane.setVgap(13);
        add = (Button) root.lookup("#add");
        add.setStyle(String.valueOf(addButtonStyle));
        buttonStyleList.add(new StringBuilder("-fx-background-color:#F9F2ED"));
        buttonStyleList.add(new StringBuilder("-fx-background-color:#FFE5B4"));
        buttonStyleList.add(new StringBuilder("-fx-background-color:#FFE6E6"));
        buttonStyleList.add(new StringBuilder("-fx-background-color:#F6FBF4"));
        noteStyleList.add(new StringBuilder(noteStyle.append("-fx-control-inner-background:#F9F2ED;")));
        noteStyleList.add(new StringBuilder(noteStyle.append("-fx-control-inner-background:#FFE5B4;")));
        noteStyleList.add(new StringBuilder(noteStyle.append("-fx-control-inner-background:#FFE6E6;")));
        noteStyleList.add(new StringBuilder(noteStyle.append("-fx-control-inner-background:#F6FBF4;")));

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
        Button balloon = new Button("\uD83C\uDF88");
        Button save = new Button("保存");
        save.setFont(new Font("STFangSong",13));
        Region filler = new Region();
        love.setStyle(String.valueOf(buttonStyleList.get(index)));
        delete.setStyle(String.valueOf(buttonStyleList.get(index)));
        balloon.setStyle(String.valueOf(buttonStyleList.get(index)));
        filler.setStyle(String.valueOf(buttonStyleList.get(index)));
        save.setStyle(String.valueOf(buttonStyleList.get(index)));
        love.setId("love" + curNote);
        delete.setId("delete" + curNote);
        balloon.setId("balloon" + curNote);
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

        EventHandler<MouseEvent> clickBalloon = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("CLICK " + balloon.getId());
                Integer curNote = Integer.valueOf(balloon.getId().substring(7,8));
                TextArea tempNote = taList.get(curNote);
                tempNote.appendText("\uD83C\uDF88");
            }
        };

        EventHandler<MouseEvent> clickSave = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("CLICK " + save.getId());
                saveNote(save.getId());
            }
        };

        love.addEventHandler(MouseEvent.MOUSE_CLICKED, clickLove);
        balloon.addEventHandler(MouseEvent.MOUSE_CLICKED, clickBalloon);
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, clickSave);

        HBox.setHgrow(filler, Priority.ALWAYS);
        HBox controlsContainer = new HBox();
        controlsContainer.getChildren().addAll(love, balloon, save, filler, delete);

        TextArea tempNote = new TextArea();
        tempNote.setPromptText("Write your text here!");
        tempNote.setWrapText(true);
        tempNote.setStyle(String.valueOf(noteStyleList.get(index)));
        index++;
        if(index>3){
            index = 0;
        }
        saveIdList.add(save.getId());
        taList.add(tempNote);
        GridPane.setMargin(tempNote, new Insets(0, 0, 10, 0));

        notePane.add(controlsContainer, gridPaneX, gridPaneY++); // 0 0 -> 1 0 -> 2 0
        notePane.add(tempNote, gridPaneX++, gridPaneY--); // 0 1 -> 1 1 -> 2 1
        if(gridPaneX > 2) {
            gridPaneX = 0;
            gridPaneY += 2;
        }
        curNote += 1;

        EventHandler<MouseEvent> clickDelete = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("CLICK " + delete.getId());
                deleteNote(save.getId());
                notePane.getChildren().remove(tempNote);
                notePane.getChildren().remove(controlsContainer);

                curNote = 0;
                gridPaneX = 0;
                gridPaneY = 0;

                saveIdList.removeAll(saveIdList);
                taList.removeAll(taList);
                loadNote();

            }
        };
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, clickDelete);

    }

    private void deleteNote(String saveId) {
        System.out.println("DELETE note: " + saveId);

        File delFile = new File("src/notes/" + saveId + ".txt");
        if(delFile.delete()) {
            System.out.println("DELETE SUCCESS");
        }
        else {
            System.out.println("DELETE FAILED");
        }

        Integer delSaveId = Integer.valueOf(saveId.substring(4,5));

        for(int i = delSaveId; i < curNote - 1; i++) {
            File file = new File("src/notes/save" + (i+1) + ".txt");
            File rename = new File("src/notes/save" + i + ".txt");

            if(file.renameTo(rename)) {
                System.out.println("RENAME SUCCESS");
            }
            else {
                System.out.println("RENAME FAILED");
            }
        }
        
    }

    private void loadNote() {
        notePane.getChildren().clear();
        File directory = new File("src/notes");
        int fileCount = directory.list().length;
        System.out.println("Load File Count:"+fileCount);

        for(int i = 0; i < fileCount; i++) {
            Button love = new Button("♥");
            Button delete = new Button("X");
            Button balloon = new Button("\uD83C\uDF88");
            Button save = new Button("保存");
            save.setFont(new Font("STFangSong",14));
            Region filler = new Region();
            love.setStyle(String.valueOf(buttonStyleList.get(index)));
            delete.setStyle(String.valueOf(buttonStyleList.get(index)));
            balloon.setStyle(String.valueOf(buttonStyleList.get(index)));
            filler.setStyle(String.valueOf(buttonStyleList.get(index)));
            save.setStyle(String.valueOf(buttonStyleList.get(index)));
            love.setId("love" + curNote);
            delete.setId("delete" + curNote);
            balloon.setId("balloon" + curNote);
            save.setId("save" + curNote);

            EventHandler<MouseEvent> clickLove = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    System.out.println("CLICK " + love.getId());
                    Integer curNote = Integer.valueOf(love.getId().substring(4, 5));
                    TextArea tempNote = taList.get(curNote);
                    tempNote.appendText("♥");
                }
            };

            EventHandler<MouseEvent> clickBalloon = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    System.out.println("CLICK " + balloon.getId());
                    Integer curNote = Integer.valueOf(balloon.getId().substring(7, 8));
                    TextArea tempNote = taList.get(curNote);
                    tempNote.appendText("\uD83C\uDF88");
                }
            };

            EventHandler<MouseEvent> clickSave = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    System.out.println("CLICK " + save.getId());
                    saveNote(save.getId());
                }
            };

            love.addEventHandler(MouseEvent.MOUSE_CLICKED, clickLove);
            balloon.addEventHandler(MouseEvent.MOUSE_CLICKED, clickBalloon);
            save.addEventHandler(MouseEvent.MOUSE_CLICKED, clickSave);

            HBox.setHgrow(filler, Priority.ALWAYS);
            HBox controlsContainer = new HBox();
            controlsContainer.getChildren().addAll(love, balloon, save, filler, delete);

            TextArea tempNote = new TextArea();
            try {
                List<String> line = new ArrayList<>();
                File inFile = new File("src/notes/" + save.getId() + ".txt");
                BufferedReader in = new BufferedReader(new FileReader(inFile));
                String str;

                while((str = in.readLine()) != null) {
                    line.add(str);
                }

                for(int j = 0; j < line.size(); j++) {
                    tempNote.appendText(line.get(j));
                    if(j != line.size() - 1) {
                        tempNote.appendText("\n");
                    }
                }

                in.close();

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("IOIOIO");
            }


            tempNote.setPromptText("Write your text here!");
            tempNote.setWrapText(true);
            tempNote.setStyle(String.valueOf(noteStyleList.get(index)));
            index++;
            if(index>3){
                index = 0;
            }

            saveIdList.add(save.getId());
            taList.add(tempNote);
            GridPane.setMargin(tempNote, new Insets(0, 0, 10, 0));
            
            notePane.add(controlsContainer, gridPaneX, gridPaneY++); // 0 0 -> 1 0 -> 2 0
            notePane.add(tempNote, gridPaneX++, gridPaneY--); // 0 1 -> 1 1 -> 2 1
            if(gridPaneX > 2) {
                gridPaneX = 0;
                gridPaneY += 2;
            }
            curNote+= 1;

            EventHandler<MouseEvent> clickDelete = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    System.out.println("CLICK " + delete.getId());
                    deleteNote(save.getId());
                    notePane.getChildren().remove(tempNote);
                    notePane.getChildren().remove(controlsContainer);

                    curNote = 0;
                    gridPaneX = 0;
                    gridPaneY = 0;

                    saveIdList.removeAll(saveIdList);
                    taList.removeAll(taList);
                    loadNote();

                }
            };
            delete.addEventHandler(MouseEvent.MOUSE_CLICKED, clickDelete);
        }

    }

    private void saveNote(String saveId) {
        try {
            List<String> line = new ArrayList<>();
            File outFile = new File("src/notes/" + saveId + ".txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
            String str;

            for(int i = 0; i < saveIdList.size(); i++) {
                String tempSaveId = saveIdList.get(i);
                if(tempSaveId.equals(saveId)) {
                    TextArea tempTa = taList.get(i);
                    str = tempTa.getText();
                    System.out.println(str);
                    out.write(str);
                }
            }

            out.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("IOIOIO");
        }
    }


}
