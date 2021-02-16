package lasers.model;

import javafx.scene.control.Label;
import lasers.gui.LasersGUI;

/**
 * Use this class to customize the data you wish to send from the model
 * to the view when the model changes state.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class ModelData {

    public enum Status {
        START("Game Start"),
        OOB("Out Of Bounds"),

        ADD_LSR("Adding Laser"),
        REM_LSR("Removing Laser"),
        VER_SUC("Verify Success"),

        VER_ERR("Verifying Error at ( %d, %d )"),
        ADD_ERR("Adding Error at ( %d, %d )"),
        REM_ERR("Removing Error at ( %d, %d )");

        private String message;

        Status(String message){
            this.message = message;
        }

        public String getMessage(){
            return message;
        }

    }
    private String status;
    private LasersModel model;
    private String[][] board;

    public ModelData(LasersModel model){
        this.model = model;
        this.board = model.getBoard();
        status = Status.START.getMessage();
    }

    public String getStatus(){
        return status;
    }

    public void refresh(){
        for (int row = 0; row < model.getRow(); row++) {
            for (int col = 0; col < model.getCol(); col++) {
                if ((!board[row][col].matches("[0-9]")) && !(board[row][col].equals("X"))){
                    board[row][col] = ".";
                }
            }
        }
        model.notifyObservers(this);
    }

    /**
     * removes a laser from a coordinate
     * @param row the row to remove the laser from
     * @param col the column to remove the laser from
     */
    public void remove(int row,int col) {
        if (row >= model.getRow() || col >= model.getCol() || row < 0 || col < 0){
            status=Status.OOB.getMessage();
        }
        else if (board[row][col].equals("L")){
            board[row][col] = ".";
            removeBeam(row, col);
            for (int rows = 0; rows < model.getRow(); rows++){
                for (int cols = 0; cols < model.getCol(); cols++){
                    if (board[rows][cols].equals("L")){
                        addBeam(rows, cols);
                    }
                }
            }
            status=Status.REM_LSR.getMessage();
        }
        else {
            status=String.format(Status.REM_ERR.getMessage(),row,col);
        }
        setTheBoard();
    }

    /**
     * adds a laser to a coordinate
     * @param row the row for the laser to be placed at
     * @param col the column for the laser to be placed at
     */
    public void add(int row, int col) {
        if (row >= model.getRow() || col >= model.getCol() || row < 0 || col < 0){
            status=Status.OOB.getMessage();
        }
        else if ((!board[row][col].matches("[0-9]")) && !(board[row][col].equals("X"))
                && !(board[row][col].equals("L"))){
            board[row][col] = "L";
            status=Status.ADD_LSR.getMessage();
            addBeam(row, col);
        }
        else {
            status=String.format(Status.ADD_ERR.getMessage(),row,col);
        }
        setTheBoard();
    }

    /**
     * removes the beams from a laser that was removed
     * @param row the row from where laser was removed
     * @param col the column from where laser was removed
     */
    public void removeBeam(int row , int col){
        if (row <  model.getRow()-1) {
            for (int rows = row + 1; rows < model.getRow(); rows++) {
                if (!(board[rows][col].matches("[0-9]")) && !(board[rows][col].equals("X"))
                        && !board[rows][col].equals("L")) {
                    board[rows][col] = ".";
                } else {
                    break;
                }
            }
        }
        if (row > 0) {
            for (int rows = row - 1; rows >= 0; rows--) {
                if (!(board[rows][col].matches("[0-9]")) && !(board[rows][col].equals("X"))
                        && !board[rows][col].equals("L")) {
                    board[rows][col] = ".";
                } else {
                    break;
                }
            }
        }
        if (col < model.getCol()-1) {
            for (int cols = col + 1; cols < model.getCol(); cols++) {
                if (!(board[row][cols].matches("[0-9]")) && !(board[row][cols].equals("X"))
                        && !board[row][cols].equals("L")) {
                    board[row][cols] = ".";
                } else {
                    break;
                }
            }
        }
        if (col > 0) {
            for (int cols = col - 1; cols >= 0; cols--) {
                if (!(board[row][cols].matches("[0-9]")) && !(board[row][cols].equals("X"))
                        && !board[row][cols].equals("L")) {
                    board[row][cols] = ".";
                } else {
                    break;
                }
            }
        }
    }

    /**
     * adds the beams for a laser at a coordinate
     * @param row the row from where laser comes
     * @param col the column from where laser comes
     */
    public void addBeam(int row , int col){
        if (row < model.getRow()-1) {
            for (int rows = row + 1; rows < model.getRow(); rows++) {
                if (!(board[rows][col].matches("[0-9]")) && !(board[rows][col].equals("X"))
                        && !board[rows][col].equals("L")) {
                    board[rows][col] = "*";
                } else {
                    break;
                }
            }
        }
        if (row > 0) {
            for (int rows = row - 1; rows >= 0; rows--) {
                if (!(board[rows][col].matches("[0-9]")) && !(board[rows][col].equals("X"))
                        && !board[rows][col].equals("L")) {
                    board[rows][col] = "*";
                } else {
                    break;
                }
            }
        }
        if (col < model.getCol()-1) {
            for (int cols = col + 1; cols < model.getCol(); cols++) {
                if (!(board[row][cols].matches("[0-9]")) && !(board[row][cols].equals("X"))
                        && !board[row][cols].equals("L")) {
                    board[row][cols] = "*";
                } else {
                    break;
                }
            }
        }
        if (col > 0) {
            for (int cols = col - 1; cols >= 0; cols--) {
                if (!(board[row][cols].matches("[0-9]")) && !(board[row][cols].equals("X"))
                        && !board[row][cols].equals("L")) {
                    board[row][cols] = "*";
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Verifies that the board follows the rules
     *      1) All space must have either:
     *             - Pillar
     *             - laser
     *             -laser beam
     *      2) No laser is in direct line of sight of each other
     *      3) Every pillar has the required number of lasers attached
     *              no more, no less
     */
    public String verify() {
        /*
         check for "."
         */
        for (int row = 0; row < model.getRow(); row++) {
            for (int col = 0; col < model.getCol(); col++) {
                if(board[row][col].equals(".")){
                    status=String.format(Status.VER_ERR.getMessage(),row,col);
                    return row + " " + col;
                }
            }
        }
        /*
        Check Pillars have right number of Lasers
         */
        for (int row = 0; row < model.getRow(); row++) {
            for (int col = 0; col < model.getCol(); col++) {
                if(board[row][col].matches("[0-9]")){
                    int pillar = Integer.parseInt(board[row][col]);
                    int laser = 0;
                    if(row == 0 && col == 0){
                        if(board[row+1][col].equals("L")){
                            laser++;
                        }
                        if(board[row][col+1].equals("L")){
                            laser++;
                        }
                    }else if (row == 0 && col == model.getCol()-1){
                        if(board[row+1][col].equals("L")){
                            laser++;
                        }
                        if(board[row][col-1].equals("L")){
                            laser++;
                        }
                    }else if(row == model.getRow()-1 && col == 0){
                        if(board[row-1][col].equals("L")){
                            laser++;
                        }
                        if(board[row][col+1].equals("L")){
                            laser++;
                        }
                    }else if (row == model.getRow()-1 && col == model.getCol()-1){
                        if(board[row-1][col].equals("L")){
                            laser++;
                        }
                        if(board[row][col-1].equals("L")){
                            laser++;
                        }
                    }else{
                        if(row != model.getRow()-1 && board[row+1][col].equals("L")){
                            laser++;
                        }
                        if(col != model.getCol()-1 && board[row][col+1].equals("L")){
                            laser++;
                        }
                        if(row != 0 && board[row-1][col].equals("L")){
                            laser++;
                        }
                        if(col != 0 && board[row][col-1].equals("L")){
                            laser++;
                        }
                    }
                    if(!(laser == pillar)){
                        status=String.format(Status.VER_ERR.getMessage(),row,col);
                        return row + " " + col;
                    }
                }
            }
        }
        /*
        check for lasers are in sight of each other
         */
        int explode;
        for (int row = 0; row < model.getRow(); row++) {
            explode = 0;
            for (int col = 0; col < model.getCol(); col++) {
                if((board[row][col].matches("[0-9]")) || (board[row][col].equals("X"))){
                    explode = 0;
                }else if(board[row][col].equals("L")){
                    explode++;
                }
                if (explode > 1){
                    status=String.format(Status.VER_ERR.getMessage(),row,col);
                    return row + " " + col;
                }
            }
        }
        for (int col = 0; col < model.getCol(); col++) {
            explode = 0;
            for (int row = 0; row < model.getRow(); row++) {
                if((board[row][col].matches("[0-9]")) || (board[row][col].equals("X"))){
                    explode = 0;
                }else if(board[row][col].equals("L")){
                    explode++;
                }
                if (explode > 1){
                    status=String.format(Status.VER_ERR.getMessage(),row,col);
                    return row + " " + col;
                }
            }
        }
        status=Status.VER_SUC.getMessage();
        return "A B";
    }

    public void setTheBoard(){
        model.setBoard(board);
        model.notifyObservers(this);
    }

}