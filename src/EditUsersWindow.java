import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.stage.Window;

public class EditUsersWindow extends VBox {

    public EditUsersWindow(){
        // setting spacing and padding
        setSpacing(15);
        setPadding(new Insets(15,75,20,75));

        // to create a table view with users and their admin/club member information
        TableView<User> tableView = new TableView<>();
        // to create columns for the table
        TableColumn<User,String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User,Boolean> clubMemberCol = new TableColumn<>("Club Member");
        clubMemberCol.setCellValueFactory(new PropertyValueFactory<>("clubMember"));
        TableColumn<User,Boolean> adminCol = new TableColumn<>("Admin");
        adminCol.setCellValueFactory(new PropertyValueFactory<>("admin"));
        // to add the columns to the table
        tableView.getColumns().add(usernameCol);
        tableView.getColumns().add(clubMemberCol);
        tableView.getColumns().add(adminCol);

        // to add users to the table
        tableView.getItems().addAll(User.getUsers().values());
        // to remove the logged-in user and admin from the table
        tableView.getItems().remove(User.getUsers().get("admin"));
        if (!User.getLoggedInUser().getName().equals("admin"))
            tableView.getItems().remove(User.getLoggedInUser());
        tableView.getSelectionModel().selectFirst();

        // creating a back button to go previous page
        Button backButton = new Button("\u25C0 Back");
        backButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new WelcomeWindow(User.getLoggedInUser()));
            window.sizeToScene();
            window.centerOnScreen();
        });

        // creating a button to change club member status of a user
        Button changeClubMemberStatus = new Button("Promote/Denote Club Member");
        changeClubMemberStatus.setOnAction(e->{
            try {
                tableView.getSelectionModel().getSelectedItem().changeClubMemberStatus();
                tableView.refresh();
            }catch (NullPointerException ignored){}
        });

        // creating a button to change admin status of a user
        Button changeAdminStatus = new Button("Promote/Denote Admin");
        changeAdminStatus.setOnAction(e->{
            try {
                tableView.getSelectionModel().getSelectedItem().changeAdminStatus();
                tableView.refresh();
            }catch (NullPointerException ignored){}
        });

        // placing buttons next to each other
        HBox buttonBox = new HBox(backButton,changeClubMemberStatus,changeAdminStatus);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        // to add all the elements to the window
        getChildren().addAll(tableView,buttonBox);
        setAlignment(Pos.TOP_CENTER);

    }
}
