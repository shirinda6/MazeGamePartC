package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Help extends AView implements Initializable, Observer {
    public javafx.scene.control.Label help;

    public void update(Observable o, Object arg) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        help.setWrapText(true);
        help.setText( "To start playing you need to create a maze, click on the File tab and then New \nAnd create a maze according to the size you decide, " +
                "(Note that the minimum size is 2,2).\n" +
                "The maze you created can be saved by clicking the File and Save tab.\n" +
                "Or load a maze you previously saved by clicking File and Load\n" +
                "You can influence the size of the window by pressing ctrl and dragging the mouse.\n" +
                "You can try to solve the maze yourself in an attempt to reach the destination,\n" +
                "This can be done by dragging the character or by pressing the appropriate scroll keys:\n" +
                "2->UP,8->DOWN,4->LEFT,6->RIGHT,3->RIGHT+UP,1->LEFT+UP,9->RIGHT+DOWN,7->LEFT+DOWN.\n"+
                "In addition there is an option to press the solve button and get the shortest way to the destination.\n" +
                "Hope you enjoy our game,\n"+
                "Shir and Nir"
        );
    }

    public void exitButton(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
