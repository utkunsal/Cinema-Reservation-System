import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;


public class WelcomeWindow extends VBox {

    public WelcomeWindow(User user){
        // setting padding and spacing
        setPadding(new Insets(40,75,40,75));
        setSpacing(15);

        // to display messages about the window and user
        String userType = "";
        String str = "Select a film and then click OK to continue.";
        if (user.isAdmin()){
            if (user.isClubMember())
                userType = " (Admin - Club Member)";
            else
                userType = " (Admin)";
            str = "You can either select film below or do edits.";
        } else if (user.isClubMember()){
            userType = " (Club Member)";
        }
        Text message = new Text("Welcome "+user.getName()+userType+"!\n"+str);
        message.setTextAlignment(TextAlignment.CENTER);

        // to create a combo box with films
        ComboBox<Film> comboBox = new ComboBox<>(FXCollections.observableArrayList(Film.getFilms().values()));
        comboBox.getSelectionModel().selectFirst();

        // creating an ok button to select a film and go film window
        Button okButton = new Button("OK");
        okButton.setOnAction(e->{
            Window window = getScene().getWindow();
            try {
                getScene().setRoot(new FilmWindow(comboBox.getSelectionModel().getSelectedItem()));
            }catch (NullPointerException e1){}
            window.sizeToScene();
            window.centerOnScreen();
        });

        // placing film box and button
        HBox chooseFilmBox = new HBox(comboBox,okButton);
        chooseFilmBox.setAlignment(Pos.CENTER);
        chooseFilmBox.setSpacing(10);

        // creating a button to log out and return log in window
        Button logOutButton = new Button("LOG OUT");
        logOutButton.setOnAction(e -> {
            Window window = getScene().getWindow();
            getScene().setRoot(new LogInWindow());
            window.sizeToScene();
            window.centerOnScreen();
        });

        // creating a button to go add film window
        Button addFilmButton = new Button("Add Film");
        addFilmButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new AddFilmWindow());
            window.sizeToScene();
            window.centerOnScreen();
        });

        // creating a button to go remove film window
        Button removeFilmButton = new Button("Remove Film");
        removeFilmButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new RemoveFilmWindow());
            window.sizeToScene();
            window.centerOnScreen();
        });

        // creating a button to go edit users window
        Button editUsersButton = new Button("Edit Users");
        editUsersButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new EditUsersWindow());
            window.sizeToScene();
            window.centerOnScreen();
        });

        // placing buttons
        HBox adminButtonBox = new HBox(addFilmButton,removeFilmButton,editUsersButton);
        adminButtonBox.setAlignment(Pos.CENTER);
        adminButtonBox.setSpacing(10);

        HBox logOutButtonBox = new HBox(logOutButton);
        logOutButtonBox.setAlignment(Pos.CENTER_RIGHT);

        // adding elements to the window
        // if the user is admin, add/remove film and edit user buttons are available
        getChildren().addAll(message,chooseFilmBox);
        if (user.isAdmin()) {
            getChildren().add(adminButtonBox);
        }
        getChildren().add(logOutButtonBox);
        setAlignment(Pos.TOP_CENTER);

    }
}
