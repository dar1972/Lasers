package lasers.ptui;

import lasers.model.LasersModel;
import lasers.model.ModelData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class ControllerPTUI  {
    /** The UI's connection to the lasers.lasers.model */
    private LasersModel model;
    private ModelData data;
    private String[][] board;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
        this.data = new ModelData(model);
        this.board = model.getBoard();
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) {
        if (inputFile != null){
            try {
                Scanner myReader = new Scanner(new File(inputFile));
                while (myReader.hasNextLine()) {
                    String[] tokens = myReader.nextLine().strip().split(" ");
                    proceed(tokens);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        command();
    }

    /**
     * Runs User Interface
     *      takes user command, calls proceed()
     *      continues to do so while true
     */
    public void command(){
        System.out.println("Enter Command: ");
        Scanner scan = new Scanner(System.in);
        while (true){
            System.out.print(" > ");
            String command = scan.nextLine();
            String[] tokens = command.strip().split(" ");
            if (tokens.length > 3) {
                System.out.println("Too Many Arguments");
            }else {
                proceed(tokens);
            }
        }
    }

    /**
     *  Takes command arguments, calls necessary methods
     * @param tokens command arguments
     */
    public void proceed(String[] tokens){
        switch (tokens[0]) {
            case "a":
            case "add":
                checkNum(tokens, true);
                break;
            case "r":
            case "remove":
                checkNum(tokens, false);
                break;
            case "q":
            case "quit":
                quit();
                break;
            case "v":
            case "verify":
                verify();
                break;
            case "d":
            case "display":
                data.setTheBoard();
                break;
            case "h":
            case "help":
                help();
                break;
            default:
                System.out.println("Unknown Command");
        }
    }

    /**
     * Checks that add and remove arguments are valid numbers
     * @param tokens the command argument with integer values
     * @param adding if true calls add() method, else false calls remove method
     */
    private void checkNum(String[] tokens, boolean adding) {
        int row;
        int col;
        if (tokens.length != 3){
            System.out.println("please enter " + tokens[0] + " with two integers (row,col)");
            return;
        }
        try {
            row = Integer.parseInt(tokens[1]);
            col = Integer.parseInt(tokens[2]);
        }catch (NumberFormatException nfe){
            System.out.println("Can't recognize one or both of these numbers: (" + tokens[1] + ", " + tokens[2] + ")");
            return;
        }
        if (adding){
            add(row, col);
        }else {
            remove(row,col);
        }
    }

    /**
     * Exits the program
     */
    public static void quit(){
        System.out.println("Quitting now");
        System.exit(0);
    }

    /**
     * Print list of valid commands
     */
    public static void help(){
        System.out.println("Commands: ");
        System.out.println("a | add     >   Add to Laser to (r,c)");
        System.out.println("r | remove  >   Remove to Laser to (r,c)");
        System.out.println("v | verify  >   Verify safe correctness");
        System.out.println("h | help    >   Print List Of Commands");
        System.out.println("d | display >   Display safe");
        System.out.println("q | quit    >   Exit Program \n");
    }

    /**
     * Remove Laser at row,col
     * @param row row coordinate
     * @param col col coordinate
     */
    public void remove(int row,int col) {
        data.remove(row,col);
    }

    public void add(int row, int col) {
        data.add(row,col);
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
    public void verify() {
        /*
         check for "."
         */
        for (int row = 0; row < model.getRow(); row++) {
            for (int col = 0; col < model.getCol(); col++) {
                if(board[row][col].equals(".")){
                    System.out.println("Verify ERROR at " +row + " " + col);
                    return;
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
                        System.out.println("Verify ERROR at " +row + " " + col);
                        return;
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
                    System.out.println("Verify ERROR at " +row + " " + col);
                    return;
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
                    System.out.println("Verify ERROR at " +row + " " + col);
                    return;
                }
            }
        }
        System.out.println("Verify Success");
    }
}
