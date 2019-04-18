package Library;

import Login.User;
import java.util.ArrayList;

public class MyLibrary {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers = new ArrayList<>();

    MyLibrary(ArrayList<Book> listOfBooks) {
        this.listOfBooks = new ArrayList<>();
        for (int i = 0; i < listOfBooks.size(); i++) {
            Book tempBook = new Book(listOfBooks.get(i).getTitle(), listOfBooks.get(i).getAuthor(), listOfBooks.get(i).getCategory(), listOfBooks.get(i).getIsbn());
            this.listOfBooks.add(tempBook);
        }
    }

    MyLibrary() {
        this.listOfBooks = new ArrayList<>();
    }

    public void add(Book k) {
        Book temp = new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn());
        for (int i = 0; i < listOfBooks.size(); i++) {
            if (listOfBooks.get(i).getTitle().equals(k.getTitle())) {
                k.incQuantity();
                temp.setQuantity(k.getQuantity());

                listOfBooks.remove(i);
                break;
            }
        }
        listOfBooks.add(temp);

    }

    public void remove(Book k) {
        for (int i = 0; i < listOfBooks.size(); i++) {
            if (listOfBooks.get(i).getTitle().equals(k.getTitle())) {
                if (k.getQuantity() - 1 == 0) {
                    listOfBooks.remove(k);
                } else
                    k.decQuantity();
            }
        }
    }
}
