import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Window;
import javafx.util.Duration;
import java.io.File;

public class FilmWindow extends GridPane {

    public FilmWindow(Film film){
        // setting gaps and padding
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(20,50,20,50));

        // to initialize the media view of the trailer
        Media trailerM = new Media(new File(film.getTrailerPath()).toURI().toString());
        MediaPlayer trailerMP = new MediaPlayer(trailerM);
        MediaView trailer = new MediaView(trailerMP);

        // to display film information
        Label label = new Label(film.getName()+" ("+film.getDuration()+" minutes)");

        // to create a combo box with halls of the film
        ComboBox<Hall> hallComboBox = new ComboBox<>(FXCollections.observableArrayList(film.getHalls().values()));
        hallComboBox.getSelectionModel().selectFirst();

        // creating a back button to go previous page
        Button backButton = new Button("\u25C0 Back");
        backButton.setOnAction(e->{
            trailer.getMediaPlayer().pause();
            Window window = getScene().getWindow();
            getScene().setRoot(new WelcomeWindow(User.getLoggedInUser()));
            window.sizeToScene();
            window.centerOnScreen();
        });
        // creating an add hall button to go add hall window
        Button addHAllButton = new Button("Add Hall");
        addHAllButton.setOnAction(e->{
            trailer.getMediaPlayer().pause();
            Window window = getScene().getWindow();
            getScene().setRoot(new AddHallWindow(film));
            window.sizeToScene();
            window.centerOnScreen();
        });
        // creating a remove hall button to go remove hall window
        Button removeHallButton = new Button("Remove Hall");
        removeHallButton.setOnAction(e->{
            trailer.getMediaPlayer().pause();
            Window window = getScene().getWindow();
            getScene().setRoot(new RemoveHallWindow(film));
            window.sizeToScene();
            window.centerOnScreen();
        });
        // creating an ok button to confirm the selection and go hall window
        Button okButton = new Button("OK");
        okButton.setOnAction(e->{
            trailer.getMediaPlayer().pause();
            Window window = getScene().getWindow();
            try {
                getScene().setRoot(new HallWindow(film, hallComboBox.getSelectionModel().getSelectedItem()));
            }catch (NullPointerException e1){}
            window.sizeToScene();
            window.centerOnScreen();
        });

        // placing buttons next to each other
        HBox bottomControls = new HBox(backButton);
        if (User.getLoggedInUser().isAdmin()){
            // if user is admin, add and remove hall buttons are available
            bottomControls.getChildren().addAll(addHAllButton,removeHallButton);
        }
        bottomControls.getChildren().addAll(hallComboBox,okButton);
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.setSpacing(10);


        // creating a play/pause button to control the trailer of the film
        Button playPauseButton = new Button("  \u25B6  ");
        playPauseButton.setOnAction(e->{
            if (playPauseButton.getText().equals("  \u25B6  ")){
                if (trailer.getMediaPlayer().getCurrentTime().equals(trailer.getMediaPlayer().getTotalDuration()))
                    trailer.getMediaPlayer().seek(Duration.ZERO);
                trailer.getMediaPlayer().play();
                playPauseButton.setText("  ||  ");
            } else {
                trailer.getMediaPlayer().pause();
                playPauseButton.setText("  \u25B6  ");
            }
        });

        // creating a button to go 5 second back in the trailer
        Button fiveSecondsBackButton = new Button("<<");
        fiveSecondsBackButton.setOnAction(e->{
            if (trailer.getMediaPlayer().getCurrentTime().greaterThan(Duration.seconds(5)))
                trailer.getMediaPlayer().seek(trailer.getMediaPlayer().getCurrentTime().subtract(Duration.seconds(5)));
            else // if there are less than 5 second to the beginning
                trailer.getMediaPlayer().seek(Duration.ZERO);
        });

        // creating a button to go 5 second forward in the trailer
        Button skipFiveSecondsButton = new Button(">>");
        skipFiveSecondsButton.setOnAction(e->{
            if (trailer.getMediaPlayer().getCurrentTime().add(Duration.seconds(5)).lessThan(trailer.getMediaPlayer().getTotalDuration()))
                trailer.getMediaPlayer().seek(trailer.getMediaPlayer().getCurrentTime().add(Duration.seconds(5)));
            else {
                // if there are less than 5 seconds to the end
                trailer.getMediaPlayer().seek(trailer.getMediaPlayer().getTotalDuration());
                trailer.getMediaPlayer().pause();
                playPauseButton.setText("  \u25B6  ");
            }
        });

        // creating a button to rewind
        Button rewindButton = new Button("|<<");
        rewindButton.setOnAction(e->{
            trailer.getMediaPlayer().seek(Duration.ZERO);
        });

        // creating a slider to control the volume of the trailer
        Slider slider = new Slider();
        slider.setValue(trailerMP.getVolume()*50);
        slider.setOrientation(Orientation.VERTICAL);
        slider.valueProperty().addListener(e->{
            if (slider.isValueChanging() || slider.isPressed())
                trailerMP.setVolume(slider.getValue()/100);
        });

        // placing trailer related buttons
        VBox trailerButtons = new VBox(playPauseButton,fiveSecondsBackButton,skipFiveSecondsButton,rewindButton,slider);
        trailerButtons.setSpacing(10);
        trailerButtons.setAlignment(Pos.CENTER);


        // creating a slider to control the trailer playback
        Label trailerTime = new Label(getDurationInformation(trailerMP.getCurrentTime(),trailerMP.getTotalDuration()));
        Slider timeSlider = new Slider();

        timeSlider.valueProperty().addListener(e->{
            // to control the trailer playback
            if (timeSlider.isValueChanging() || timeSlider.isPressed()) {
                trailerMP.seek(trailerMP.getTotalDuration().multiply(timeSlider.getValue() / 100D));
                if (playPauseButton.getText().equals("  \u25B6  ")){
                    trailer.getMediaPlayer().pause();
                }
            }
        });

        trailerMP.currentTimeProperty().addListener(e->{
            // to update time and slider
            timeSlider.setValue(trailerMP.getCurrentTime().toMillis()/trailerMP.getTotalDuration().toMillis()*100D);
            trailerTime.setText(getDurationInformation(trailerMP.getCurrentTime(),trailerMP.getTotalDuration()));
        });

        trailerMP.setOnEndOfMedia(()->{
            // to update play/pause button and stop the player
            trailer.getMediaPlayer().pause();
            playPauseButton.setText("  \u25B6  ");
        });

        timeSlider.setPrefWidth(700);
        // placing time and slider next to each other
        HBox trailerTimeControl = new HBox(timeSlider,trailerTime);

        // adding all elements to the pane
        add(label,0,0);
        add(trailer,0,1);
        add(trailerTimeControl,0,2);
        add(bottomControls,0,3);
        add(trailerButtons,1,0,1,3);

        // adjusting trailer display
        trailer.setPreserveRatio(true);
        trailer.setFitHeight(450);
        trailer.setFitWidth(800);

        // adjusting positioning
        setHalignment(label, HPos.CENTER);
        setAlignment(Pos.TOP_CENTER);


    }
    // returns the time information of the trailer in minutes:seconds format
    private String getDurationInformation(Duration current, Duration total){

        int totalDurationAsSec = (int) Math.floor(total.toSeconds());
        int currentDurationAsSec = (int) Math.floor(current.toSeconds());
        int totalDurationMin;
        int totalDurationSec;

        if (totalDurationAsSec>=60){
            totalDurationMin = (int) Math.floor(totalDurationAsSec/60D);
        } else {
            totalDurationMin = 0;
        }

        if (totalDurationMin!=0){
            totalDurationSec = totalDurationAsSec - (totalDurationMin*60);
        } else{
            totalDurationSec = totalDurationAsSec;
        }

        int currentDurationMin;
        int currentDurationSec;

        if (currentDurationAsSec>=60){
            currentDurationMin = (int) Math.floor(currentDurationAsSec/60D);
        } else {
            currentDurationMin = 0;
        }

        if (currentDurationMin!=0){
            currentDurationSec = currentDurationAsSec - (currentDurationMin*60);
        } else{
            currentDurationSec = currentDurationAsSec;
        }
        return currentDurationMin+":"+currentDurationSec+" / "+totalDurationMin+":"+totalDurationSec;
    }
}
