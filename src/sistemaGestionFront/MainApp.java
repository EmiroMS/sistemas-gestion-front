package sistemaGestionFront;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("views/login.fxml")
        );

        Scene scene = new Scene(loader.load(), 900, 600);

        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setResizable(true);


        stage.setTitle("LicoSoft HM - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}