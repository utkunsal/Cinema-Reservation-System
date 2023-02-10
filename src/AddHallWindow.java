import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Window;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;

public class AddHallWindow extends GridPane {

    public AddHallWindow(Film film){
        // setting gaps and padding
        setPadding(new Insets(20,50,20,50));
        setVgap(10);
        setHgap(10);

        // adding details of the window to the top of the pane
        Label msg = new Label("Please enter attributes of the new hall for "+film.getName()+".");
        add(msg,0,0,2,1);

        // creating a back button to go previous page
        Button backButton = new javafx.scene.control.Button("\u25C0 Back");
        backButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new FilmWindow(film));
            window.sizeToScene();
            window.centerOnScreen();
        });

        // creating combo boxes which contain numbers from 3 to 10
        // to choose row and column count
        ArrayList<String> threeToTen = new ArrayList<>();
        threeToTen.add("3");
        threeToTen.add("4");
        threeToTen.add("5");
        threeToTen.add("6");
        threeToTen.add("7");
        threeToTen.add("8");
        threeToTen.add("9");
        threeToTen.add("10");
        ComboBox<String> rowComboBox = new ComboBox<>(FXCollections.observableArrayList(threeToTen));
        rowComboBox.getSelectionModel().selectFirst(); // setting default selection
        ComboBox<String> columnComboBox = new ComboBox<>(FXCollections.observableArrayList(threeToTen));
        columnComboBox.getSelectionModel().selectFirst(); // setting default selection

        // initializing media player of the error sound
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));

        // initializing text fields
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField dateField = new TextField();
        dateField.setPromptText("DD/MM/YY");

        // to create an ok button to confirm
        Label bottomMsg = new Label();
        Button okButton = new javafx.scene.control.Button("OK");
        okButton.setOnAction(e->{
            // reading text into variables from the fields
            String name = nameField.getText();
            String date = dateField.getText();
            String price = priceField.getText();
            // to check for errors
            if (name.isEmpty()){
                bottomMsg.setText("ERROR: Hall name could not be empty!");
                mediaPlayer.seek(Duration.ZERO); // to play error sound
                mediaPlayer.play();
            } else if (date.isEmpty()) {
                bottomMsg.setText("ERROR: Date could not be empty!");
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            } else if (price.isEmpty()) {
                bottomMsg.setText("ERROR: Price could not be empty!");
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            } else if (film.getHalls().containsKey(name)){
                bottomMsg.setText("ERROR: This hall name already exists!");
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            } else {
                // to check if the date is valid
                boolean dateValid = false;
                try {
                    String[] splittedDate = date.split("/");
                    int day = Integer.parseInt(splittedDate[0]);
                    int month = Integer.parseInt(splittedDate[1]);
                    int year = Integer.parseInt(splittedDate[2]);

                    // if there is no errors and the date is valid
                    if (day < 32 && day > 0 && month < 13 && month > 0 && year > 0)
                        dateValid = true;

                } catch (Exception ignored){}
                if(!dateValid){
                    // if invalid date
                    bottomMsg.setText("ERROR: Date format is not correct!");
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
                else {
                    // to check price
                    try {
                        // if price is not an integer catch block handles
                        int priceInt = Integer.parseInt(price);
                        if (priceInt <= 0) {
                            // if the price is negative
                            bottomMsg.setText("ERROR: Price has to be a positive integer!");
                            mediaPlayer.seek(Duration.ZERO);
                            mediaPlayer.play();

                        } else {

                            // to add the hall if there is no errors
                            film.addHall(new String[]{"hall", film.getName(), name, price,
                                    rowComboBox.getSelectionModel().getSelectedItem(),
                                    columnComboBox.getSelectionModel().getSelectedItem(),date});

                            // to add seats to the hall
                            for (int row = 0; row < Integer.parseInt(rowComboBox.getSelectionModel().getSelectedItem()); row++) {
                                for (int col = 0; col < Integer.parseInt(columnComboBox.getSelectionModel().getSelectedItem()); col++) {
                                    String rowIndex = String.valueOf(row);
                                    String columnIndex = String.valueOf(col);
                                    film.getHalls().get(name).addSeat(new String[]{"seat", film.getName(), name, rowIndex, columnIndex, "null", "0"});
                                }
                            }

                            bottomMsg.setText("SUCCESS: Hall successfully created!");
                            // to clear selections and fields
                            rowComboBox.getSelectionModel().selectFirst();
                            columnComboBox.getSelectionModel().selectFirst();
                            nameField.clear();
                            priceField.clear();

                        }
                    } catch (NumberFormatException e2) {
                        // if price is not an integer
                        bottomMsg.setText("ERROR: Price has to be a positive integer!");
                        mediaPlayer.seek(Duration.ZERO);
                        mediaPlayer.play();
                    }
                }
            }
            // to adjust the window size
            getScene().getWindow().sizeToScene();
            getScene().getWindow().centerOnScreen();
        });

        // adding elements to the pane
        add(new Label("Row:"),0,1);
        add(rowComboBox,1,1);
        add(new Label("Column:"),0,2);
        add(columnComboBox,1,2);
        add(new Label("Name:"),0,3);
        add(nameField,1,3);
        add(new Label("Date:"),0,4);
        add(dateField,1,4);
        add(new Label("Price:"),0,5);
        add(priceField,1,5);
        add(backButton,0,6);
        add(okButton,1,6);
        add(bottomMsg,0,7,2,1);
        // setting positions
        setHalignment(bottomMsg,HPos.CENTER);
        setHalignment(msg, HPos.CENTER);
        setHalignment(columnComboBox,HPos.CENTER);
        setHalignment(rowComboBox,HPos.CENTER);
        setHalignment(okButton,HPos.RIGHT);
        setAlignment(Pos.TOP_CENTER);
    }

}
