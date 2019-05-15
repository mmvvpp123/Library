package project.LogIn;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.Library.AdminWindow;
import project.Library.UserWindow;

import java.io.File;
import java.util.Optional;

public class LogInScreen extends Application {

    public static final ToggleGroup privilege = new ToggleGroup();
    public static Stage currentStage;
    public static Scene scene;
    public static PasswordField password_Field;
    private ImageView userImg, passwordImg, logInButtonImg, signUpButtonImg;

    public LogInScreen() {

    }

    public static void main(String[] args) {
        launch(args);
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

        Button back = new Button("Back");
        back.setCancelButton(true);
        back.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to go back?");
            Optional<ButtonType> option = alert.showAndWait();
            try {
                if (option.get() == ButtonType.OK) {
                    currentStage.close();
                    LogInScreen logInScreen = new LogInScreen();
                    logInScreen.start(logInScreen.currentStage);
                }
            } catch (Exception ex) {
                System.out.println("Back failed!");
            }
        });
        Button signUp = new Button("Sign Up");
        signUp.setDefaultButton(true);
        signUp.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (!passwordConfirm_Field.getText().equals(password_SignUpField.getText())) {
                alert.setContentText("Password fields don't match");
                alert.show();
            } else if (password_SignUpField.getText().length() < 8) {
                alert.setContentText("Password too short");
                alert.show();
            } else if (passwordValidNumbers(password_SignUpField.getText())) {
                alert.setContentText("Your password must include numbers");
                alert.show();
            } else if (passwordValidLetters(password_SignUpField.getText())) {
                alert.setContentText("Your password must include letters");
                alert.show();
            } else if ((!email_Field.getText().contains(".")) || (!email_Field.getText().contains("@"))) {
                alert.setContentText("Please enter a valid e-mail.");
                alert.show();
            } else {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Account created");
                User user1 = new User(email_Field.getText(), new String(User.encrypt(password_SignUpField.getText().getBytes())), name_Field.getText());
                User.save(user1);
                currentStage.setScene(scene);
                alert.show();
            }
            password_SignUpField.setText("");
            passwordConfirm_Field.setText("");
        });
        HBox buttons = new HBox(10, signUp, back);
        VBox box = new VBox(10, name_Label, name_Field, email_Label, email_Field, password_Label, password_SignUpField, passwordSecurity_Label, passwordConfirm_Label, passwordConfirm_Field, buttons);
        box.setPadding(new Insets(20));
        Scene newScene = new Scene(box, 640, 480);

        currentStage.setScene(newScene);
    }

    public static boolean passwordValidNumbers(String password) {
        char[] charList = password.toCharArray();
        boolean check = true;

        for (int i = 0; i < charList.length; i++) {
            if ((charList[i] <= 57) && (charList[i] >= 48)) {
                return false;
            }
        }
        return check;
    }

    public static boolean passwordValidLetters(String password) {
        char[] charList = password.toCharArray();
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

    @Override
    public void start(Stage primaryStage) {
        currentStage = primaryStage;
        currentStage.setTitle("Sherzod's Local Library Log In");

        Text welcome = new Text("Sherzod's Local Library");
        welcome.setFont(Font.font("Rockwell", 36));

        TextField email_Field = new TextField();
        email_Field.setPromptText("Email");
        email_Field.setMaxWidth(Control.USE_COMPUTED_SIZE);
        Image male = new Image(new File("vectors/email.png").toURI().toString());
        userImg = new ImageView(male);
        userImg.setPreserveRatio(true);
        userImg.setFitWidth(40);
        HBox box = new HBox(5, userImg, email_Field);
        box.setAlignment(Pos.CENTER);


        password_Field = new PasswordField();
        password_Field.setPromptText("Password");
        password_Field.setMaxWidth(Control.USE_COMPUTED_SIZE);
        Image lock = new Image(new File("vectors/lock.png").toURI().toString());
        passwordImg = new ImageView(lock);
        passwordImg.setPreserveRatio(true);
        passwordImg.setFitWidth(40);
        HBox passBox = new HBox(5, passwordImg, password_Field);
        passBox.setAlignment(Pos.CENTER);


        RadioButton guest = new RadioButton("Guest");
        guest.setToggleGroup(privilege);
        guest.setOnAction(e -> {
            password_Field.setDisable(true);
            email_Field.setPromptText("Name");
        });

        RadioButton user = new RadioButton("User");
        user.setToggleGroup(privilege);
        user.setSelected(true);
        user.setOnAction(e -> {
            email_Field.setPromptText("Email");
            password_Field.setDisable(false);
        });

        RadioButton admin = new RadioButton("Admin");
        admin.setToggleGroup(privilege);
        admin.setOnAction(e -> {
            email_Field.setPromptText("Email");
            password_Field.setDisable(false);
        });
        VBox mainContainer = new VBox(50);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPrefSize(600, 400);
        mainContainer.setMaxSize(600, 400);

        VBox secondContainer = new VBox(20);
        secondContainer.setAlignment(Pos.CENTER);
        secondContainer.setMaxSize(300, 100);
        secondContainer.getChildren().addAll(box, passBox);

        mainContainer.getChildren().addAll(welcome, secondContainer);


        HBox horiBox = new HBox(20, user, guest, admin);
        horiBox.setAlignment(Pos.CENTER);

        secondContainer.getChildren().add(horiBox);

        Button logIn = new Button("Log In");
        logIn.setDefaultButton(true);
        logInButtonImg = new ImageView(new File("vectors/login.png").toURI().toString());
        logInButtonImg.setPreserveRatio(true);
        logInButtonImg.setFitWidth(25);
        logIn.setGraphic(logInButtonImg);

        logIn.setOnAction((event) -> {
            File loadFile = new File("users/" + email_Field.getText() + ".bin");
            User temp = User.load(loadFile);
            if (!guest.isSelected()) {
                try {
                    if (!new String(User.decrypt(temp.getPassword().getBytes())).equals(password_Field.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Wrong Password");
                        alert.show();
                        password_Field.setText("");
                        temp = null;
                    } else {
                        UserWindow userWindow = new UserWindow(temp);
                        userWindow.start(primaryStage);
                    }
                } catch (Exception e) {
                    System.out.println("Cannot find user");
                }
            } else if (guest.isSelected()) {
                if (email_Field.getText().length() < 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please enter a name");
                    alert.show();
                } else {
                    UserWindow userWindow = new UserWindow(new User(" ", " ", email_Field.getText()), true);
                    userWindow.start(primaryStage);
                }
            }
            try {
                if (admin.isSelected() && temp.getEmail().equals("admin") &&
                        new String(User.decrypt(temp.getPassword().getBytes())).equals(password_Field.getText())) {
                    AdminWindow adminWindow = new AdminWindow();
                    adminWindow.start(primaryStage);
                } else if (admin.isSelected() && !email_Field.getText().equals("admin")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You do not have administrative privileges");
                    alert.show();
                }
            } catch (NullPointerException e) {
                System.out.println("Wrong password.");
            }
        });

        Button signUp = new Button("Sign Up");
        signUpButtonImg = new ImageView(new File("vectors/adduser.png").toURI().toString());
        signUpButtonImg.setPreserveRatio(true);
        signUpButtonImg.setFitWidth(25);
        signUp.setGraphic(signUpButtonImg);


        HBox buttons = new HBox(20, logIn, signUp);
        buttons.setAlignment(Pos.CENTER);
        secondContainer.getChildren().add(buttons);
        signUp.setOnAction(e -> password_Field.setText(""));
        signUp.setOnAction(e -> signUp(email_Field.getText()));

        scene = new Scene(mainContainer, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
