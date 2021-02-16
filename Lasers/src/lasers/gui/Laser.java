package lasers.gui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class Laser extends Button {
    private boolean on;
    private boolean beam;
    Laser(){
        on = false;
        beam = false;
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resources/white.png"))));
        setButtonBackground(this, "white.png");
    }

    public void setLaser(){
        on = true;
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resources/laser.png"))));
        setButtonBackground(this, "yellow.png");
    }

    public void setBeam(){
        beam = true;
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resources/beam.png"))));
        setButtonBackground(this, "yellow.png");
    }

    public void setBlank(){
        on = false;
        beam = false;
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resources/white.png"))));
    }

    public void setError(){
        beam = false;
        if (!this.isOn()){
            this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resources/red.png"))));
        }
        setButtonBackground(this, "red.png");
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

    public boolean isOn() {
        return on;
    }

    public void Toggle() {
        on = !on;
    }

    public boolean isBeam() {
        return beam;
    }
}
