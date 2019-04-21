package Library;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class AdminWindow extends Application implements Serializable {
    private TableView table = generateColumns();
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private MyLibrary library = new MyLibrary(load());
    private ImageView addImg, removeImg, saveImg, loadImg;

    public AdminWindow() throws IOException, ClassNotFoundException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Admin Page");

        if (library.getList() == null)
            library = new MyLibrary();

        table.setEditable(true);

        Label title_Label = new Label("Title");
        TextField title_Field = new TextField();

        Label author_Label = new Label(("Author"));
        TextField author_Field = new TextField();

        Label category_Label = new Label(("Category"));
        TextField category_Field = new TextField();

        Label isbn_Label = new Label(("ISBN"));
        TextField isbn_Field = new TextField();


        Button add = new Button("Add Book");
        add.setDefaultButton(true);
        addImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/add.png").toURI().toString());
        addImg.setPreserveRatio(true);addImg.setFitWidth(20);
        add.setGraphic(addImg);


        Button save = new Button("Save");
        saveImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/save.png").toURI().toString());
        saveImg.setPreserveRatio(true);saveImg.setFitWidth(20);
        save.setGraphic(saveImg);

        Button load = new Button("Load");
        loadImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/load.png").toURI().toString());
        loadImg.setPreserveRatio(true);loadImg.setFitWidth(20);
        load.setGraphic(loadImg);

        Button remove = new Button("Remove Book");
        removeImg = new ImageView(new File("/Users/sherzodnimatullo/Library-School-Project/vectors/remove.png").toURI().toString());
        removeImg.setPreserveRatio(true);removeImg.setFitWidth(20);
        remove.setGraphic(removeImg);

        load.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File f = fileChooser.showOpenDialog(primaryStage);
            ArrayList<Book> temp = load(f);
            for (int i = 0; i < temp.size(); i++) {
                books.add(temp.get(i));
            }
            temp = null;
        });

        save.setOnAction(e -> save(library.getList()));


        remove.setOnAction(e -> {
            Book k = (Book)table.getSelectionModel().getSelectedItem();

            if (k.getQuantity() == 1) {
                books.remove(k);
                library.remove(k);
            }
            else
                for (int i = 0; i < books.size(); i++) {
                    if (books.get(i).getTitle().equals(k.getTitle())) {
                        books.get(i).decQuantity();
                        library.getList().get(i).decQuantity();
                        table.refresh();
                        return;
                    }
                }
        });

        add.setOnAction(e -> {
            Book temp = new Book(title_Field.getText(), author_Field.getText(), category_Field.getText(), isbn_Field.getText());




            if(books.size() > 0) {
                for (int i = 0; i < books.size(); i++) {
                    if (library.getList().get(i).getTitle().equals(temp.getTitle())) {
                        library.getList().get(i).incQuantity();
                        books.get(i).incQuantity();
                        table.refresh();


                        title_Field.setText("");
                        author_Field.setText("");
                        category_Field.setText("");
                        isbn_Field.setText("");
                        return;
                    }
                }
            }
            books.add(temp);
            library.add(temp);
            table.refresh();


            title_Field.setText("");
            author_Field.setText("");
            category_Field.setText("");
            isbn_Field.setText("");
        });


        table.setItems(books);


        HBox hbox = new HBox(20, add, remove, save, load);
        VBox vbox = new VBox(20, table, title_Label, title_Field, author_Label, author_Field, category_Label, category_Field, isbn_Label, isbn_Field, hbox);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 1000,600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public TableView generateColumns() {
        TableColumn<Book, String> titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMinWidth(320);


        TableColumn <Book, String> authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setMinWidth(200);


        TableColumn <Book, String> categoryColumn = new TableColumn("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setMinWidth(150);

        TableColumn <Book, String> isbnColumn = new TableColumn("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setMinWidth(200);

        TableColumn <Book, Integer> quantityColumn = new TableColumn("#");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(50);

        TableView table = new TableView();
        table.getColumns().addAll(titleColumn, authorColumn, categoryColumn, isbnColumn, quantityColumn);

        return table;

    }

    public static void save(ArrayList<Book> library) {
        try {
            File userFile = new File("listOfBooks.bin");

            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(userFile);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object

            out.writeObject(library);

            out.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> load(File f) {
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            ArrayList<Book> listOfBooks =  (ArrayList) in.readObject();

            in.close();
            file.close();

            return listOfBooks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Book> load() throws IOException, ClassNotFoundException {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(new File("/Users/sherzodnimatullo/Library-School-Project/listOfBooks.bin"));
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            ArrayList<Book> listOfBooks =  (ArrayList) in.readObject();

            in.close();
            file.close();

            System.out.println("Books Loaded");
            return listOfBooks;
    }
}