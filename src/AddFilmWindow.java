import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.Duration;
import java.io.File;

public class AddFilmWindow extends VBox {

    public AddFilmWindow(){
        // setting spacing and padding
        setSpacing(15);
        setPadding(new Insets(30,75,40,75));

        // creating explanation text
        Text message = new Text("Please give name, relative path of the trailer and duration of the film.");

        // initializing media player of the error sound
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));

        // creating film name row
        TextField nameField = new TextField();
        HBox nameBox = new HBox(new Label("Name:            "), nameField);
        nameBox.setAlignment(Pos.CENTER);

        // creating trailer path row
        TextField trailerPathField = new TextField();
        HBox pathBox = new HBox(new Label("Trailer (Path):  "), trailerPathField);
        pathBox.setAlignment(Pos.CENTER);

        // creating film duration row
        TextField durationField = new TextField();
        HBox durationBox = new HBox(new Label("Duration (m):  "), durationField);
        durationBox.setAlignment(Pos.CENTER);

        // creating a back button to go previous page
        Button backButton = new Button("\u25C0 BACK");
        backButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new WelcomeWindow(User.getLoggedInUser()));
            window.sizeToScene();
            window.centerOnScreen();
        });

        // creating an ok button to confirm
        Label buttonMessage = new Label();
        Button okButton = new Button("OK");
        okButton.setOnAction(e->{
            // getting the information into variables from text fields
            String name = nameField.getText();
            String path = trailerPathField.getText();
            String duration = durationField.getText();
            // checking for errors
            if (name.isEmpty()){
                buttonMessage.setText("ERROR: Film name could not be empty!");
                mediaPlayer.seek(Duration.ZERO); // to play error sound
                mediaPlayer.play();
            } else if (path.isEmpty()){
                buttonMessage.setText("ERROR: Trailer path could not be empty!");
                mediaPlayer.seek(Duration.ZERO); // to play error sound
                mediaPlayer.play();
            } else if (duration.isEmpty()) {
                buttonMessage.setText("ERROR: Duration has to be a positive integer!");
                mediaPlayer.seek(Duration.ZERO); // to play error sound
                mediaPlayer.play();
            } else if (Film.getFilms().containsKey(name)){
                buttonMessage.setText("ERROR: This film name already exists!");
                mediaPlayer.seek(Duration.ZERO); // to play error sound
                mediaPlayer.play();
            } else {
                try {
                    // if duration is not an integer catch block handles
                    int durationInt = Integer.parseInt(duration);

                    // to check if the duration is positive
                    if (durationInt <= 0){
                        buttonMessage.setText("ERROR: Duration has to be a positive integer!");

                        // to play error sound
                        mediaPlayer.seek(Duration.ZERO);
                        mediaPlayer.play();

                    } else {
                        // to check if the trailer exists
                        try {
                            File file = new File(path);
                            if (file.exists()) {
                                Media media = new Media(file.toURI().toString());
                            } else {
                                throw new Exception();
                            }

                            // to add the film if there is no errors
                            new Film(name,path,duration);
                            buttonMessage.setText("SUCCESS: Film added successfully!");

                        } catch (Exception e1){
                            // if the trailer does not exist
                            buttonMessage.setText("ERROR: There is no such a trailer!");

                            // to play error sound
                            mediaPlayer.seek(Duration.ZERO);
                            mediaPlayer.play();
                        }
                    }
                } catch (NumberFormatException er){
                    // if the film duration is not an integer
                    buttonMessage.setText("ERROR: Duration has to be a positive integer!");
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
            }
        });

        // placing buttons next to each other
        HBox buttonBox = new HBox(backButton,okButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(175);

        // adding all elements to the window
        getChildren().addAll(message,nameBox,pathBox,durationBox,buttonBox,buttonMessage);
        setAlignment(Pos.TOP_CENTER);

    }
}
