import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Window;

public class HallWindow extends VBox {

    public HallWindow(Film film, Hall hall){
        // setting spacing, padding, positioning
        setSpacing(10);
        setPadding(new Insets(25,50,25,50));
        setAlignment(Pos.TOP_CENTER);

        // to display film and hall information
        Label label = new Label(film.getName()+" ("+film.getDuration()+" minutes) Hall: "+hall.getHallName());
        label.setAlignment(Pos.CENTER);

        // labels for messages and information about seats
        Label message = new Label();
        message.setAlignment(Pos.CENTER);
        Label seatStatus = new Label();
        seatStatus.setAlignment(Pos.CENTER);

        // a combo box for admins to make reservations for other users
        ComboBox<User> usersComboBox = new ComboBox<>(FXCollections.observableArrayList(User.getUsers().values()));
        usersComboBox.getSelectionModel().select(User.getLoggedInUser());

        // creating a pane for seats
        GridPane seats = new GridPane();
        seats.setAlignment(Pos.TOP_CENTER);
        for (Seat seat : hall.getSeats().values()){

            // images for seats
            ImageView emptySeat = new ImageView("file:assets/icons/empty_seat.png");
            emptySeat.setFitHeight(50);
            emptySeat.setPreserveRatio(true);
            ImageView reservedSeat = new ImageView("file:assets/icons/reserved_seat.png");
            reservedSeat.setFitWidth(50);
            reservedSeat.setPreserveRatio(true);

            int column = seat.getColumnIndex();
            int row = seat.getRowIndex();
            // creating a button for the seat
            Button seatButton = new Button();
            seatButton.setBackground(null);
            if (seat.getOwnerName() == null) // if the seat is empty
                seatButton.setGraphic(emptySeat);
            else // if the seat is reserved
                seatButton.setGraphic(reservedSeat);
            // if the seat is reserved by someone else and the logged-in user is not an admin, disables that seat's button
            if (seat.getOwnerName()!=null){
                if (!seat.getOwnerName().equals(User.getLoggedInUser().getName()) && !User.getLoggedInUser().isAdmin())
                    seatButton.setDisable(true);
            }
            seatButton.setOnAction(e->{

                User seatWillBoughtFor;
                if (User.getLoggedInUser().isAdmin()) {
                    // reserving for another user
                    seatWillBoughtFor = usersComboBox.getSelectionModel().getSelectedItem();
                } else {
                    // reserving for logged in user
                    seatWillBoughtFor = User.getLoggedInUser();
                }

                if (seat.getOwnerName() == null) {
                    // to reserve a seat
                    seat.setOwner(seatWillBoughtFor);
                    int price;
                    if (User.getLoggedInUser().isClubMember()) // discount for club members
                        price = (hall.getPricePerSeat() * (100-Data.getDiscountPercentage())) / 100;
                    else
                        price = hall.getPricePerSeat();
                    seat.setPrice(price);
                    message.setText("Seat at "+(row+1)+"-"+(column+1)+" is bought for "+
                            User.getLoggedInUser().getName()+" for "+ price+" TL successfully!");
                    seatButton.setGraphic(reservedSeat);

                } else {
                    // to remove the reservation of a seat
                    seat.setOwner(null);
                    message.setText("Seat at "+(row+1)+"-"+(column+1)+" is refunded successfully!");
                    seatButton.setGraphic(emptySeat);
                }

                getScene().getWindow().sizeToScene();
                getScene().getWindow().centerOnScreen();

            });

            // to display messages about seats when the mouse is on the seats
            seatButton.setOnMouseEntered(e->{
                if (seat.getOwnerName() == null)
                    seatStatus.setText("Not bought yet!");
                else
                    seatStatus.setText("Bought by "+seat.getOwnerName()+" for "+seat.getPrice()+" TL!");
            });
            seatButton.setOnMouseExited(e->{
                seatStatus.setText("");
            });

            // adding seat to the grid pane
            seats.add(seatButton,column,row);

        }

        // back button to go previous page
        Button backButton = new Button("\u25C0 Back");
        backButton.setOnAction(e->{
            Window window = getScene().getWindow();
            getScene().setRoot(new FilmWindow(film));
            window.sizeToScene();
            window.centerOnScreen();
        });
        backButton.setAlignment(Pos.BASELINE_LEFT);

        // adding elements to the window
        getChildren().addAll(label,seats);
        // if the user is admin, adds seat information text and a box to choose other users
        if (User.getLoggedInUser().isAdmin()){
            getChildren().add(usersComboBox);
            getChildren().add(seatStatus);
        }
        getChildren().addAll(message,backButton);


    }
}
