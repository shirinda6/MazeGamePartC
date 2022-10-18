package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Game Of Thrones");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.setOnCloseRequest((event)-> MyViewController.exitProgram());
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> view.changeSize();
        primaryStage.getScene().widthProperty().addListener(stageSizeListener);
        primaryStage.getScene().heightProperty().addListener(stageSizeListener);
        view.setViewModel(viewModel);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
