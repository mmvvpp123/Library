package project.Library;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.LogIn.LogInScreen;
import project.LogIn.User;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class UserWindow extends Application implements Serializable {

    private Stage currentStage;

    private User user;
    private boolean guest;

    private AdminWindow admin = new AdminWindow();
    private MyLibrary library = new MyLibrary(admin.load());

    private TableView availableBooksTable = admin.generateColumns();
    private ObservableList<Book> availableBooksList = FXCollections.observableArrayList();

    private TableView myBooksTable = admin.generateColumns();
    private ObservableList<Book> borrowedBooks;

    private ImageView logOffImg, borrowImg, returnImg, searchImg;

    public UserWindow(User user) {
        this.user = user;
    }

    public UserWindow(User user, boolean guest) {
        this(user);
        this.guest = guest;
    }

    @Override
    public void start(Stage primaryStage) {
        currentStage = primaryStage;
        currentStage.setTitle(user.getName() + "'s Window");
        borrowedBooks = loadData();
        TabPane tabPane = new TabPane();

        for (Book k : admin.load())
            availableBooksList.add(k);

        Button borrow = new Button("Borrow");
        borrowImg = new ImageView(new File("vectors/borrow.png").toURI().toString());
        borrowImg.setPreserveRatio(true);
        borrowImg.setFitWidth(25);
        borrow.setGraphic(borrowImg);

        borrow.setOnAction(e -> {
            borrow((Book) availableBooksTable.getSelectionModel().getSelectedItem());
            save(user, library.getList());
        });

        Button returnBook = new Button("Return");
        returnImg = new ImageView(new File("vectors/return.png").toURI().toString());
        returnImg.setPreserveRatio(true);
        returnImg.setFitWidth(25);
        returnBook.setGraphic(returnImg);

        returnBook.setOnAction(event -> {
            returnBook((Book) myBooksTable.getSelectionModel().getSelectedItem());
            save(user, library.getList());
        });

        searchImg = new ImageView(new File("vectors/search.png").toURI().toString());
        searchImg.setPreserveRatio(true);
        searchImg.setFitWidth(15);
        Button search = new Button("Search");
        search.setGraphic(searchImg);
        search.setDefaultButton(true);
        TextField searchBox = new TextField();
        search.setOnAction(e -> {
            TableView table;
            ObservableList<Book> list;
            String selectedTab = tabPane.getSelectionModel().getSelectedItem().getText();
            if (selectedTab.equals("My Books")) {
                table = myBooksTable;
                list = borrowedBooks;
            } else {
                table = availableBooksTable;
                list = availableBooksList;
            }

            String query = searchBox.getText().toLowerCase();
            if (searchBox.getText().length() == 0) {
                table.setItems(list);
            } else {
                ObservableList<Book> searchList = FXCollections.observableArrayList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTitle().toLowerCase().contains(query) ||
                            list.get(i).getAuthor().toLowerCase().contains(query) ||
                            list.get(i).getCategory().toLowerCase().contains(query) ||
                            list.get(i).getIsbn().toLowerCase().contains(query)) {
                        searchList.add(list.get(i));
                    }
                }
                if (selectedTab.equals("My Books"))
                    myBooksTable.setItems(searchList);
                else
                    availableBooksTable.setItems(searchList);
            }
        });

        Tab myBooks = new Tab("My Books");

        VBox myBooksBox = new VBox(20, myBooksTable, returnBook);
        myBooksBox.setPadding(new Insets(10));
        myBooks.setContent(myBooksBox);

        Tab availableBooks = new Tab("Available Books");
        availableBooksTable.setItems(availableBooksList);

        VBox availableBox = new VBox(20, availableBooksTable, borrow);
        availableBox.setPadding(new Insets(10));
        availableBooks.setContent(availableBox);

        tabPane.getTabs().addAll(availableBooks, myBooks);

        Label name = new Label("Hello, " + user.getName());
        Button logoff = new Button("Log Out");
        logOffImg = new ImageView(new File("vectors/logout.png").toURI().toString());
        logOffImg.setPreserveRatio(true);
        logOffImg.setFitWidth(15);
        logoff.setGraphic(logOffImg);
        Region region = new Region();

        HBox logOffandName = new HBox(20, name, region, searchBox, search, logoff);
        logOffandName.setPadding(new Insets(10));
        HBox.setHgrow(region, Priority.ALWAYS);

        logoff.setOnAction(event -> restart());
        VBox vbox = new VBox(20, logOffandName, tabPane);

        Scene scene = new Scene(vbox, 1000, 500);

        if (guest == true) {
            myBooks.setDisable(true);
            borrow.setDisable(true);
            Label label = new Label("You are currently in guest mode. To have full access, please create an account.");
            availableBox.getChildren().add(label);
        }

        currentStage.setScene(scene);
        currentStage.show();
    }

    void restart() {
        try {
            currentStage.close();
            LogInScreen login = new LogInScreen();
            login.start(login.currentStage);
        } catch (Exception e) {
            System.out.println("Restart failed!");
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

    private void borrow(Book k) {
        int toBook = indexOfBook(borrowedBooks, k);
        int fromBook = indexOfBook(availableBooksList, k);
        try {
            if (k.getQuantity() == 1) {
                availableBooksList.remove(k);
                library.remove(k);

                if (toBook > -1) {
                    borrowedBooks.get(toBook).incQuantity(1);
                    user.getList().get(toBook).incQuantity(1);
                } else {
                    borrowedBooks.add(k);
                    user.add(k);
                }
                refresh();
            } else {
                if (toBook < 0) {
                    borrowedBooks.add(new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn()));
                    user.add(new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn()));
                } else {
                    borrowedBooks.get(toBook).incQuantity(1);
                    user.getList().get(toBook).incQuantity(1);
                }
                availableBooksList.get(fromBook).decQuantity(1);
                library.getList().get(fromBook).decQuantity(1);
                refresh();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void returnBook(Book k) {
        int toBook = indexOfBook(availableBooksList, k);
        int fromBook = indexOfBook(borrowedBooks, k);
        try {
            if (k.getQuantity() == 1) {
                borrowedBooks.remove(k);
                user.remove(k);

                if (toBook > -1) {
                    availableBooksList.get(toBook).incQuantity(1);
                    library.getList().get(toBook).incQuantity(1);
                } else {
                    availableBooksList.add(k);
                    library.add(k);
                }
                refresh();
            } else {
                if (toBook < 0) {
                    availableBooksList.add(new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn()));
                    library.add(new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn()));
                } else {
                    availableBooksList.get(toBook).incQuantity(1);
                    library.getList().get(toBook).incQuantity(1);
                }
                borrowedBooks.get(fromBook).decQuantity(1);
                int initialQuantity = borrowedBooks.get(fromBook).getQuantity();
                if (user.getList().get(fromBook).getQuantity() > initialQuantity) {
                    user.getList().get(fromBook).decQuantity(1);
                }
                refresh();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private int indexOfBook(ObservableList<Book> books, Book k) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(k.getTitle()))
                return i;
        }
        return -1;
    }

    private ObservableList<Book> loadData() {
        if (user.getList() != null) {
            ObservableList<Book> temp = FXCollections.observableArrayList(user.getList());
            myBooksTable.setItems(temp);
            return temp;
        } else
            return FXCollections.observableArrayList();
    }
}