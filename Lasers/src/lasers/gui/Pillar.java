package lasers.gui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.HashMap;

public class Pillar extends Button {
    public enum PillarNumbers{
        ZERO("resources/pillar0.png","0"),
        ONE("resources/pillar1.png","1"),
        TWO("resources/pillar2.png","2"),
        THREE("resources/pillar3.png","3"),
        FOUR("resources/pillar4.png","4"),
        X("resources/pillarX.png","X");

        private String image;
        private String number;

        PillarNumbers(String image, String number) {
            this.image = image;
            this.number = number;
        }

        public String getImage() {
            return image;
        }

        public String getNumber() {
            return number;
        }
    }

    private static HashMap<String, ImageView> deck = new HashMap<>();

    Pillar(String number){
        for(PillarNumbers poke : PillarNumbers.values()){
            deck.put(poke.getNumber(),new ImageView(
                    new Image(getClass().getResourceAsStream(poke.getImage()))
            ));
        }
        this.setGraphic(deck.get(number));
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/white.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        this.setBackground(background);
    }

}
