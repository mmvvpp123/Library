package project.Library;

import java.util.ArrayList;

class MyLibrary {
    private ArrayList<Book> listOfBooks;

    MyLibrary(ArrayList<Book> listOfBooks) {
        this.listOfBooks = listOfBooks;
    }

    public void add(Book k) {
        listOfBooks.add(new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn(), k.getQuantity()));
    }

    public void remove(Book k) {
        for (Book inList : listOfBooks) {
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
