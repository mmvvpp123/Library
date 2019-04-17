package Library;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class AdminClass extends Application implements Serializable {
    private TableView table = new TableView();
    private ObservableList<Book> books = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
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


        Button add = new Button("Add Book");
        Button save = new Button("Save");
        Button load = new Button("Load");
        load.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File f = fileChooser.showOpenDialog(primaryStage);
            books = load(f);
            table.setItems(books);

        });

        save.setOnAction(e -> save(books));

        add.setOnAction(e -> {
            Book temp = new Book(title_Field.getText(), author_Field.getText(), category_Field.getText(), isbn_Field.getText());
            books.add(temp);
            title_Field.setText("");
            author_Field.setText("");
            category_Field.setText("");
            isbn_Field.setText("");
        });

        TableColumn<Book, String> titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMinWidth(300);


        TableColumn <Book, String> authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));


        TableColumn <Book, String> categoryColumn = new TableColumn("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn <Book, String> isbnColumn = new TableColumn("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setMinWidth(200);


        table.getColumns().addAll(titleColumn, authorColumn, categoryColumn, isbnColumn);
        table.setItems(getBooks());


        VBox vbox = new VBox(20, table, title_Label, title_Field, author_Label, author_Field, category_Label, category_Field, isbn_Label, isbn_Field, add, save, load);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 1280,720);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public ObservableList<Book> getBooks() {
        books.add(new Book("YES", "IWROTETHIS", "MYSTERY", "123"));
        return books;
    }

    public void save(ObservableList<Book> books) {
        try {
            File userFile = new File("listOfBooks.bin");
            ArrayList<Book> listOfBooks = new ArrayList<>();
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(userFile);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object

            for (int i = 0; i < books.size(); i++) {
                listOfBooks.add(books.get(i));
            }
            out.writeObject(listOfBooks);

            out.close();
            file.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Saved");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Book> load(File f) {
        ObservableList<Book> books = FXCollections.observableArrayList();
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            ArrayList<Book> listOfBooks =  (ArrayList) in.readObject();

            for (int i = 0; i < listOfBooks.size(); i++) {
                books.add(listOfBooks.get(i));
            }

            in.close();
            file.close();

            return books;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
