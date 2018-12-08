package biblioteka;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class FXMLDocumentController   {
    
    
    @FXML
    private TextField textField1;
    @FXML
    private PasswordField textField2;
    @FXML
    private Button button;
    
    
    @FXML
    private void handleButtonAction() throws IOException {// метод, который срабатывает при нажатии кнопки "Войти" в первом окне приложения(он открывает новое окно в зависимости от того, кто авторизуется
         User user= new User(textField1.getText(), textField2.getText());
         Boolean f= user.enter();
         if (f) {
                boolean chek= user.whois();
                if (chek==true) {
                Stage stage= new Stage();
                    Parent root= FXMLLoader.load(getClass().getResource("Search.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    button.getScene().getWindow().hide();}
                else {
                    Stage stage= new Stage();
                    Parent root= FXMLLoader.load(getClass().getResource("HomeBibliotekar.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    button.getScene().getWindow().hide();
                    }
                }
         else{
             Alert alert= new Alert(AlertType.ERROR);
             alert.setTitle("Ошибка");
             alert.setContentText("Проверьте введеные данные");
             alert.setHeaderText("Такого пользователя не существует");
             alert.showAndWait();
         }
             
}
    
    
    public void initialize(){
        textField2.setOnKeyPressed(event-> {
            if (event.getCode().equals(KeyCode.ENTER)){
                button.fire();
            }
        });
        
       
    } 
    public void close(){
   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
   alert.setTitle("Выход");
   alert.setHeaderText("Вы собираетесь выйти из приложения");
   alert.setContentText("Вы уверены?");
   Optional<ButtonType> result = alert.showAndWait();
   if (result.get() == ButtonType.OK) {
   Platform.exit();
   }
   }
    
}
