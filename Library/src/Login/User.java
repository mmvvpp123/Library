package Login;

import Library.Book;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String email;
    private String password;
    private String name;
    private String type;
    private ArrayList<Book> borrowedBooks = new ArrayList<>();

    User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        type = "User";
    }

    public void addBorrowedBooks(Book k) {
        borrowedBooks.add(k);
    }

    public static byte[] encrypt(byte[] password) {
        byte[] encrypted = new byte[password.length];

        for (int i = 0; i < encrypted.length; i++) {
            if (i % 2 == 0) {
                encrypted[i] = (byte) (password[i] - 1);
            } else encrypted[i] = (byte) (password[i] + 1);
        }
        return encrypted;
    }

    public static byte[] decrypt(byte[] encrypted) {
        byte[] decrypted = new byte[encrypted.length];

        for (int i = 0; i < decrypted.length; i++) {
            if (i % 2 == 0) {
                decrypted[i] = (byte) (encrypted[i] + 1);
            } else decrypted[i] = (byte) (encrypted[i] - 1);
        }
        return decrypted;
    }

    public static void save(User x) {
        try {
            File userFile = new File("/Users/sherzodnimatullo/Library-School-Project/users/" + x.email + ".bin");
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(userFile);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(x);

            out.close();
            file.close();

            System.out.println("Object has been serialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User load(File f) {
        User user = null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            user = (User) in.readObject();

            in.close();
            file.close();

            return user;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter the correct email. If you don't have an account, click the Sign Up button");
            alert.show();
        }
        return null;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public String getName() { return name; }

    public ArrayList<Book> getBorrowedBooks() {
        if (!(borrowedBooks.size() > 1)) {
            System.out.println("True");
            return new ArrayList<Book>();
        }
        else
            return borrowedBooks;
    }
}