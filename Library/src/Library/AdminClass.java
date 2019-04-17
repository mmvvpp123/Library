package Library;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminClass extends Application {
    private TableView table = new TableView();

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
        add.setOnAction(e -> {
            table.getItems().add(new Book(title_Field.getText(), author_Field.getText(), category_Field.getText(), isbn_Field.getText()));
            title_Field.setText("");
            author_Field.setText("");
            category_Field.setText("");
            isbn_Field.setText("");
        });

        TableColumn<Book, String> titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));


        TableColumn <Book, String> authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn <Book, String> categoryColumn = new TableColumn("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn <Book, String> isbnColumn = new TableColumn("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));


        table.getColumns().addAll(titleColumn, authorColumn, categoryColumn);
        table.setItems(getBooks());


        VBox vbox = new VBox(20, table, title_Label, title_Field, author_Label, author_Field, category_Label, category_Field, isbn_Label, isbn_Field, add);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public ObservableList<Book> getBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        books.add(new Book("YES", "IWROTETHIS", "MYSTERY", "123"));
        return books;
    }
}
