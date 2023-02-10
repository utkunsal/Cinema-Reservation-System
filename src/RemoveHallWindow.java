import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;


public class RemoveHallWindow extends VBox {

    public RemoveHallWindow(Film film){
        // setting spacing and padding
        setSpacing(10);
        setPadding(new Insets(20,50,20,50));

        // to display information about the window
        Label msg = new Label("Select the hall you desire to remove from "+film.getName()+" and then click OK.");

        // creating a combo box with halls
        ComboBox<Hall> hallComboBox = new ComboBox<>(FXCollections.observableArrayList(film.getHalls().values()));
        hallComboBox.getSelectionModel().selectFirst();

        // creating a back button to go previous page
        Button backButton = new javafx.scene.control.Button("\u25C0 BACK");
        backButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new FilmWindow(film));
            window.sizeToScene();
            window.centerOnScreen();
        });

        // creating an ok button to remove hall and return film window
        Button okButton = new Button("OK");
        okButton.setOnAction(e->{
            try {
                film.getHalls().remove(hallComboBox.getSelectionModel().getSelectedItem().getHallName());
            }catch (NullPointerException ignored){}
            Window window = getScene().getWindow();
            getScene().setRoot(new FilmWindow(film));
            window.sizeToScene();
            window.centerOnScreen();
        });

        // placing buttons
        HBox buttons = new HBox(backButton,okButton);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        // adding elements to window
        getChildren().addAll(msg,hallComboBox,buttons);
        setAlignment(Pos.TOP_CENTER);
    }
}
