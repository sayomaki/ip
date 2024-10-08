package alexer.ui;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;

public class MessageBox extends HBox {
    @FXML
    private Label text;

    @FXML
    private ImageView picture;

    private MessageBox(String message, Image displayPicture) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/MessageBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        text.setText(message);
        picture.setImage(displayPicture);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> nodes = FXCollections.observableArrayList(getChildren());
        FXCollections.reverse(nodes);
        getChildren().setAll(nodes);
        getStyleClass().add("reversed");
        text.getStyleClass().add("reversed-label");
    }

    private void setColor(Response.ResponseType type) {
        if (type == Response.ResponseType.ERROR) text.getStyleClass().add("error");
    }

    public void animateText() {
        String content = text.getText();
        int duration = content.length() > 300 ? 3000 : content.length() > 80 ? 1500 : 750;
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(duration));
            }

            protected void interpolate(double frac) {
                final int length = content.length();
                final int n = Math.round(length * (float) frac);
                text.setText(content.substring(0, n));
            }
        };

        animation.play();
    }


    /**
     * Creates a message box originating from the user (user input).
     * @param s the string to display
     * @param i the user avatar image
     * @return a message box to be displayed
     */
    public static MessageBox createUserDialog(String s, Image i) {
        return new MessageBox(s, i);
    }

    /**
     * Creates a message box originating from the bot (bot response).
     * @param response the response instance
     * @param i the bot avatar image
     * @return a message box to be displayed
     */
    public static MessageBox createBotDialog(Response response, Image i) {
        MessageBox msgBox = new MessageBox(response.response, i);
        msgBox.setColor(response.type);
        msgBox.flip();
        return msgBox;
    }
}
