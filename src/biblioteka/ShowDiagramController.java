package biblioteka;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

public class ShowDiagramController implements Initializable {

    @FXML
    private BarChart barChart;
    @FXML
    private CategoryAxis xAxis;

    private ObservableList productNames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String line;
        ArrayList<String> arr = new ArrayList<>();

        try {
            InputStream books_stream = getClass().getResourceAsStream("books.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(books_stream, "utf-8"));

            while ((line = reader.readLine()) != null) {
                String[] s = line.split("&");
                arr.add(s[0]);
            }
        } catch (IOException ex) {
            Logger.getLogger(ShowDiagramController.class.getName()).log(Level.SEVERE, null, ex);

        }
        productNames.addAll(arr);
        xAxis.setCategories(productNames);
    }

    public void setProduct() throws FileNotFoundException, IOException {
        ArrayList<Integer> arr = new ArrayList<>();
        try {
            InputStream books_stream = getClass().getResourceAsStream("books.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(books_stream, "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                String s[] = line.split("&");
                arr.add(Integer.valueOf(s[3]));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ShowDiagramController.class.getName()).log(Level.SEVERE, null, ex);
        }
        XYChart.Series series = new XYChart.Series<>();
        for (int j = 0; j < arr.size(); j++) {
            series.getData().add(new XYChart.Data<>(productNames.get(j), arr.get(j)));
        }
        barChart.getData().add(series);

    }

}
