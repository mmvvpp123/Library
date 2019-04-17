package Library;

import Login.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class MyLibrary {
    private ArrayList<Book> listOfBooks = new ArrayList<>();
    private ArrayList<User> listOfUsers = new ArrayList<>();

    MyLibrary(ArrayList<Book> listOfBooks) {
        for (int i = 0; i < listOfBooks.size(); i++) {
            Book tempBook = new Book(listOfBooks.get(i).getTitle(), listOfBooks.get(i).getAuthor(), listOfBooks.get(i).getCategory(), listOfBooks.get(i).getIsbn());
            this.listOfBooks.add(tempBook);
        }
    }

    public void add (Book k) {
        listOfBooks.add(new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn()));
    }

    public void remove (Book k) {
        for (int i = 0; i < listOfBooks.size(); i++) {
            if (listOfBooks.get(i).getIsbn().equals(k.getIsbn())) {
                listOfBooks.remove(i);
            }
        }
    }

    public ArrayList<Book> getListOfBooks () {
        return listOfBooks;
    }

    public ObservableList<Book> getBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        for (int i = 0; i < listOfBooks.size(); i++) {
            books.add(listOfBooks.get(i));
        }
        return books;
    }
}
