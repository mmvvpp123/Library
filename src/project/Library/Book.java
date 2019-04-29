package project.Library;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 220485221L;
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

    public void decQuantity(int decreaseAmount) { quantity -= decreaseAmount; }
    public void incQuantity(int increaseAmount) { quantity += increaseAmount; }
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public int getQuantity() { return quantity; }
}
