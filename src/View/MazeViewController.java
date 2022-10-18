package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MazeViewController extends AView implements Initializable, Observer {
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;

    //after click on generate maze button
    public void generateMaze(ActionEvent actionEvent){
        if (textField_mazeRows.getText().isEmpty() || textField_mazeColumns.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Error ! You need to declare the maze size.");
            alert.showAndWait();
        }
        else {
            int rows = Integer.valueOf(textField_mazeRows.getText());
            int cols = Integer.valueOf(textField_mazeColumns.getText());
            if (rows < 2 || cols < 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Look, an Error Dialog");
                alert.setContentText("Error ! The minimum rows,columns size is two");
                alert.showAndWait();
            } else {
                viewModel.generateMaze(rows, cols);
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            }
        }
    }
    public void cancelButtom(ActionEvent actionEvent){
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void update(Observable o, Object arg) {
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
