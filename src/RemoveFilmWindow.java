import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;

public class RemoveFilmWindow extends VBox {

    public RemoveFilmWindow(){
        // setting spacing and padding
        setSpacing(15);
        setPadding(new Insets(20,75,20,75));

        // creating a combobox with films
        ComboBox<Film> comboBox = new ComboBox<>(FXCollections.observableArrayList(Film.getFilms().values()));
        comboBox.getSelectionModel().selectFirst();

        // creating an ok button to remove a film
        Button okButton = new Button("OK");
        okButton.setOnAction(e->{
            try {
                Film.getFilms().remove(comboBox.getSelectionModel().getSelectedItem().getName());
                comboBox.getItems().remove(comboBox.getSelectionModel().getSelectedItem());
                comboBox.getSelectionModel().selectFirst();
            }catch (NullPointerException e1){}
        });

        // creating a back button to go previous page
        Button backButton = new Button("\u25C0 BACK");
        backButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new WelcomeWindow(User.getLoggedInUser()));
            window.sizeToScene();
            window.centerOnScreen();
        });

        // placing buttons
        HBox buttons = new HBox(backButton,okButton);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);

        // adding elements to the window
        getChildren().addAll(new Text("Select the film that you desire to remove and then click OK."),comboBox,buttons);
        setAlignment(Pos.TOP_CENTER);
    }
}
