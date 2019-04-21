package Library;

import Login.LogInScreen;
import Login.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class UserWindow extends Application implements Serializable {

    private Stage currentStage;

    private User user;

    private AdminWindow admin = new AdminWindow();
    private MyLibrary library = new MyLibrary(admin.load());

    private TableView availableBooksTable = admin.generateColumns();
    private ObservableList<Book> availableBooksList = FXCollections.observableArrayList();


    private TableView myBooksTable = admin.generateColumns();
    private ObservableList<Book> borrowedBooks;

    private ImageView logOffImg, borrowImg, returnImg;

    public UserWindow(User user) throws IOException, ClassNotFoundException {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        currentStage = primaryStage;
        currentStage.setTitle(user.getName()+"'s Window");
        borrowedBooks = loadData();

        for (Book k : admin.load())
            availableBooksList.add(k);

        Button borrow = new Button("Borrow");
        borrow.setDefaultButton(true);
        borrowImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/borrow.png").toURI().toString());
        borrowImg.setPreserveRatio(true);
        borrowImg.setFitWidth(25);
        borrow.setGraphic(borrowImg);

        borrow.setDefaultButton(true);
        borrow.setOnAction(e -> {
            Book selectedBook = (Book)availableBooksTable.getSelectionModel().getSelectedItem();
            moveBook(availableBooksList, borrowedBooks, user, library, selectedBook);
            save(user, library.getList());
        });

        Button returnBook = new Button("Return");
        returnImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/return.png").toURI().toString());
        returnImg.setPreserveRatio(true);
        returnImg.setFitWidth(25);
        returnBook.setGraphic(returnImg);

        returnBook.setOnAction(event -> {
            Book selectedBook = (Book)myBooksTable.getSelectionModel().getSelectedItem();
            moveBook(borrowedBooks, availableBooksList, library, user, selectedBook);
            save(user, library.getList());
        });

        TabPane tabPane = new TabPane();
        Tab myBooks = new Tab("My Books");

        VBox myBooksBox = new VBox(20, myBooksTable, returnBook);
        myBooksBox.setPadding(new Insets(5));
        myBooks.setContent(myBooksBox);

        Tab availableBooks = new Tab("Available Books");
        availableBooksTable.setItems(availableBooksList);

        VBox availableBox = new VBox(20, availableBooksTable, borrow);
        availableBox.setPadding(new Insets(5));
        availableBooks.setContent(availableBox);

        tabPane.getTabs().addAll(availableBooks, myBooks);

        Label name = new Label("Hello, " + user.getName());
        Button logoff = new Button("Log Out");
        logOffImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/logout.png").toURI().toString());
        logOffImg.setPreserveRatio(true);
        logOffImg.setFitWidth(15);
        logoff.setGraphic(logOffImg);
        Region region = new Region();

        HBox logOffandName = new HBox( name, region, logoff);
        logOffandName.setPadding(new Insets(20));
        HBox.setHgrow(region, Priority.ALWAYS);

        logoff.setOnAction(event -> restart(LogInScreen.scene));
        VBox vbox = new VBox(20, logOffandName, tabPane);

        Scene scene = new Scene(vbox, 1000, 500);
        currentStage.setScene(scene);
        currentStage.show();
    }

    void restart (Scene scene) {
        try {
            currentStage.close();
            LogInScreen login = new LogInScreen();
            login.start(login.currentStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        availableBooksTable.refresh();
        myBooksTable.refresh();
    }

    private void save(User c, ArrayList<Book> k) {
        User.save(c);
        AdminWindow.save(k);
    }

    private void moveBook(ObservableList<Book> fromList, ObservableList<Book> toList, Object to, Object from, Book selectedBook) {
        int indexOf = indexOfBook(toList, selectedBook);

        if (selectedBook.getQuantity() == 1) {
            fromList.remove(selectedBook);
            if (from instanceof User)
                ((User)from).remove(selectedBook);
            else
                ((MyLibrary)from).remove(selectedBook);

            if(indexOf > -1) {
                toList.get(indexOf).incQuantity();
                refresh();
            }
            else {
                toList.add(selectedBook);
                if(to instanceof User)
                    ((User)to).add(selectedBook);
                else
                    ((MyLibrary)to).add(selectedBook);
            }
        }
        else {
            for (int i = 0; i < fromList.size(); i++) {
                if (fromList.get(i).getTitle().equals(selectedBook.getTitle())) {
                    fromList.get(i).decQuantity();
                    if (from instanceof User)
                        ((User)from).getList().get(i).decQuantity();
                    else
                        ((MyLibrary)from).getList().get(i).decQuantity();
                    refresh();

                    for (int j = 0; j < toList.size(); j++) {
                        if (toList.get(j).getTitle().equals(selectedBook.getTitle())) {
                            toList.get(j).incQuantity();
                            if (to instanceof User)
                                ((User)to).getList().get(j).incQuantity();
                            else
                                ((MyLibrary)to).getList().get(j).incQuantity();

                            refresh();
                            return;
                        }
                    }
                    toList.add(new Book(selectedBook.getTitle(), selectedBook.getAuthor(),
                                        selectedBook.getCategory(), selectedBook.getIsbn()));
                    if (to instanceof User)
                        ((User)to).add(new Book(selectedBook.getTitle(), selectedBook.getAuthor(),
                            selectedBook.getCategory(), selectedBook.getIsbn()));
                    else
                        ((MyLibrary)to).add(new Book(selectedBook.getTitle(), selectedBook.getAuthor(),
                                selectedBook.getCategory(), selectedBook.getIsbn()));
                    return;
                }
            }
        }
    }

    private int indexOfBook(ObservableList<Book> books, Book k) {
        for(int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(k.getTitle()))
                return i;
        }
        return -1;
    }

    private ObservableList<Book> loadData() {
        if(user.getList() != null) {
            ObservableList<Book> temp = FXCollections.observableArrayList(user.getList());
            myBooksTable.setItems(temp);
            return temp;
        }
        else
            return FXCollections.observableArrayList();
    }
}