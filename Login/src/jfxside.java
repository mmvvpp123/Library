import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class jfxside extends Application {

    public static Stage currentStage;
    public static Scene scene;
    public static PasswordField password_Field;
    public static final ToggleGroup privilege = new ToggleGroup();

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        currentStage = primaryStage;

        Label email_Label = new Label("Email");
        TextField email_Field = new TextField();

        Label password_Label = new Label("Password");
        password_Field = new PasswordField();

        RadioButton guest = new RadioButton("Guest");
        guest.setToggleGroup(privilege);
        guest.setOnAction(e -> {
            password_Field.setDisable(true);
            email_Label.setText("Name");
        });

        RadioButton user = new RadioButton("User");
        user.setToggleGroup(privilege);
        user.setSelected(true);
        user.setOnAction(e -> {
            password_Field.setDisable(false);
            email_Label.setText("Email");
        });

        RadioButton admin = new RadioButton("Admin");
        admin.setToggleGroup(privilege);
        admin.setOnAction(e -> {

            password_Field.setDisable(false);
            email_Label.setText("Email");
        });

        HBox horiBox = new HBox(user, guest, admin);
        Button logIn = new Button("Log In");
        logIn.setOnAction((event) -> {
            if(!guest.isSelected()) {
                File loadFile = new File("/Users/sherzodnimatullo/Library-School-Project/users/" + email_Field.getText() + ".bin");
                User temp = User.load(loadFile);
                if (!new String(User.decrypt(temp.getPassword().getBytes())).equals(password_Field.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Wrong Password");
                    alert.show();
                    password_Field.setText("");
                    temp = null;
                }
            }
            System.out.println("Login Successful");
        });

        Button signUp = new Button("Sign Up");
        signUp.setOnAction(e -> password_Field.setText(""));
        signUp.setOnAction(e -> signUp(email_Field.getText()));

        VBox vertBox = new VBox(10, email_Label, email_Field, password_Label, password_Field, horiBox, logIn, signUp);
        vertBox.setAlignment(Pos.CENTER);
        vertBox.setPadding(new Insets(20));
        scene = new Scene(vertBox,300, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void signUp(String email) {
        Label name_Label = new Label("Name");
        TextField name_Field = new TextField();

        Label email_Label = new Label("Email");
        TextField email_Field = new TextField();
        email_Field.setText(email);

        Label password_Label = new Label("Password");
        PasswordField password_SignUpField = new PasswordField();
        Label passwordSecurity_Label = new Label("Password must have: 8 characters, a number, and a letter");
        passwordSecurity_Label.setFont(new Font("Arial", 10));

        Label passwordConfirm_Label = new Label("Confirm Password");
        PasswordField passwordConfirm_Field = new PasswordField();

        Button signUp = new Button("Sign Up");
        signUp.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if(!passwordConfirm_Field.getText().equals(password_SignUpField.getText())) {
                alert.setContentText("Password fields don't match");
                alert.show();
                password_SignUpField.setText("");
                passwordConfirm_Field.setText("");
            }
            else if(password_SignUpField.getText().length() < 8) {
                alert.setContentText("Password too short");
                alert.show();
                password_SignUpField.setText("");
                passwordConfirm_Field.setText("");
            }
            else if (passwordValidNumbers(password_SignUpField.getText())) {
                alert.setContentText("Your password must include numbers");
                alert.show();
                password_SignUpField.setText("");
                passwordConfirm_Field.setText("");
            }
            else if (passwordValidLetters(password_SignUpField.getText())) {
                alert.setContentText("Your password must include letters");
                alert.show();
                passwordConfirm_Field.setText("");
                password_SignUpField.setText("");
            }
            else {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Account created");
                User user1 = new User(email_Field.getText(), new String(User.encrypt(password_SignUpField.getText().getBytes())), name_Field.getText());
                User.save(user1);
                password_Field.setText("");
                currentStage.setScene(scene);
                alert.show();
            }
        });
        VBox box = new VBox(10, name_Label, name_Field, email_Label, email_Field, password_Label, password_SignUpField, passwordSecurity_Label ,passwordConfirm_Label, passwordConfirm_Field, signUp);
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