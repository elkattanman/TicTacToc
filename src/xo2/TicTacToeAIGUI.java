/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xo2;

import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Mustafa Khaled
 */
class Turn {

    enum NextMove {

        O, X, E
    }
    NextMove next;
}

public class TicTacToeAIGUI extends Application {

    Board board = new Board();
    Turn boardTurn = new Turn();
    GridPane grid;
    Label cell1, cell2, cell3,
            cell4, cell5, cell6,
            cell7, cell8, cell9;
    Label[] cells;

    @Override
    public void start(Stage primaryStage) {

        Stage stage = new Stage();
        GridPane g = new GridPane();
        g.setId("firstDialog");
        g.setPadding(new Insets(20, 20, 20, 20));
        g.setVgap(20);
        g.setHgap(20);

        //First Dialog Labels and Buttons
        Label label = new Label("Who play first?");
        Button IWillPlay = new Button("Me play first!");
        Button YouPlay = new Button("You play first!");
        g.add(label, 0, 0, 2, 1);
        g.add(IWillPlay, 0, 1, 1, 1);
        g.add(YouPlay, 1, 1, 1, 1);

        //Scene for the firstDialog
        Scene sc = new Scene(g, 450, 200);
        g.setAlignment(Pos.CENTER);
        sc.getStylesheets().addAll(this.getClass().getResource("firstDialog.css").toExternalForm());
        stage.setTitle("The turn");
        stage.setScene(sc);
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        //Board scene
        GridPane grid = new GridPane();
        cell1 = new Label();
        cell2 = new Label();
        cell3 = new Label();
        cell4 = new Label();
        cell5 = new Label();
        cell6 = new Label();
        cell7 = new Label();
        cell8 = new Label();
        cell9 = new Label();

        cells = new Label[]{cell1, cell2, cell3,
            cell4, cell5, cell6,
            cell7, cell8, cell9};

        for (Label cell : cells) {
            cell.setMinSize(128, 128);
            boolean isUsed = false;
            cell.setUserData(isUsed);
        }

        grid.addRow(0, cell1, cell2, cell3);
        grid.addRow(1, cell4, cell5, cell6);
        grid.addRow(2, cell7, cell8, cell9);

        grid.setAlignment(Pos.CENTER);
        grid.setMaxSize(800, 800);
        grid.setGridLinesVisible(true);
        grid.setId("board");

        boardTurn.next = Turn.NextMove.O;
        Image OPic = new Image(getClass().getResourceAsStream("O.png"));
        Image XPic = new Image(getClass().getResourceAsStream("X.png"));

        for (Label cell : cells) {

            cell.setOnMouseClicked(event -> {
                if (((boolean) cell.getUserData()) == false) {
                    cell.setGraphic(new ImageView(XPic));

                    int index = -1;
                    for (int i = 0; i < cells.length; ++i) {
                        if (cell == cells[i]) {
                            index = i;
                        }
                    }

                    board.placeAMove(new Point(index / 3, index % 3), 2);
                    board.displayBoard();
                    System.out.println("Placed a move at: (" + index / 3 + ", " + index % 3 + ")");
                    boolean mark = true;
                    int next = board.returnNextMove();

                    if (next != -1) {   //If the game isn't finished yet!   
                        int indexCell = next;

                        cells[indexCell].setGraphic(new ImageView(OPic));
                        cells[indexCell].setUserData(mark); //Used!
                        System.out.println("Computer's move! " + indexCell);
                        board.placeAMove(new Point(indexCell / 3, indexCell % 3), 1);
                        cell.setUserData(mark);
                    }

                    if (board.isGameOver()) {
                        Stage stage2 = new Stage();
                        GridPane g2 = new GridPane();
                        g2.setPadding(new Insets(20, 20, 20, 20));
                        g2.setVgap(20);
                        g2.setHgap(20);
                        Label label2 = new Label();
                        if (board.hasXWon()){
                            label2.setText(" GG ");
                            stage2.setTitle("You lost!");
                        } else {
                            label2.setText(" No one of us will win today! ): ");
                            stage2.setTitle("It's a draw!");
                        }
                        g2.add(label2, 0, 0, 2, 1);
                        Button onceMore = new Button("Play again!");
                        Button quit = new Button("I quit!");
                        g2.add(onceMore, 1, 1, 1, 1);
                        g2.add(quit, 2, 1, 1, 1);
                        onceMore.setOnMouseClicked(q -> {
                            primaryStage.close();
                            stage2.close();
                            board.resetBoard();
                            start(new Stage());
                        });

                        quit.setOnMouseClicked(q -> {
                            System.exit(0);
                        });
                        Scene scene = new Scene(g2);
                        scene.getStylesheets().addAll(this.getClass().getResource("result.css").toExternalForm());
                        stage2.setScene(scene);
                        stage2.setOnCloseRequest(q -> {
                            primaryStage.close();
                        });
                        stage2.show();
                    }
                }
            });
        };

        Scene scene = new Scene(grid);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);

        scene.getStylesheets().addAll(this.getClass().getResource("board.css").toExternalForm());

        //FirstWindow Action Listeners
        IWillPlay.setOnMouseClicked((event) -> {
            boardTurn.next = Turn.NextMove.X;
            stage.close();
        });

        YouPlay.setOnMouseClicked((event) -> {
            int index = new Random().nextInt(9);
            cells[index].setGraphic(new ImageView(OPic));
            cells[index].setUserData(new Boolean(true));
            board.placeAMove(new Point(index / 3, index % 3), 1);
            boardTurn.next = Turn.NextMove.X;
            stage.close();
        });
        stage.showAndWait();  //Tag1 
        /*
         The placement position of this line (tag1) is important.
         If you place this line above the listeners, the listeners 
         aren't gonna work
         */
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}