package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.search.ISearchingAlgorithm;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel{
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private Server generateServer;
    private Server solveServer;


    public MyModel() {
        generateServer= new Server(5400,1000,new ServerStrategyGenerateMaze());
        solveServer= new Server(5401,1000,new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        //call server to generate,with client statagey that return maze
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[])(fromServer.readObject()); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows*cols+18 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        playerRow=0;
        playerCol=0;

        setChanged();
        notifyObservers("maze generated");
        // start position:
        movePlayer(0, 0);
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if (playerRow > 0)
                    if (maze.getM()[playerRow-1][playerCol]!=1)
                        movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < maze.getM().length- 1)
                    if (maze.getM()[playerRow+1][playerCol]!=1)
                        movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0)
                    if (maze.getM()[playerRow][playerCol-1]!=1)
                        movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < maze.getM()[0].length - 1)
                    if (maze.getM()[playerRow][playerCol+1]!=1)
                        movePlayer(playerRow, playerCol + 1);
            }

            case RIGHTUP -> {
                if (playerCol < maze.getM()[0].length - 1 && playerRow>0)
                    if ((maze.getM()[playerRow][playerCol+1]!=1 || maze.getM()[playerRow-1][playerCol]!=1) && maze.getM()[playerRow-1][playerCol+1]!=1 )
                        movePlayer(playerRow-1, playerCol + 1);
            }
            case RIGHTDOWN -> {
                if (playerCol < maze.getM()[0].length - 1 && playerRow<maze.getM().length-1)
                    if ((maze.getM()[playerRow][playerCol+1]!=1 || maze.getM()[playerRow+1][playerCol]!=1) && maze.getM()[playerRow+1][playerCol+1]!=1 )
                        movePlayer(playerRow+1, playerCol + 1);
            }
            case LEFTUP -> {
                if (playerCol > 0 && playerRow>0)
                    if ((maze.getM()[playerRow][playerCol-1]!=1 || maze.getM()[playerRow-1][playerCol]!=1) && maze.getM()[playerRow-1][playerCol-1]!=1 )
                        movePlayer(playerRow-1, playerCol - 1);
            }
            case LEFTDOWN -> {
                if (playerCol >0 && playerRow<maze.getM().length-1)
                    if ((maze.getM()[playerRow][playerCol-1]!=1 || maze.getM()[playerRow+1][playerCol]!=1) && maze.getM()[playerRow+1][playerCol-1]!=1 )
                        movePlayer(playerRow+1, playerCol - 1);
            }
        }

    }

    private void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        if (getPlayerCol() == getMaze().getGoalPosition().getColumnIndex() && getPlayerRow() == getMaze().getGoalPosition().getRowIndex())
            notifyObservers("finish");
        else notifyObservers("player moved");

    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        //solve the maze with server
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        solution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Solution getSolution() {
        return solution;
    }
}
