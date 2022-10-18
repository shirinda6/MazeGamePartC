package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class About extends AView implements Initializable, Observer {
    public javafx.scene.control.Label info;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        info.setWrapText(true);
        info.setText( "Welcome to the Game of Thrones maze game.\n" +
                "In this game you can create maze of the size you want \nAnd try to solve it yourself in an attempt to reach the goal.\n" +
                "We used different algorithms to create the maze:\nEmpty-Maze,Simple-Maze or My-Maze that based on Prim algorithm.\n"+
                "And different search algorithms to solve the maze:\nDepth-First-Search, Breadth-First-Search or Best-First-Search. \n" +
                "By clicking on the option tab you can change the player you want to play with.\nBy clicking on properties you can define which algorithm you want to use.\n"+
                "Hope you enjoy our game \n"+
                "Shir and Nir"
        );
    }

    public void exitButton(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
