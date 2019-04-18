package Library;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String category;
    private String isbn;
    private int quantity = 1;

    public Book(String title, String author, String category, String isbn) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
    }
//    public Book(String title, String author, String category, String isbn, int quantity) {
//        this.title = title;
//        this.author = author;
//        this.category = category;
//        this.isbn = isbn;
//        this.quantity = quantity;
//    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void decQuantity() { quantity--; }
    public void incQuantity() { quantity++;}
    public void setQuantity(int k) { quantity = k; }
    public int getQuantity() { return quantity; }
}
