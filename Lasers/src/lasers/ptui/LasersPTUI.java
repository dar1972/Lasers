package lasers.ptui;

import java.io.FileNotFoundException;

import lasers.model.LasersModel;
import lasers.model.ModelData;
import lasers.model.Observer;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author YOUR NAME HERE
 */
public class LasersPTUI implements Observer<LasersModel, ModelData> {
    /** The UI's connection to the model */
    private LasersModel model;
    private ModelData data;

    /**
     * Construct the PTUI.  Create the lasers.lasers.model and initialize the view.
     * @param filename the safe file name
     * @throws FileNotFoundException if file not found
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        try {
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * Accessor for the model the PTUI create.
     *
     * @return the model
     */
    public LasersModel getModel() { return this.model; }

    /**
     * Display current safe layout
     *      calls printEmpty() method in Safe.java
     */
    public void display(){
        int r = this.model.getRow();
        int c = this.model.getCol();
        StringBuilder print = new StringBuilder("\t  ");
        for (int i = 0; i < c; i++) {
            print.append(i).append(" ");
        }
        print.append("\n\t  ").append("- ".repeat(Math.max(0, c)));
        for (int i = 0; i < r; i++) {
            print.append("\n\t").append(i).append("|");
            for (int j = 0; j < c; j++) {
                print.append(model.getBoard()[i][j]).append(" ");
            }
        }
        System.out.println(print + "\n");
    }

    @Override
    public void update(LasersModel model, ModelData data) {
        this.model = model;
        this.data = data;
        System.out.println(data.getStatus());
        display();
    }
}
