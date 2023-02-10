import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Data.initialize("assets/data/backup.dat", "assets/data/properties.dat");
        Scene scene = new Scene(new LogInWindow());
        primaryStage.setScene(scene);
        primaryStage.setTitle(Data.getTitle());
        primaryStage.getIcons().add(new Image("file:assets/icons/logo.png"));
        primaryStage.setOnCloseRequest(e->{
            Data.backup("assets/data/backup.dat");
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}
