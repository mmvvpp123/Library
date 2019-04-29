package project.Library;

import java.util.ArrayList;

public class MyLibrary {
    private ArrayList<Book> listOfBooks;

    MyLibrary(ArrayList<Book> listOfBooks) {
        this.listOfBooks = listOfBooks;
    }

    MyLibrary() {
        this.listOfBooks = new ArrayList<>();
    }

    public void add(Book k) {
        Book temp = new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn());
        listOfBooks.add(temp);
        }

    public void remove(Book k) {
        for (Book inList:listOfBooks) {
            if (k.getTitle().equals(inList.getTitle())) {
                listOfBooks.remove(inList);
                AdminWindow.save(listOfBooks);
                return;
            }
        }
    }

    public ArrayList<Book> getList() {
        return listOfBooks;
    }
}
