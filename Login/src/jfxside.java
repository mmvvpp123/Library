import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
            if(!new String(User.decrypt(temp.getPassword().getBytes())).equals(password_Field.getText())){
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
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
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
        Label passwordSecurity_Label = new Label("Password must have: 8 characters, a number, and a letter");
        passwordSecurity_Label.setFont(new Font("Arial", 10));

        Label passwordConfirm_Label = new Label("Confirm Password");
        PasswordField passwordConfirm_Field = new PasswordField();

        Button signUp = new Button("Sign Up");
        signUp.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if(password_Field.getText().length() < 8) {
                alert.setContentText("Password too short");
                alert.show();
                password_Field.setText("");
                passwordConfirm_Field.setText("");
            }
            else if (passwordValidNumbers(password_Field.getText())) {
                alert.setContentText("Your password must include numbers");
                alert.show();
                password_Field.setText("");
                passwordConfirm_Field.setText("");
            }
            else if (passwordValidLetters(password_Field.getText())) {
                alert.setContentText("Your password must include letters");
                alert.show();
                passwordConfirm_Field.setText("");
                password_Field.setText("");
            }
            else {
                User user1 = new User(email_Field.getText(), new String(User.encrypt(password_Field.getText().getBytes())), name_Field.getText());
                User.save(user1);
            }
        });
        VBox box = new VBox(10, name_Label, name_Field, email_Label, email_Field, password_Label, password_Field, passwordSecurity_Label ,passwordConfirm_Label, passwordConfirm_Field, signUp);
        box.setPadding(new Insets(20));
        Scene newScene = new Scene(box, 300, 500);

        currentStage.setScene(newScene);
    }

    public static boolean passwordValidNumbers(String password) {
        char [] charList = password.toCharArray();
        boolean check = true;

        for(int i = 0; i < charList.length; i++) {
            if((charList[i] <= 57) && (charList[i] >= 48)) {
                return false;
            }
        }
        return check;
    }

    public static boolean passwordValidLetters(String password) {
        char [] charList = password.toCharArray();
        boolean check = true;
        for (int i = 0; i < charList.length; i++) {
            if ((charList[i] <= 90) && (charList[i] >= 65)) {
                return false;
            } else if ((charList[i] <= 122) && (charList[i] >= 97)) {
                return false;
            }
        }
        return check;
    }
}
