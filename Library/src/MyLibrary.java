import java.util.ArrayList;

public class MyLibrary {
    public ArrayList<Book> listOfBooks = new ArrayList<>();

    MyLibrary(ArrayList<Book> listOfBooks) {
        for (int i = 0; i < listOfBooks.size(); i++) {
            Book tempBook = new Book(listOfBooks.get(i).getTitle(), listOfBooks.get(i).getAuthor(), listOfBooks.get(i).getCategory(), listOfBooks.get(i).getIsbn());
            this.listOfBooks.add(tempBook);
        }
    }

    //public add(Book k) {

   // }

}
