package biblioteka;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeBibliotekarController {

    private final ObservableList<Books> productData = FXCollections.observableArrayList();
    private final ObservableList<Orders> productData1 = FXCollections.observableArrayList();
    private final ObservableList<Books> productData2 = FXCollections.observableArrayList();
    private final ObservableList<Orders> productData3 = FXCollections.observableArrayList();

    @FXML
    private Button button1;
    @FXML
    private TableColumn<Books, String> id;
    @FXML
    private TableColumn<Books, String> avtor;
    @FXML
    private TableColumn<Books, String> nazvanie;
    @FXML
    private TableColumn<Books, String> nalichie;
    @FXML
    private TableView<Books> booksTable;
    @FXML
    private Button button2;
    @FXML
    private TableColumn<Orders, String> idd;
    @FXML
    private TableColumn<Orders, String> login;
    @FXML
    private TableColumn<Orders, String> dolg;
    @FXML
    private TableView<Orders> ordersTable;
    @FXML
    private Tab tab;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private Button button3;

    public void initialize() {//в этом методе заполняются две таблицы: каталог книг и заказы
        textField1.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                button2.fire();
            }
        });
        textField2.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                button3.fire();
            }
        });
        Orders orders = new Orders();
        ObservableList<Books> mas = FXCollections.observableArrayList();
        String line;
        InputStream books_stream = getClass().getResourceAsStream("books.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(books_stream, "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] s = line.split("&");
                Books books = new Books(Integer.valueOf(s[0]), s[1], s[2], Integer.valueOf(s[3]));
                mas.add(books);
            }
            productData.addAll(mas);
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            avtor.setCellValueFactory(new PropertyValueFactory<>("avtor"));
            nazvanie.setCellValueFactory(new PropertyValueFactory<>("nazvanie"));
            nalichie.setCellValueFactory(new PropertyValueFactory<>("nalichie"));
            booksTable.setItems(productData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<Orders> mas1 = FXCollections.observableArrayList();
        String line1;
        InputStream orders_stream = getClass().getResourceAsStream("orders.txt");
        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(orders_stream, "utf-8"))) {
            while ((line1 = reader1.readLine()) != null) {
                String[] s1 = line1.split(" ");
                orders = new Orders(Integer.valueOf(s1[0]), s1[1], s1[2]);
                mas1.add(orders);
            }
            productData1.addAll(mas1);
            idd.setCellValueFactory(new PropertyValueFactory<>("idd"));
            login.setCellValueFactory(new PropertyValueFactory<>("login"));
            dolg.setCellValueFactory(new PropertyValueFactory<>("dolg"));
            ordersTable.setItems(productData1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editDolg() {// в этом методе мы меняем не должника на должника, если срок сдачи книги истек
        Orders selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            int ID = ordersTable.getSelectionModel().getSelectedIndex();
            if (selectedOrder.getDolg().equals("ок")) {
                selectedOrder.setDolg("должник");
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setContentText("Ошибка");
                alert.setHeaderText("Ошибка");
                alert.showAndWait();
            }
            productData1.set(ID, selectedOrder);
            ordersTable.setItems(productData1);
            ordersTable.getItems();
            ObservableList<Orders> mas;
            mas = ordersTable.getItems();
            Orders.rewrite(mas);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Выберите заказ, чтобы изменить");
            alert.setHeaderText("Ничего не выбрано");
            alert.showAndWait();
        }
    }

    @FXML
    private void delete() { // в этом методе мы удаляем заказ со списка заказов
        int selectedIndex = ordersTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {

            ordersTable.getItems().remove(selectedIndex);
            ObservableList<Orders> mas;
            mas = ordersTable.getItems();
            Orders.rewrite(mas);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Выберите заказ, чтобы удалить");
            alert.setHeaderText("Ничего не выбрано");
            alert.showAndWait();
        }
    }

    @FXML
    private void serahId() {//в этом методе мы по артикулу ищем нужную нам книгу в каталоге и оставляем в таблице только ее

        String ID = textField1.getText();
        boolean f = false;
        ObservableList<Books> mas = FXCollections.observableArrayList();
        String line;
        InputStream books_stream = getClass().getResourceAsStream("books.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(books_stream, "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] s = line.split("&");
                if (s[0].equals(ID)) {
                    f = true;
                    Books books = new Books(Integer.valueOf(s[0]), s[1], s[2], Integer.valueOf(s[3]));
                    mas.add(books);
                }
            }
            if (f == false) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Такой книги нет");
                alert.setContentText("Проверьте введенные данные");
                alert.showAndWait();
            } else {
                productData2.addAll(mas);
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                avtor.setCellValueFactory(new PropertyValueFactory<>("avtor"));
                nazvanie.setCellValueFactory(new PropertyValueFactory<>("nazvanie"));
                nalichie.setCellValueFactory(new PropertyValueFactory<>("nalichie"));
                booksTable.setItems(productData2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchLogin() {//в этом методе мы ищем нужный нам заказ по логину заказчика и оставляем только его в таблице
        String LOGIN = textField2.getText();
        boolean f = false;
        ObservableList<Orders> mas = FXCollections.observableArrayList();
        String line;
        InputStream orders_stream = getClass().getResourceAsStream("orders.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(orders_stream, "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] s = line.split(" ");
                if (s[1].equals(LOGIN)) {
                    f = true;
                    Orders orders = new Orders(Integer.valueOf(s[0]), s[1], s[2]);
                    mas.add(orders);
                }
            }
            if (f == false) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Такого пользователя нет");
                alert.setContentText("Проверьте введенные данные");
                alert.showAndWait();
            } else {
                productData3.addAll(mas);
                idd.setCellValueFactory(new PropertyValueFactory<>("idd"));
                login.setCellValueFactory(new PropertyValueFactory<>("login"));
                dolg.setCellValueFactory(new PropertyValueFactory<>("dolg"));
                ordersTable.setItems(productData3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void close() throws IOException {//с помощью данного метода можно сменить пользователя
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        button1.getScene().getWindow().hide();

    }

    @FXML
    private void handleShow() throws IOException {

        //загружаем FXML сцену
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ShowDiagram.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Статистика наличия");
        dialogStage.initModality(Modality.WINDOW_MODAL);

        dialogStage.initOwner(null);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        //передает адресаты в контроллер
        ShowDiagramController controller = loader.getController();
        controller.setProduct();

        dialogStage.show();
    }

    public void hypetlinkBooks() {
        productData.clear();
        ObservableList<Books> mas = FXCollections.observableArrayList();
        String line;
        InputStream books_stream = getClass().getResourceAsStream("books.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(books_stream, "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] s = line.split("&");
                Books books = new Books(Integer.valueOf(s[0]), s[1], s[2], Integer.valueOf(s[3]));
                mas.add(books);
            }
            productData.addAll(mas);
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            avtor.setCellValueFactory(new PropertyValueFactory<>("avtor"));
            nazvanie.setCellValueFactory(new PropertyValueFactory<>("nazvanie"));
            nalichie.setCellValueFactory(new PropertyValueFactory<>("nalichie"));
            booksTable.setItems(productData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hyprlinkOrders() {
        productData1.clear();
        ObservableList<Orders> mas1 = FXCollections.observableArrayList();
        String line1;
        InputStream orders_stream = getClass().getResourceAsStream("orders.txt");
        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(orders_stream, "utf-8"))) {
            while ((line1 = reader1.readLine()) != null) {
                String[] s1 = line1.split(" ");
                Orders orders = new Orders(Integer.valueOf(s1[0]), s1[1], s1[2]);
                mas1.add(orders);
            }
            productData1.addAll(mas1);
            idd.setCellValueFactory(new PropertyValueFactory<>("idd"));
            login.setCellValueFactory(new PropertyValueFactory<>("login"));
            dolg.setCellValueFactory(new PropertyValueFactory<>("dolg"));
            ordersTable.setItems(productData1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleDolg() throws IOException {
        Stage stage = new Stage();
        Parent root = null;

        root = FXMLLoader.load(getClass().getResource("Dolg.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
