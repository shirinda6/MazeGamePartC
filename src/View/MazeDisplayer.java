
package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    private Maze maze;
    private Solution solution;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    // wall,player,road and goal images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();
    StringProperty imageFileNameSol = new SimpleStringProperty();
    StringProperty imageFileNameRoad = new SimpleStringProperty();
    StringProperty imageFileNameRoadJon = new SimpleStringProperty();
    StringProperty imageFileNameRoadDan = new SimpleStringProperty();
    StringProperty imageFileNameDaenerys = new SimpleStringProperty();
    StringProperty imageFileNamePlayerArya = new SimpleStringProperty();
    StringProperty imageFileNamePlayerJon = new SimpleStringProperty();

    public InputStream getImage(String image){
        return getClass().getClassLoader().getResourceAsStream(image);
    }
    public Maze getMaze() {
        return maze;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public String getImageFileNameRoadJon() {
        return imageFileNameRoadJon.get();
    }

    public StringProperty imageFileNameRoadJonProperty() {
        return imageFileNameRoadJon;
    }

    public void setImageFileNameRoadJon(String imageFileNameRoadJon) {
        this.imageFileNameRoadJon.set(imageFileNameRoadJon);
    }

    public String getImageFileNameRoadDan() {
        return imageFileNameRoadDan.get();
    }

    public StringProperty imageFileNameRoadDanProperty() {
        return imageFileNameRoadDan;
    }

    public void setImageFileNameRoadDan(String imageFileNameRoadDan) {
        this.imageFileNameRoadDan.set(imageFileNameRoadDan);
    }

    public String getImageFileNameDaenerys() {
        return imageFileNameDaenerys.get();
    }

    public String imageFileNameDaenerysProperty() {
        return imageFileNameDaenerys.get();
    }

    public void setImageFileNameDaenerys(String imageFileNameDaenerys) {
        this.imageFileNameDaenerys.set(imageFileNameDaenerys);
    }

    public String getImageFileNameArya() {
        return imageFileNamePlayerArya.get();
    }

    public String imageFileNameAryaProperty() {
        return imageFileNamePlayerArya.get();
    }

    public void setImageFileNameArya(String imageFileNameArya) {
        this.imageFileNamePlayerArya.set(imageFileNameArya);
    }

    public String getImageFileNameJon() {
        return imageFileNamePlayerJon.get();
    }

    public String imageFileJonPlayerProperty() {
        return imageFileNamePlayerJon.get();
    }

    public void setImageFileNameJon(String imageFileNamePlayerJon) {
        this.imageFileNamePlayerJon.set(imageFileNamePlayerJon);
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameSol() {
        return imageFileNameSol.get();
    }

    public String imageFileNameSolProperty() {
        return imageFileNameSol.get();
    }

    public void setImageFileNameSol(String imageFileNameSol) {
        this.imageFileNameSol.set(imageFileNameSol);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public String getImageNameGoal() {
        return imageFileNameGoal.get();
    }

    public String imageFileGoalNameProperty() {
        return imageFileNameGoal.get();
    }

    public void setImageNameGoal(String imageFileNamePlayer) {
        this.imageFileNameGoal.set(imageFileNamePlayer);
    }

    public String getImageFileNameRoad() {
        return imageFileNameRoad.get();
    }

    public void setImageFileNameRoad(String imageFileNameRoad) {
        this.imageFileNameRoad.set(imageFileNameRoad);
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();
    }

    public void drawMaze(Maze maze) {
        this.maze = maze;
        solution=null;
        draw();
    }

    public void draw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getM().length;
            int cols = maze.getM()[0].length;
            double cellHeight = (canvasHeight / rows);
            double cellWidth = (canvasWidth / cols) * 0.85;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            if (solution != null)
                drawSolution(graphicsContext, cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawGoal(graphicsContext, cellHeight, cellWidth);
        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        // lonked list of location -draw solution
        graphicsContext.setFill(Color.RED);
        AState p;
        int row;
        int col;
        Image solImage = null;
        solImage = new Image(getImage("images/sol.jpg"));

        ArrayList<AState> mazeSolutionSteps = solution.getSolutionPath();
        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
            p = mazeSolutionSteps.get(i);
            row = ((MazeState) p).getCurrent_pos().getRowIndex();
            col = ((MazeState) p).getCurrent_pos().getColumnIndex();
            graphicsContext.drawImage(solImage, col * cellWidth, row * cellHeight, cellWidth, cellHeight);
        }
//        System.out.println("drawing solution...");
    }

    private void drawGoal(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        graphicsContext.setFill(Color.RED);
        Image goal = null;
        goal = new Image(getImage("images/thrones.jpg"));

        double r = maze.getGoalPosition().getRowIndex() * cellHeight;
        double c = maze.getGoalPosition().getColumnIndex() * cellWidth;
        graphicsContext.drawImage(goal, c, r, cellWidth, cellHeight);
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);
        Image wallImage = null;
        Image roadImage = null;
        wallImage = new Image(getImage(getImageFileNameWall().substring(10)));
        roadImage = new Image(getImage(getImageFileNameRoad().substring(10)));


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze.getM()[i][j] == 1) {
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if (wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                } else {
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if (roadImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(roadImage, x, y, cellWidth, cellHeight);

                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);
        Image playerImage = null;
        playerImage = new Image(getImage(getImageFileNamePlayer().substring(10)));

        if (playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    public void changePlayer(String name) {
        solution=null;
        if (name.equals("Daenerys")) {
            setImageFileNamePlayer(getImageFileNameDaenerys());
            setImageFileNameRoad(getImageFileNameRoadDan());
            setImageFileNameWall("resources/Images/stone.jpg");
        } else if (name.equals("Arya")) {
            setImageFileNamePlayer(getImageFileNameArya());
            setImageFileNameRoad(getImageFileNameRoadJon());
            setImageFileNameWall("resources/Images/ice.jpg");
        } else if (name.equals("Jon")) {
            setImageFileNamePlayer(getImageFileNameJon());
            setImageFileNameRoad(getImageFileNameRoadJon());
            setImageFileNameWall("resources/Images/wall.jpg");
        }
    }
}