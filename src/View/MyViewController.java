package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController extends AView implements Initializable, Observer {
    public Pane pane_solve;
    private double startDragX;
    private double startDragY;
    private boolean started=false;
    public MazeDisplayer mazeDisplayer;
    private MediaPlayer music;
    private MediaPlayer music_end;
    private boolean isPlay = false;
    private boolean finished = false;
    public Button solve;
    public RadioButton radioButton;
    public Pane game_pane;
    public VBox box_game;
    public Pane program_pane;
    public javafx.scene.layout.BorderPane BorderPane;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        solve.setVisible(false);
        game_pane.setOnMousePressed(e -> {
            startDragX = e.getSceneX();
            startDragY = e.getSceneY();

        });
        game_pane.setOnMouseDragged(e -> {
            try {
                Thread.sleep(90);
            } catch (InterruptedException interruptedException) {
            }
            viewModel.movePlayer(e , startDragY, startDragX);
        });

        game_pane.prefHeightProperty().bind(program_pane.heightProperty());
        game_pane.prefWidthProperty().bind(program_pane.widthProperty());
        Media sound = new Media(new File("./resources/Game_of_Thrones.mp3").toURI().toString());
        music = new MediaPlayer(sound);
        music.play();
    }

    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "maze solved" -> mazeSolved();
            case "finish" -> finish();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void mazeGenerated() {
        radioButton.setVisible(true);
        if(radioButton.isSelected())
            music.play();
        if (isPlay)
            music_end.stop();
        game_pane.setVisible(true);
        solve.setVisible(true);
        BorderPane.setStyle("-fx-background-image: url(/Images/logo.jpg)");

        solve.setVisible(true);
        program_pane.prefHeightProperty().bind(BorderPane.heightProperty());
        program_pane.prefWidthProperty().bind(BorderPane.widthProperty());
        game_pane.prefHeightProperty().bind(program_pane.heightProperty());
        game_pane.prefWidthProperty().bind(program_pane.widthProperty());
        mazeDisplayer.heightProperty().bind(game_pane.heightProperty());
        mazeDisplayer.widthProperty().bind(game_pane.widthProperty());
        mazeDisplayer.drawMaze(viewModel.getMaze());

    }
    private void playerMoved() {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }
    private void mazeSolved() {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }
    private void finish() {
        //function for finished maze image and music
        game_pane.setVisible(false);
        solve.setVisible(false);
        isPlay=true;
        finished=true;

        if (mazeDisplayer.getImageFileNamePlayer().equals("resources/images/pl.jpg"))
            BorderPane.setStyle("-fx-background-image: url(/Images/endNightKing.jpeg)");
        else if (mazeDisplayer.getImageFileNamePlayer().equals("resources/images/dans.jpg"))
            BorderPane.setStyle("-fx-background-image: url(/Images/endDani.jpg)");
        else if (mazeDisplayer.getImageFileNamePlayer().equals("resources/images/Jons.jpg"))
            BorderPane.setStyle("-fx-background-image: url(/Images/endJohn.jpg)");
        music.stop();
        radioButton.setVisible(false);
        Media sound = new Media(new File("./resources/winning.mp3").toURI().toString());
        music_end = new MediaPlayer(sound);
        music_end.play();
    }


    public void newMaze(ActionEvent actionEvent) throws IOException {
        started=true;
        finished=false;
        switchScene("MazeView.fxml");
    }

    public void saveFile(ActionEvent event) {
        // get the file selected
        if(mazeDisplayer.getMaze()==null || !started || finished){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Error ! for save you need to generate maze first");
            alert.showAndWait();
        }
        else {
            FileChooser fil_chooser = new FileChooser();
            fil_chooser.setTitle("Save maze");
            fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
            fil_chooser.setInitialDirectory(new File("./resources"));
            File file = fil_chooser.showSaveDialog((Stage) mazeDisplayer.getScene().getWindow());
            if (file != null) {
                SaveFile(mazeDisplayer.getMaze(), file);
            }
        }
    }

    private void SaveFile(Maze maze, File file){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath(), true);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(maze);
            objectOutputStream.flush();
            fileOutputStream.close();
            objectOutputStream.close();
        } catch (IOException ex) {
        }

    }

    public void openFile(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        if(chosen!=null) {
            FileInputStream in = new FileInputStream(chosen.getPath());
            ObjectInputStream sol = new ObjectInputStream(in);
            try {
                Maze maze = (Maze) sol.readObject();
                mazeDisplayer.drawMaze(maze);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }


    //functions for switch bitween scene
    public void propscene(ActionEvent actionEvent) throws IOException {
        switchScene("MyProperties.fxml");
    }

    public void aboutscene(ActionEvent actionEvent) throws IOException {
        switchScene("About.fxml");
    }

    public void helpscene(ActionEvent actionEvent) throws IOException {
        switchScene("Help.fxml");
    }



    public void playMusic(ActionEvent actionEvent){
        if(radioButton.isSelected())
            music.play();
        else
            music.stop();
    }

    public void changeSize() {
        if(!started)
            return;
        mazeDisplayer.draw();
    }

    public void zoom(ScrollEvent event) {
        if(event.isControlDown()) {
            game_pane.setOnScroll(new EventHandler<ScrollEvent>() {
                @Override
                public void handle(ScrollEvent event) {
                    double zoomFactor = 1.05;
                    double deltaY = event.getDeltaY();

                    if (deltaY < 0) {
                        zoomFactor = 0.95;
                    }
                    game_pane.setScaleX(game_pane.getScaleX() * zoomFactor);
                    game_pane.setScaleY(game_pane.getScaleY() * zoomFactor);
                    event.consume();
                }
            });
            event.consume();
        }
        event.consume();
    }

    //function for move player after key pressed
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    //function for update player position
    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }
    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    //functions for change player from option tab
    public void changetoDaenerys(ActionEvent actionEvent) {
        mazeDisplayer.changePlayer("Daenerys");
        mazeDisplayer.drawMaze(viewModel.getMaze());

    }
    public void changetoTheNightKing(ActionEvent actionEvent){
        mazeDisplayer.changePlayer("Arya");
        mazeDisplayer.drawMaze(viewModel.getMaze());

    }
    public void changetoJon(ActionEvent actionEvent){
        mazeDisplayer.changePlayer("Jon");
        mazeDisplayer.drawMaze(viewModel.getMaze());

    }

    //function for exit tab
    public static void exitProgram() {
        System.exit(0);
    }
    public void exitProgram(ActionEvent actionEvent) {
        exitProgram();
    }
}
