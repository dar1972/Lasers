package lasers.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * The model of the lasers safe.  You are free to change this class however
 * you wish, but it should still follow the MVC architecture.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class LasersModel {
    /** the observers who are registered with this model */
    private List<Observer<LasersModel, ModelData>> observers;
    private String[][] board;
    private final String[][] solution;

    private final int row;
    private final int col;

    public LasersModel(String filename) throws FileNotFoundException {
        this.observers = new LinkedList<>();
        ArrayList<String> fileLines = readFile(filename);
        String[] sze = fileLines.get(0).split(" ");
        row = Integer.parseInt(sze[0]);
        col = Integer.parseInt(sze[1]);

        board = new String[row][col];
        //empty = new String[row][col];
        solution = new String[row][col];

        ArrayList<String[]> empty = new ArrayList<>();
        ArrayList<String[]> solved = new ArrayList<>();
        for (int i = 1; i <= row; i++) {
            empty.add(fileLines.get(i).split(" "));
        }
        for (int i = row+4; i < fileLines.size(); i++) {
            solved.add(fileLines.get(i).substring(2).split(" "));
        }
        createBoards(empty, solved);
    }

    public void createBoards(ArrayList<String[]> eboard, ArrayList<String[]> sBoard){
        for(int r = 0; r < row; r++){
            String[] line = eboard.get(r);
            System.arraycopy(line, 0, board[r], 0, line.length);
        }
        for(int r = 0; r < sBoard.size(); r++){
            String[] line = sBoard.get(r);
            System.arraycopy(line, 0, solution[r], 0, line.length);
        }
    }

    public void setBoard(String[][] board){
        this.board = board;
    }

    public String[][] getBoard(){
        return board;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    /**
     * Reads the safe file, creates ArrayList<String> of the file's lines
     * @param filename name of file to be read
     * @return ArrayList<String> of the file's lines
     */
    public static ArrayList<String> readFile(String filename) throws FileNotFoundException {
        ArrayList<String> fileLines = new ArrayList<>();
        Scanner myReader = new Scanner(new File(filename));
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if (!data.equals("")){
                fileLines.add(data);
            }
        }
        myReader.close();
        return fileLines;
    }

    /**
     * Add a new observer.
     *
     * @param observer the new observer
     */
    public void addObserver(Observer<LasersModel, ModelData > observer) {
        this.observers.add(observer);
    }

    /**
     * Notify observers the model has changed.
     *
     * @param data optional data the model can send to the view
     */
    void notifyObservers(ModelData data){
        for (Observer<LasersModel, ModelData> observer: observers) {
            observer.update(this, data);
        }
    }

    public void add(int row, int col) throws LaserException {
        isChanged = true;
        if (row >= r || col >= c || row < 0 || col < 0){
            throw new LaserException(String.format(OOB_ERR, row, col));
        }
        else if (!(safe[row][col].matches("[0-9]")) && !(safe[row][col].equals("X"))
                && !(safe[row][col].equals("L"))){
            safe[row][col] = "L";
            msg = String.format(ADD_LSR,row,col);
            addBeam(row, col);
            System.out.println(msg);
        }
        else {
            throw new LaserException(String.format(ADD_ERR, row, col));
        }
    }

}