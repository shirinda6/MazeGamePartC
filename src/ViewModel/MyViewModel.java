package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public Maze getMaze(){
        return model.getMaze();
    }

    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);
    }

    public void movePlayer(KeyEvent keyEvent){
        MovementDirection direction;
        switch (keyEvent.getCode()){
            case NUMPAD8, UP -> direction = MovementDirection.UP;
            case NUMPAD2, DOWN -> direction = MovementDirection.DOWN;
            case NUMPAD4, LEFT -> direction = MovementDirection.LEFT;
            case NUMPAD6, RIGHT -> direction = MovementDirection.RIGHT;
            case NUMPAD9 -> direction = MovementDirection.RIGHTUP;
            case NUMPAD7 -> direction = MovementDirection.LEFTUP;
            case NUMPAD3 -> direction = MovementDirection.RIGHTDOWN;
            case NUMPAD1 -> direction = MovementDirection.LEFTDOWN;

            default -> {// no need to move the player...
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    public void movePlayer(MouseEvent mouseEvent, double startDragY, double startDragX) {
        MovementDirection direction;
        double y =  mouseEvent.getSceneY() - startDragY;
        double x = mouseEvent.getSceneX() - startDragX;
        if (y > 0){
            direction = MovementDirection.DOWN;
            model.updatePlayerLocation(direction);
        }
        else if (y < 0){
            direction = MovementDirection.UP;
            model.updatePlayerLocation(direction);
        }
        else if (x > 0){
            direction = MovementDirection.RIGHT;
            model.updatePlayerLocation(direction);
        }
        else if (x < 0){
            direction = MovementDirection.LEFT;
            model.updatePlayerLocation(direction);
        }
    }

    public void solveMaze(){
        model.solveMaze();
    }
}


