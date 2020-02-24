import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TabroomGUI extends Application {

    Button startButton;

    public static void main(String[] args) {
        launch(args); //sets up JavaFX


    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("TabroomReader");

        startButton = new Button("Start");
        startButton.setOnAction(e -> System.out.println("what the fuck"));

        StackPane layout = new StackPane();
        layout.getChildren().add(startButton);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);

        stage.show();
    }

}