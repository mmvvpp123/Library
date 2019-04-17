package Library;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class javafxSide extends Application {
    private TableView table = new TableView();


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Table View Sample");
        primaryStage.setWidth(300);
        primaryStage.setHeight(500);


        table.setEditable(true);

        TableColumn <Book, String> titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));


        TableColumn <Book, String> authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn <Book, String> categoryColumn = new TableColumn("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        table.getColumns().addAll(titleColumn, authorColumn, categoryColumn);
        //table.setItems(getBooks());

        VBox vbox = new VBox(20, table);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
