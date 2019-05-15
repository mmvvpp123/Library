package project.Library;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.LogIn.LogInScreen;
import project.LogIn.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class AdminWindow extends Application implements Serializable {

    private static final long serialVersionUID = 990882L;

    private TableView table = generateColumns();
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private MyLibrary library = new MyLibrary(load());
    private ImageView addImg, removeImg, saveImg, logoutImg, usersImg;

    public AdminWindow() {
    }

    public static void save(ArrayList<Book> library) {
        try {
            File userFile = new File("listOfBooks.bin");

            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(userFile);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object

            out.writeObject(library);

            out.close();
            file.close();
        } catch (Exception e) {
            System.out.println("Directory missing");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        for (int i = 0; i < library.getList().size(); i++) {
            books.add(library.getList().get(i));
        }
        primaryStage.setTitle("Admin Page");

        table.setEditable(true);

        Label title_Label = new Label("Title");
        TextField title_Field = new TextField();

        Label author_Label = new Label(("Author"));
        TextField author_Field = new TextField();

        Label category_Label = new Label(("Category"));
        TextField category_Field = new TextField();

        Label isbn_Label = new Label(("ISBN"));
        TextField isbn_Field = new TextField();

        Spinner<Integer> quantitySpinner = new Spinner<>();
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));

        HBox isbnAndSpinner = new HBox(20, isbn_Field, quantitySpinner);

        Button users = new Button("View Users");
        usersImg = new ImageView(new File("vectors/users.png").toURI().toString());
        usersImg.setPreserveRatio(true);
        usersImg.setFitWidth(20);
        users.setGraphic(usersImg);
        users.setOnAction(e -> {
            ObservableList<String> userList = FXCollections.observableArrayList();
            File folder = new File("users/");
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (User.load(listOfFiles[i]) instanceof User) {
                    userList.add(User.load(listOfFiles[i]).getEmail());
                }
            }
            ListView<String> listViewUser = new ListView<>(userList);

            Button viewBooks = new Button("View Books");
            ObservableList<String> books = FXCollections.observableArrayList();
            viewBooks.setOnAction(event -> {
                books.clear();
                String email = "users/" + listViewUser.getSelectionModel().getSelectedItem() + ".bin";
                User temp = User.load(new File(email));
                for (int i = 0; i < temp.getList().size(); i++) {
                    books.add(temp.getList().get(i).getTitle());
                }
            });
            ListView<String> listViewBooks = new ListView<>(books);
            HBox lists = new HBox(listViewUser, listViewBooks);
            VBox container = new VBox(lists, viewBooks);
            Stage stage = new Stage();
            stage.setTitle("Manage Users");
            stage.setScene(new Scene(container, 500, 500));
            stage.show();
        });


        Button logout = new Button("Log Out");
        logout.setCancelButton(true);
        logoutImg = new ImageView(new File("vectors/logout.png").toURI().toString());
        logoutImg.setPreserveRatio(true);
        logoutImg.setFitWidth(20);
        logout.setGraphic(logoutImg);


        Button add = new Button("Add Book");
        add.setDefaultButton(true);
        addImg = new ImageView(new File("vectors/add.png").toURI().toString());
        addImg.setPreserveRatio(true);
        addImg.setFitWidth(20);
        add.setGraphic(addImg);


        Button save = new Button("Save");
        saveImg = new ImageView(new File("vectors/save.png").toURI().toString());
        saveImg.setPreserveRatio(true);
        saveImg.setFitWidth(20);
        save.setGraphic(saveImg);

        Button remove = new Button("Remove Book");
        removeImg = new ImageView(new File("vectors/remove.png").toURI().toString());
        removeImg.setPreserveRatio(true);
        removeImg.setFitWidth(20);
        remove.setGraphic(removeImg);

        save.setOnAction(e -> save(library.getList()));

        logout.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to log off?");
            Optional<ButtonType> option = alert.showAndWait();
            try {
                if (option.get() == ButtonType.OK) {
                    primaryStage.close();
                    LogInScreen logInScreen = new LogInScreen();
                    logInScreen.start(logInScreen.currentStage);
                }
            } catch (Exception ex) {
                System.out.println("Restart failed!");
            }
        });


        remove.setOnAction(e -> {
            Book k = (Book) table.getSelectionModel().getSelectedItem();

            if (k.getQuantity() == 1 || k.getQuantity() < quantitySpinner.getValue()) {
                books.remove(k);
                library.remove(k);
            } else
                for (int i = 0; i < books.size(); i++) {
                    if (books.get(i).getTitle().equals(k.getTitle())) {
                        books.get(i).decQuantity(quantitySpinner.getValue());
                        library.getList().get(i).decQuantity(quantitySpinner.getValue());
                        table.refresh();
                        return;
                    }
                }
        });

        add.setOnAction(e -> {
            Book temp = new Book(title_Field.getText(), author_Field.getText(), category_Field.getText(), isbn_Field.getText(), quantitySpinner.getValue());
            if (books.size() > 0) {
                for (int i = 0; i < books.size(); i++) {
                    if (library.getList().get(i).getTitle().equals(temp.getTitle())) {
                        library.getList().get(i).incQuantity(temp.getQuantity());
                        books.get(i).incQuantity(temp.getQuantity());
                        table.refresh();


                        title_Field.setText("");
                        author_Field.setText("");
                        category_Field.setText("");
                        isbn_Field.setText("");
                        quantitySpinner.getValueFactory().setValue(1);
                        return;
                    }
                }
            }
            books.add(temp);
            library.add(temp);
            table.refresh();


            title_Field.setText("");
            author_Field.setText("");
            category_Field.setText("");
            isbn_Field.setText("");
            quantitySpinner.getValueFactory().setValue(1);
        });


        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Edit");
        item1.setOnAction(e -> {
            Book temp = ((Book) table.getSelectionModel().getSelectedItem());
            TextInputDialog dialog = new TextInputDialog(Integer.toString(temp.getQuantity()));
            dialog.setTitle("Quantity Change");
            dialog.setHeaderText("Quantity");
            dialog.setContentText("Please enter the new quantity:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                temp.setQuantity(Integer.parseInt(name));
                save(library.getList());
                table.refresh();
            });
        });
        MenuItem item2 = new MenuItem("Remove");
        item2.setOnAction(event -> {
            Book temp = (Book) table.getSelectionModel().getSelectedItem();
            books.remove(temp);
            library.remove(temp);
        });

        contextMenu.getItems().addAll(item1, item2);

        table.setOnContextMenuRequested(e ->
                contextMenu.show(table, e.getScreenX(), e.getScreenY()));
        table.setItems(books);
        table.getSelectionModel().selectFirst();

        Region region = new Region();
        HBox hbox = new HBox(20, add, remove, save, users, logout);
        VBox vbox = new VBox(20, table, title_Label, title_Field, author_Label, author_Field, category_Label, category_Field, isbn_Label, isbnAndSpinner, region, hbox);
        vbox.setPadding(new Insets(10));
        VBox.setVgrow(region, Priority.ALWAYS);

        Scene scene = new Scene(vbox, 1000, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public TableView generateColumns() {
        TableColumn<Book, String> titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMinWidth(320);


        TableColumn<Book, String> authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setMinWidth(200);


        TableColumn<Book, String> categoryColumn = new TableColumn("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setMinWidth(150);

        TableColumn<Book, String> isbnColumn = new TableColumn("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setMinWidth(200);

        TableColumn<Book, Integer> quantityColumn = new TableColumn("#");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(50);

        TableView table = new TableView();
        table.getColumns().addAll(titleColumn, authorColumn, categoryColumn, isbnColumn, quantityColumn);

        return table;

    }

    public ArrayList<Book> load() {
        // Reading the object from a file
        ArrayList<Book> listOfBooks = null;
        try {
            FileInputStream file = new FileInputStream(new File("listOfBooks.bin"));
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            listOfBooks = (ArrayList<Book>) in.readObject();

            in.close();
            file.close();

            System.out.println("Books Loaded");
        } catch (IOException e) {
            System.out.println("File is not found.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class is not found");
        }
        return listOfBooks;
    }
}