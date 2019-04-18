package Library;

import Login.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserWindow extends Application implements Serializable {

    private User user;

    private AdminWindow admin = new AdminWindow();
    private TableView availableBooksTable = admin.generateColumns();
    private ObservableList<Book> availableBooksList = admin.load();

    private TableView myBooksTable = admin.generateColumns();
    private ObservableList<Book> borrowedBooks;

    public UserWindow(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("User Window");
        borrowedBooks = loadData();

        Button borrow = new Button("Borrow");
        borrow.setOnAction(e -> {
            Book selectedItem = (Book)availableBooksTable.getSelectionModel().getSelectedItem();
            user.addBorrowedBooks(selectedItem);
            borrowedBooks.add(selectedItem);
            admin.remove(selectedItem);
            availableBooksList.remove(selectedItem);
            User.save(user);
            admin.save(availableBooksList);
        });

        Button returnBook = new Button("Return");
        returnBook.setOnAction(event -> {
            Book selectedItem = (Book)myBooksTable.getSelectionModel().getSelectedItem();
            borrowedBooks.remove(selectedItem);
            availableBooksList.add(selectedItem);
            admin.save(availableBooksList);
            User.save(user);
        });

        TabPane tabPane = new TabPane();
        Tab myBooks = new Tab("My Books");

        VBox myBooksBox = new VBox(myBooksTable, returnBook);
        myBooks.setContent(myBooksBox);

        Tab availableBooks = new Tab("Available Books");
        availableBooksTable.setItems(availableBooksList);
        VBox availableBox = new VBox(availableBooksTable, borrow);
        availableBooks.setContent(availableBox);

        tabPane.getTabs().addAll(availableBooks, myBooks);

        Label name = new Label("Hello, " + user.getName());
        VBox vbox = new VBox(20, name, tabPane, borrow);

        Scene scene = new Scene(vbox, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObservableList<Book> loadData() {
        if(user.getBorrowedBooks() != null) {
            ObservableList<Book> temp = FXCollections.observableArrayList(user.getBorrowedBooks());
            myBooksTable.setItems(temp);
            return temp;
        }
        else
            return FXCollections.observableArrayList();
    }
}