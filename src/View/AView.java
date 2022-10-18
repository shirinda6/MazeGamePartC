package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observer;

public abstract class AView implements IView , Observer {
    protected MyViewModel viewModel;

    protected void switchScene(String fxmlName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = fxmlLoader.load();
        AView view = fxmlLoader.getController();
        view.setViewModel(this.viewModel);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        int i = fxmlName.indexOf(".");
        String s = fxmlName.substring(0,i);
        stage.setTitle(s);

        stage.setScene(scene);
        stage.show();

    }

    protected void setViewModel(MyViewModel viewModel){
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }
    protected MyViewModel getViewModel(){
        return viewModel;

    }

}
