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


public class SignUpWindow extends GridPane {

    public SignUpWindow(){
        // setting positioning, gaps, padding
        setAlignment(Pos.TOP_CENTER);
        setVgap(10);
        setPadding(new Insets(40,90,45,90));

        // to display a message about the window
        Text message = new Text("Welcome to the HUCS Cinema Reservation System!\nFill the form " +
                "below to create a new account.\nYou can go to Log In page by clicking LOG IN button.");
        message.setTextAlignment(TextAlignment.CENTER);
        setHalignment(message,HPos.CENTER);
        add(message,0,0);

        // creating a row for username
        TextField usernameTextField = new TextField();
        HBox usernameBox = new HBox(new Label("Username:  "),usernameTextField);
        usernameBox.setAlignment(Pos.CENTER);

        // creating a row for password
        PasswordField passwordTextField = new PasswordField();
        HBox passwordBox = new HBox(new Label("Password:  "),passwordTextField);
        passwordBox.setAlignment(Pos.CENTER);

        // creating a row for password confirmation
        PasswordField passwordAgainTextField = new PasswordField();
        HBox passwordBoxAgain = new HBox(new Label("Password:  "),passwordAgainTextField);
        passwordBoxAgain.setAlignment(Pos.CENTER);

        // placing buttons
        HBox buttonBox = new HBox();
        Button logInButton = new Button("LOG IN");
        Button signUpButton = new Button("SIGN UP");
        buttonBox.getChildren().addAll(logInButton,signUpButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(100);

        // to display any errors
        Label message2 = new Label();
        setHalignment(message2, HPos.CENTER);
        // initializing media player for the error sound
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));

        // creating a button to return log in window
        logInButton.setOnAction(e -> {
            Window window = getScene().getWindow();
            getScene().setRoot(new LogInWindow());
            window.sizeToScene();
            window.centerOnScreen();

        });

        // creating a button to sign up
        signUpButton.setOnAction(e -> {
            // getting information into variables from text fields
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String passwordAgain = passwordAgainTextField.getText();
            usernameTextField.clear();
            passwordTextField.clear();
            passwordAgainTextField.clear();

            // checking for errors
            if (User.getUsers().containsKey(username)){
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
                message2.setText("ERROR: This username already exists!");
            } else if (username.isEmpty()){
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
                message2.setText("ERROR: Username cannot be empty!");
            } else if (password.isEmpty() && passwordAgain.isEmpty()){
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
                message2.setText("ERROR: Password cannot be empty!");
            } else if (!password.equals(passwordAgain)){
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
                message2.setText("ERROR: Passwords do not match!");
            } else {
                // to create a new user if there is no errors
                new User(username, User.hashPassword(password), "false", "false");
                message2.setText("SUCCESS: You have successfully registered with your new credentials");

            }

            // to adjust window size
            getScene().getWindow().sizeToScene();
            getScene().getWindow().centerOnScreen();

        });

        // to add elements to the window
        add(usernameBox,0,1);
        add(passwordBox,0,2);
        add(passwordBoxAgain,0,3);
        add(buttonBox,0,4);
        add(message2,0,5);


    }
}
