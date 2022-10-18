package View;

import Server.Configurations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyPropertiesController extends AView implements Initializable, Observer {
    @FXML
    private ChoiceBox algorithms;
    @FXML
    private ChoiceBox generates;
    private static Configurations config;
    public TextField threads;



    @Override
    public void update(Observable o, Object arg) {

    }

    public void changeSetting(ActionEvent actionEvent){
        try {
            config = Configurations.getInstance();
            if (threads.getText()==null || threads.getText().compareTo("2")!=1 ) {
                config.setProperties("threadPoolSize", "2");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Look, an Information Dialog");
                alert.setContentText("False insertion, Thread Pool Size is update to 2");
                alert.showAndWait();
            }
            else
                config.setProperties("threadPoolSize", threads.getText());
            config.setProperties("mazeGeneratingAlgorithm", generates.getValue().toString());
            config.setProperties("mazeSearchingAlgorithm", algorithms.getValue().toString());
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("Configuration is updated successfully");
            alert.showAndWait();
        } catch (IOException e) {
            //todo pop up alert
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        algorithms.getItems().removeAll(algorithms.getItems());
        algorithms.getItems().addAll("BestFirstSearch", "BreadthFirstSearch", "DepthFirstSearch");
        algorithms.getSelectionModel().select("BreadthFirstSearch");

        generates.getItems().removeAll(generates.getItems());
        generates.getItems().addAll("MyMazeGenerator", "SimpleMazeGenerator", "EmptyMazeGenerator");
        generates.getSelectionModel().select("MyMazeGenerator");
    }

    public void cancel(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
