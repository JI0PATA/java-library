package biblioteka;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DolgController {

    @FXML
    private TableView<Orders> catalogTable;
    @FXML
    private TableColumn<Orders, String> login;
    @FXML
    private TableColumn<Orders, String> dolg;
    private final ObservableList<Orders> productData1 = FXCollections.observableArrayList();

    public void initialize() {

        ObservableList<Orders> mas1 = FXCollections.observableArrayList();
        String line1;
        java.io.File file1 = new java.io.File("src/biblioteka/orders.txt");
        InputStream orders_stream = Books.class.getClass().getResourceAsStream("orders.txt");
        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(orders_stream, "utf-8"));) {
            while ((line1 = reader1.readLine()) != null) {
                String[] s1 = line1.split(" ");
                if (s1[2].equals("должник")) {
                    Orders orders = new Orders(Integer.valueOf(s1[0]), s1[1], s1[2]);
                    mas1.add(orders);
                }
            }
            productData1.addAll(mas1);

            login.setCellValueFactory(new PropertyValueFactory<>("login"));
            dolg.setCellValueFactory(new PropertyValueFactory<>("dolg"));
            catalogTable.setItems(productData1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
