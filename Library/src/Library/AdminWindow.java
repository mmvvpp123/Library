package Library;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class AdminWindow extends Application implements Serializable {
    private TableView table = generateColumns();
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private MyLibrary library = new MyLibrary(new ArrayList<>());

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
        Button remove = new Button("Remove Book");
        load.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File f = fileChooser.showOpenDialog(primaryStage);
            ObservableList<Book> temp = load(f);
            for (int i = 0; i < temp.size(); i++) {
                books.add(temp.get(i));
            }
            temp = null;
        });

        save.setOnAction(e -> save(books));
        remove.setOnAction(e -> {
            remove ((Book)table.getSelectionModel().getSelectedItem());
        });

        add.setOnAction(e -> {
            Book temp = new Book(title_Field.getText(), author_Field.getText(), category_Field.getText(), isbn_Field.getText());
            if(books.size() > 0) {
                for (int i = 0; i < books.size(); i++) {
                    if (books.get(i).getTitle().equals(temp.getTitle())) {
                        temp = books.get(i);
                        books.remove(books.get(i));
                    }
                }
            }
            books.add(temp);
            library.add(temp);
            title_Field.setText("");
            author_Field.setText("");
            category_Field.setText("");
            isbn_Field.setText("");
        });


        table.setItems(books);


        HBox hbox = new HBox(20, add, remove, save, load);
        VBox vbox = new VBox(20, table, title_Label, title_Field, author_Label, author_Field, category_Label, category_Field, isbn_Label, isbn_Field, hbox);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 900,680);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public TableView generateColumns() {
        TableColumn<Book, String> titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMinWidth(320);


        TableColumn <Book, String> authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setMinWidth(200);


        TableColumn <Book, String> categoryColumn = new TableColumn("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setMinWidth(200);

        TableColumn <Book, String> isbnColumn = new TableColumn("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setMinWidth(300);

        TableColumn <Book, Integer> quantityColumn = new TableColumn("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(100);

        TableView table = new TableView();
        table.getColumns().addAll(titleColumn, authorColumn, categoryColumn, isbnColumn, quantityColumn);

        return table;

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

    public ObservableList<Book> load() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(new File("/Users/sherzodnimatullo/Library-School-Project/listOfBooks.bin"));
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

    public void remove(Book k) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(k.getTitle())) {
                books.get(i).decQuantity();
            }
            else {
                books.remove(k);
                library.remove(k);
                break;
            }
        }
    }
}