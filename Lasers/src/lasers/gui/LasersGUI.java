package lasers.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;

import lasers.model.*;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the lasers.lasers.model
 * and receives updates from it.
 *
 * @author RIT CS
 */
public class LasersGUI extends Application implements Observer<LasersModel, ModelData> {
    /** The UI's connection to the lasers.lasers.model */
    private LasersModel model;
    private ModelData data;

    private Label message;
    private GridPane laserGrid;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        try {
            Parameters params = getParameters();
            String filename = params.getRaw().get(0);
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    public void addLaser(int row, int col){
        Button laser = getFromGridPane(col,row);
        if (laser instanceof Laser){
            ((Laser)laser).setLaser();
        }
        data.add(row,col);
    }

    public void removeLaser(int row, int col){
        Button laser = getFromGridPane(col,row);
        if (laser instanceof Laser){
            ((Laser)laser).setBlank();
        }
        data.remove(row,col);
    }

    /**
     * Make Grid pane of PokeCards buttons
     * @return grid pane
     */
    private GridPane makeGridPane(){
        GridPane gridPane = new GridPane();
        String[][] board = model.getBoard();
        for (int row=0; row<model.getRow(); ++row) {
            for (int col=0; col<model.getCol(); ++col) {
                Button button;
                if (board[row][col].equals(".")){
                    button = new Laser();
                    int finalRow = row;
                    int finalCol = col;
                    button.setOnAction(event -> {
                        if(!((Laser)button).isOn()){
                            addLaser(finalRow, finalCol);
                        }else{
                            removeLaser(finalRow, finalCol);
                        }
                    });
                }else {
                    button = new Pillar(board[row][col]);
                }
                gridPane.add(button, col, row);
            }
        }

        return gridPane;
    }

    /**
     * Got From Stack Overflow
     * @param col column of button
     * @param row row of button
     * @return button at row,col
     */
    private Button getFromGridPane(int col, int row) {
        for (Node button : laserGrid.getChildren()) {
            if (GridPane.getColumnIndex(button) == col && GridPane.getRowIndex(button) == row) {
                return (Button) button;
            }
        }
        return null;
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * The initialization of all GUI component happens here.
     *
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage) {
        this.data = new ModelData(model);
        BorderPane borderPane = new BorderPane();
        message = new Label(data.getStatus());
        message.setAlignment(Pos.CENTER);
        borderPane.setTop(message);
        laserGrid = makeGridPane();
        borderPane.setCenter(laserGrid);
        HBox hbox = commands(stage);
        borderPane.setBottom(hbox);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
    }

    public void check(){
        String[] point = data.verify().split(" ");
        if (!point[0].equals("A")){
            int row = Integer.parseInt(point[0]);
            int col = Integer.parseInt(point[1]);
            Button button = getFromGridPane(col,row);
            if (button instanceof Laser) {
                ((Laser) button).setError();
            }else if (button instanceof Pillar) {
                setButtonBackground(button, "red.png");
            }
        }
        message.setText(data.getStatus());
    }

    private HBox commands(Stage stage){
        HBox hbox = new HBox();
        Button check = new Button("CHECK");
        check.setOnAction(e -> check());
        Button hint = new Button("HINT");
        // TODO check.setOnAction(e -> {call hint function});
        Button solve = new Button("SOLVE");
        // TODO check.setOnAction(e -> {call solve function});
        Button restart = new Button("RESTART");
        restart.setOnAction(e -> {
            data.refresh();
            message.setText("Game Restart");
        });
        Button load = new Button("LOAD");
        /* TODO check.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select File");
            chooser.setInitialDirectory(new File("e:\\"));
            File file = chooser.showOpenDialog(stage);
            if(file != null){
                message.setText(file.getAbsolutePath() + " selected");
            }
            });
        */
        hbox.getChildren().addAll(check,hint,solve,restart,load);
        return hbox;
    }

    @Override
    public void start(Stage stage) throws Exception {
        // TODO
        init(stage);  // do all your UI initialization here

        stage.setTitle("Lasers GUI");
        stage.show();
    }

    private void updateGrid() {
        for (int row = 0; row < model.getRow(); row++) {
            for (int col = 0; col < model.getCol(); col++) {
                Button button = getFromGridPane(col,row);
                if (button instanceof Laser) {
                    switch (model.getBoard()[row][col]) {
                        case "*":
                            ((Laser) button).setBeam();
                            break;
                        case ".":
                            ((Laser) button).setBlank();
                            break;
                        case "L":
                            setButtonBackground(button, "yellow.png");
                            break;
                    }
                }else if (button instanceof Pillar){
                    setButtonBackground(button, "white.png");
                }
            }
        }
    }

    @Override
    public void update(LasersModel model, ModelData data) {
        this.model = model;
        this.data = data;
        message.setText(data.getStatus());
        Platform.runLater(this::updateGrid);
    }
}
