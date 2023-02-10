import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;
import javafx.util.Duration;
import java.io.File;


public class LogInWindow extends GridPane {

    private static int failedLogInCounter = 0;
    private static boolean blockLogIn = false;
    private static long blockStartedAt;


    public LogInWindow(){
        // setting positioning, gaps, padding
        setAlignment(Pos.TOP_CENTER);
        setVgap(10);
        setPadding(new Insets(40,75,45,75));

        // to display a welcome message
        Text message = new Text("Welcome to the Cinema Reservation System!\nPlease enter your " +
                "credentials below and click LOGIN.\nYou can create a new account by clicking SIGN UP button.");
        message.setTextAlignment(TextAlignment.CENTER);
        setHalignment(message, HPos.CENTER);
        add(message,0,0);

        // creating a row for username
        TextField usernameTextField = new TextField();
        HBox usernameBox = new HBox(new Label("Username:  "),usernameTextField);
        usernameBox.setAlignment(Pos.CENTER);

        // creating a row for password
        PasswordField passwordTextField = new PasswordField();
        HBox passwordBox = new HBox(new Label("Password:  "),passwordTextField);
        passwordBox.setAlignment(Pos.CENTER);

        // positioning buttons
        HBox buttonBox = new HBox();
        Button logInButton = new Button("LOG IN");
        Button signUpButton = new Button("SIGN UP");
        buttonBox.getChildren().addAll(signUpButton,logInButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(100);

        // message for failed login attempts
        Label message2 = new Label();
        setHalignment(message2, HPos.CENTER);
        // initializing how many second the block will be
        int blockTime = Data.getBlockTime();
        // initializing the media player of the error sound
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));

        // creating login button
        logInButton.setOnAction(e -> {
            Window window = getScene().getWindow();
            // reading text fields
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            passwordTextField.clear();
            // checking if the block time ended
            if (blockLogIn) {
                if (System.currentTimeMillis() - blockStartedAt >= blockTime * 1000L) {
                    blockLogIn = false;
                }
            }
            if (blockLogIn){
                // to display a message and play error sound when blocked
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
                message2.setText("ERROR: Please wait until end of the "+blockTime+" seconds to make a new operation!");

            } else if (!User.getUsers().containsKey(username)){
                // if user enters an invalid username
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
                message2.setText("Error: There is no such a credential!");
                failedLogInCounter++;

            } else if (!User.hashPassword(password).equals(User.getUsers().get(username).getPassword())){
                // if user enters an invalid password
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
                message2.setText("Error: There is no such a credential!");
                failedLogInCounter++;

            } else {
                // if there is no errors
                // resets failed log in counter
                // sets logged-in user and goes to welcome window
                failedLogInCounter=0;
                User.setLoggedInUser(User.getUsers().get(username));
                getScene().setRoot(new WelcomeWindow(User.getLoggedInUser()));

            }
            if (failedLogInCounter>=Data.getMaxErrorWithoutGettingBlocked()) {
                // switches blockLogIn to true if failed log in counter reaches max error count
                failedLogInCounter = 0;
                blockLogIn = true;
                // sets error message and time when block is started
                message2.setText("ERROR: Please wait " + blockTime + " seconds to make a new operation!");
                blockStartedAt = System.currentTimeMillis();
            }

            window.sizeToScene();
            window.centerOnScreen();

        });

        // creating a sign-up button to go sign up window
        signUpButton.setOnAction(e -> {
            Window window = getScene().getWindow();
            getScene().setRoot(new SignUpWindow());
            window.sizeToScene();
            window.centerOnScreen();
        });

        // adding all elements to the window
        add(usernameBox,0,1);
        add(passwordBox,0,2);
        add(buttonBox,0,3);
        add(message2,0,4);

    }

}
