import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class jfxside extends Application {

    public static Stage currentStage;
    public static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        currentStage = primaryStage;

        Label email_Label = new Label("Email");
        TextField email_Field = new TextField();

        Label password_Label = new Label("Password");
        PasswordField password_Field = new PasswordField();

        Button logIn = new Button("Log In");
        logIn.setOnAction((event) -> {
            File loadFile = new File(email_Field.getText() + ".bin");
            User temp = User.load(loadFile);
            if(!temp.getPassword().equals(password_Field.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong Password");
                alert.show();
                password_Field.setText("");
                temp = null;
            }
            else {
                System.out.println("Log in successful.");
            }
        });

        Button signUp = new Button("Sign Up");
        signUp.setOnAction(e -> signUp());

        VBox box = new VBox(10, email_Label, email_Field, password_Label, password_Field, logIn, signUp);
        scene = new Scene(box,300, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void signUp() {
        Label name_Label = new Label("Name");
        TextField name_Field = new TextField();

        Label email_Label = new Label("Email");
        TextField email_Field = new TextField();

        Label password_Label = new Label("Password");
        PasswordField password_Field = new PasswordField();

        Label passwordConfirm_Label = new Label("Confirm Password");
        PasswordField passwordConfirm_Field = new PasswordField();

        Button signUp = new Button("Sign Up");
        signUp.setOnAction((event) -> {
            User user = new User(email_Field.getText(), password_Field.getText(), name_Field.getText());
            User.save(user);
            currentStage.setScene(scene);
        });
        VBox box = new VBox(10, name_Label, name_Field, email_Label, email_Field, password_Label, password_Field, passwordConfirm_Label, passwordConfirm_Field, signUp);
        Scene newScene = new Scene(box, 300, 500);

        currentStage.setScene(newScene);
    }
}
