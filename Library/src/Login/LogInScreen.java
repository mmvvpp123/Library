package Login;

import Library.AdminWindow;
import Library.UserWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
public class LogInScreen extends Application {

    public static Stage currentStage;
    public static Scene scene;
    public static PasswordField password_Field;
    public static final ToggleGroup privilege = new ToggleGroup();
    private ImageView userImg, passwordImg, logInButtonImg, signUpButtonImg;


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        currentStage = primaryStage;
        currentStage.setTitle("Sherzod's Local Library Log In");

        Text welcome = new Text("Sherzod's Local Library");
        welcome.setFont(Font.font("Verdana", 36));

        TextField email_Field = new TextField();
        email_Field.setPromptText("Email");
        email_Field.setMaxWidth(Control.USE_COMPUTED_SIZE);
        Image male = new Image(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/email.png").toURI().toString());
        userImg = new ImageView(male);
        userImg.setPreserveRatio(true);
        userImg.setFitWidth(40);
        HBox box = new HBox(5, userImg, email_Field);
        box.setAlignment(Pos.CENTER);


        password_Field = new PasswordField();
        password_Field.setPromptText("Password");
        password_Field.setMaxWidth(Control.USE_COMPUTED_SIZE);
        Image lock = new Image(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/lock.png").toURI().toString());
        passwordImg = new ImageView(lock);
        passwordImg.setPreserveRatio(true);
        passwordImg.setFitWidth(40);
        HBox passBox = new HBox(5, passwordImg, password_Field);
        passBox.setAlignment(Pos.CENTER);



        RadioButton guest = new RadioButton("Guest");
        guest.setToggleGroup(privilege);
        guest.setOnAction(e -> {
            password_Field.setDisable(true);
        });

        RadioButton user = new RadioButton("User");
        user.setToggleGroup(privilege);
        user.setSelected(true);
        user.setOnAction(e -> {
            password_Field.setDisable(false);
        });

        RadioButton admin = new RadioButton("Admin");
        admin.setToggleGroup(privilege);
        admin.setOnAction(e -> {

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
        logInButtonImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/login.png").toURI().toString());
        logInButtonImg.setPreserveRatio(true);
        logInButtonImg.setFitWidth(25);
        logIn.setGraphic(logInButtonImg);

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
                else {
                    try {
                        UserWindow userWindow = new UserWindow(temp);
                        userWindow.start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(admin.isSelected() && email_Field.getText().equals("admin")) {
                try {
                    AdminWindow adminWindow = new AdminWindow();
                    adminWindow.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (admin.isSelected() && !email_Field.getText().equals("sherzodnimatullo@gmail.com")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You do not have administrative privileges");
                alert.show();
            }
        });

        Button signUp = new Button("Sign Up");
        signUpButtonImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/adduser.png").toURI().toString());
        signUpButtonImg.setPreserveRatio(true);
        signUpButtonImg.setFitWidth(25);
        signUp.setGraphic(signUpButtonImg);



        HBox buttons = new HBox(20, logIn, signUp);
        buttons.setAlignment(Pos.CENTER);
        secondContainer.getChildren().add(buttons);
        signUp.setOnAction(e -> password_Field.setText(""));
        signUp.setOnAction(e -> signUp(email_Field.getText()));

        scene = new Scene(mainContainer,640, 480);
        File f = new File("stylesheet.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        primaryStage.setScene(scene);
        primaryStage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 4);
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
        signUp.setDefaultButton(true);
        signUp.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if(!passwordConfirm_Field.getText().equals(password_SignUpField.getText())) {
                alert.setContentText("Password fields don't match");
                alert.show();
                password_SignUpField.setText("");
                passwordConfirm_Field.setText("");
            }
            else if(!email_Field.getText().contains("@") && !email_Field.getText().contains(".")) {
                alert.setContentText("Please enter a valid e-mail address");
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
