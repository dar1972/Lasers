package lasers.backtracking;

import lasers.model.LasersModel;
import lasers.model.ModelData;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the lasers.model
 * package and/or incorporate it into another class.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class SafeConfig implements Configuration {

    private int row;
    private int col;
    private String[][] Safe;
    private LasersModel model;

    public SafeConfig(String filename) {
        try{
            this.model=new LasersModel(filename);
        }
        catch (FileNotFoundException e){
            System.out.println("Error: file '"+filename+"' not found.");
        }
    }

    public SafeConfig(LasersModel copy){
        this.model = copy;
    }


    @Override
    public Collection<Configuration> getSuccessors() {
        LinkedList<Configuration> successors = new LinkedList<>();
        int r=0;
        while (r<row){
            int c=0;
            while (c<col){
                if(Safe[r][c].equals(".")){
                    LasersModel copy=this.model;
                    copy.add(r,c);
                    SafeConfig successor=new SafeConfig(copy);
                    successors.add(successor);
                }
                c++;
            }
            r++;
        }
        return successors;
    }

    private boolean validity(int r, int c){
        int empty=0;
        int lasers=0;
        if(r>0){
            if (this.model.getBoard()[r-1][c].equals("L")){
                lasers+=1;
            }
            else if(this.model.getBoard()[r-1][c].equals(".")){
                empty+=1;
            }
        }
        if(r<this.model.getRow()-1){
            if(this.model.getBoard()[r+1][c].equals("L")){
                lasers+=1;
            }
            else if(this.model.getBoard()[r+1][c].equals(".")){
                empty+=1;
            }
        }
        if(c>0){
            if(this.model.getBoard()[r][c-1].equals("L")){
                lasers+=1;
            }
            else if(this.model.getBoard()[r][c-1].equals(".")){
                empty+=1;
            }
        }
        if(c<this.model.getCol()-1){
            if(this.model.getBoard()[r][c+1].equals("L")){
                lasers+=1;
            }
            else if(this.model.getBoard()[r][c+1].equals(".")){
                empty+=1;
            }
        }
        switch (this.model.getBoard()[r][c]){
            case "0":
                if(lasers>0){
                    return false;
                }
                return true;
            case "1":
                if(lasers>1){
                    return false;
                }
                else if (lasers+empty<1){
                    return false;
                }
                return true;
            case "2":
                if(lasers>2){
                    return false;
                }
                else if(lasers+empty<2){
                    return false;
                }
                return true;
            case "3":
                if(lasers>3){
                    return false;
                }
                else if(lasers+empty<3){
                    return false;
                }
                return true;
            case "4":
                if(lasers>4){
                    return false;
                }
                else if(lasers+empty<4){
                    return false;
                }
                return true;
        }
        return true;
    }

    @Override
    public boolean isValid() {
        boolean freeSpace=false;
        int yCounter=0;
        while (yCounter<this.model.getRow()){
            int xCounter=0;
            while (xCounter<this.model.getCol()){
                switch (this.model.getBoard()[yCounter][xCounter]){
                    case "0":
                        return validity(yCounter,xCounter);
                    case "1":
                        return validity(yCounter,xCounter);
                    case "2":
                        return validity(yCounter,xCounter);
                    case "3":
                        return validity(yCounter,xCounter);
                    case "4":
                        return validity(yCounter,xCounter);
                    case ".":
                        freeSpace=true;
                }
                xCounter++;
            }
            yCounter++;
        }
        if(this.isGoal()){
            return true;
        }
        return freeSpace;
    }

    @Override
    public boolean isGoal() {
        for (int r = 0; r < row; r++){
            for (int c = 0 ; c < col; c++){
                if (Safe[r][c].equals(".")){
                    return false;
                }
            }
        }
        return true;
    }
}