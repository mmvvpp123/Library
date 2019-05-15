package project.LogIn;

import javafx.scene.control.Alert;
import project.Library.Book;

import java.io.*;
import java.util.ArrayList;

public class User implements Serializable {
    private String email;
    private String password;
    private String name;
    private String type;
    private ArrayList<Book> borrowedBooks;

    User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        type = "User";
        this.borrowedBooks = new ArrayList<>();
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
            File userFile = new File("users/" + x.email + ".bin");
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(userFile);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(x);

            out.close();
            file.close();

            System.out.println("Object has been serialized");
        } catch (Exception e) {
            System.out.println("User cannot be saved.");
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
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter the correct email. If you don't have an account, click the Sign Up button");
            alert.show();
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public void add(Book k) {
        borrowedBooks.add(new Book(k.getTitle(), k.getAuthor(), k.getCategory(), k.getIsbn()));
    }

    public void remove(Book k) {
        for (Book x : borrowedBooks) {
            if (x.getTitle().equals(k.getTitle())) {
                borrowedBooks.remove(x);
                return;
            }
        }
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Book> getList() {
        return borrowedBooks;
    }
}