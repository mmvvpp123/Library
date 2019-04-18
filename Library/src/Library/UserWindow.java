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

public class UserWindow extends Application implements Serializable {

    private User user;

    private AdminWindow admin = new AdminWindow();
    private MyLibrary library = new MyLibrary(admin.load());

    private TableView availableBooksTable = admin.generateColumns();
    private ObservableList<Book> availableBooksList = FXCollections.observableArrayList();


    private TableView myBooksTable = admin.generateColumns();
    private ObservableList<Book> borrowedBooks;

    public UserWindow(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("User Window");
        borrowedBooks = loadData();

        for (Book k : admin.load())
            availableBooksList.add(k);

        Button borrow = new Button("Borrow");
        borrow.setOnAction(e -> {
            Book selectedBook = (Book)availableBooksTable.getSelectionModel().getSelectedItem();
            int indexOf = indexOfBook(borrowedBooks, selectedBook);


            if (selectedBook.getQuantity() == 1) {
                availableBooksList.remove(selectedBook);
                library.remove(selectedBook);

                if (indexOf > -1) {
                    borrowedBooks.get(indexOf).incQuantity();
                    myBooksTable.refresh();
                    availableBooksTable.refresh();
                }
                else borrowedBooks.add(selectedBook);

            }
            else
                for (int i = 0; i < availableBooksList.size(); i++) {
                    if (availableBooksList.get(i).getTitle().equals(selectedBook.getTitle())) {
                        availableBooksList.get(i).decQuantity();
                        library.getList().get(i).decQuantity();
                        availableBooksTable.refresh();

                        for (int j = 0; j < borrowedBooks.size(); j++) {
                            if (borrowedBooks.get(j).getTitle().equals(selectedBook.getTitle())) {
                                borrowedBooks.get(j).incQuantity();
                                myBooksTable.refresh();
                                return;
                            }
                        }
                        borrowedBooks.add(new Book(selectedBook.getTitle(), selectedBook.getAuthor(), selectedBook.getCategory(), selectedBook.getIsbn()));
                        return;
                    }
                }
        });

        Button returnBook = new Button("Return");
        returnBook.setOnAction(event -> {
            Book selectedBook = (Book)myBooksTable.getSelectionModel().getSelectedItem();

            if (selectedBook.getQuantity() == 1) {
                borrowedBooks.remove(selectedBook);
                library.add(selectedBook);

                availableBooksList.add(selectedBook);
            }
            else
                for (int i = 0; i < borrowedBooks.size(); i++) {
                    if (borrowedBooks.get(i).getTitle().equals(selectedBook.getTitle())) {
                        borrowedBooks.get(i).decQuantity();
                        library.getList().get(i).incQuantity();
                        myBooksTable.refresh();

                        for (int j = 0; j < availableBooksList.size(); j++) {
                            if (availableBooksList.get(j).getTitle().equals(selectedBook.getTitle())) {
                                availableBooksList.get(j).incQuantity();
                            }
                        }
                        return;
                    }
                }
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

    private int indexOfBook(ObservableList<Book> books, Book k) {
        for(int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(k.getTitle()))
                return i;
        }
        return -1;
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