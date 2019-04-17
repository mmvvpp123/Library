package Library;

public class Book {
    private String title;
    private String author;
    private String category;
    private String isbn;

    public Book(String title, String author, String category, String isbn) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
    }

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
}
