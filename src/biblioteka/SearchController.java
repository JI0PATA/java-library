package biblioteka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Optional;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class SearchController {

    private ObservableList<Books> productData = FXCollections.observableArrayList();
    private final ObservableList<Books> productData1 = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Books, String> id;
    @FXML
    private TableColumn<Books, String> avtor;
    @FXML
    private TableColumn<Books, String> nazvanie;
    @FXML
    private TableColumn<Books, String> nalichie;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TableView catalogTable;
    static ObservableList<Books> mas1 = FXCollections.observableArrayList();
    static ObservableList<Books> mas = FXCollections.observableArrayList();
    @FXML
    private Button button;
    static String ss;
    static String name;
    static String nal;
    @FXML
    private Button button1;

    public void initialize() {
        textField2.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                button.fire();
            }
        });
        textField1.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                button.fire();
            }
        });

        String line;
        InputStream books_stream = getClass().getResourceAsStream("books.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(books_stream, "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] s = line.split("&");
                Books books = new Books(Integer.valueOf(s[0]), s[1], s[2], Integer.valueOf(s[3]));
                mas.add(books);
            }

            productData.addAll(mas);
            productData1.addAll(mas);

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            avtor.setCellValueFactory(new PropertyValueFactory<>("avtor"));
            nazvanie.setCellValueFactory(new PropertyValueFactory<>("nazvanie"));
            nalichie.setCellValueFactory(new PropertyValueFactory<>("nalichie"));
            catalogTable.setItems(productData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchButton() {//данный метод срабатывает при поиске книги по автору и названию и выводит искомую книгу в таблицу
        mas1.clear();
        mas.clear();
        productData.clear();
        productData1.clear();
        String Xavtor = textField1.getText();
        boolean f = false;
        String Xnazv = textField2.getText();

        String line;
        InputStream books_stream = getClass().getResourceAsStream("books.txt");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(books_stream, "utf-8"))){
            while ((line = reader.readLine()) != null) {
                String[] s = line.split("&");
                String[] l = s[1].split(" ");
                mas1.add(new Books(Integer.valueOf(s[0]), s[1], s[2], Integer.valueOf(s[3])));
                if (l[0].equals(Xavtor) || (s[2].equals(Xnazv))) {
                    f = true;
                    ss = s[0];
                    name = s[2];
                    nal = s[3];

                    Books books = new Books(Integer.valueOf(s[0]), s[1], s[2], Integer.valueOf(s[3]));
                    mas.add(books);
                }
            }
            if (f == false) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Данной книги не бывает в библиотеке");
                alert.setContentText("Вы можете выбрать другую книгу");
                alert.showAndWait();
                initialize();
            }
            productData1.addAll(mas1);
            productData.addAll(mas);
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            avtor.setCellValueFactory(new PropertyValueFactory<>("avtor"));
            nazvanie.setCellValueFactory(new PropertyValueFactory<>("nazvanie"));
            nalichie.setCellValueFactory(new PropertyValueFactory<>("nalichie"));
            catalogTable.setItems(productData);
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
    public void chek() throws IOException {//в данном методе мы проверяем имеется ли выбранная пользователем книга в наличии, не должник ли клиент, проверяем на повторный заказ одной и той же книги
        boolean flag = true;
        Books selectedBook = (Books) catalogTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            int kol = selectedBook.getNalichie();
            int ID = selectedBook.getId() - 1;
            if (kol > 0) {
                boolean f = true;
                String line = "";
                InputStream orders_stream = getClass().getResourceAsStream("orders.txt");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(orders_stream, "utf-8"))) {
                    while (((line = reader.readLine()) != null) & (f == true)) {
                        String[] s = line.split(" ");
                        if (s[1].equals(User.login) & s[2].equals("должник")) {
                            f = false;
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Предупреждение");
                            alert.setHeaderText("Прошел срок сдачи книги");
                            String k = Books.NameById(s[0]);
                            alert.setContentText("Сначала сдайте книгу: " + k);
                            alert.showAndWait();
                        }
                    }
                    if (f == true) {
                        selectedBook.setNalichie(kol - 1);
                        productData1.set(ID, selectedBook);
                        catalogTable.setItems(productData1);

                        ObservableList<Books> mas = FXCollections.observableArrayList();
                        mas = catalogTable.getItems();
                        Books.rewrite(mas);
                        try (PrintWriter writer = new PrintWriter(new File(Books.class.getResource("orders.txt").getPath()))) {
                            writer.write("\r\n" + selectedBook.getId() + " " + User.login + " " + "ок");
                            writer.flush();
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Информация");
                        alert.setHeaderText("Ваша заказ принят");
                        alert.setContentText("Вы можете забрать данную книгу");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText("К сожалению, данной книги нет в наличии");
                alert.setContentText("Вы можете выбрать другую книгу");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setContentText("Выберите книгу, чтобы заказать");
            alert.setHeaderText("Ничего не выбрано");
            alert.showAndWait();
        }
    }

    public void Hyperlink() {
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
            productData1.addAll(mas);

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            avtor.setCellValueFactory(new PropertyValueFactory<>("avtor"));
            nazvanie.setCellValueFactory(new PropertyValueFactory<>("nazvanie"));
            nalichie.setCellValueFactory(new PropertyValueFactory<>("nalichie"));
            catalogTable.setItems(productData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
