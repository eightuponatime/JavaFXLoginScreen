package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LogInController {

    public void makePfEditable() {
        new Timer().schedule(
                new TimerTask() {
                    public void run() {
                        passwordField.setEditable(true);
                        logInButton.setDisable(false);
                    }
                },
                10000 );
    }

    Map<String, Integer> loginFails = new HashMap<>();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button logInButton;

    @FXML
    private TextField loginField;

    @FXML
    private AnchorPane loginFrame;

    @FXML
    private Label loginLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    public static String username;

    @FXML
    void initialize() {

        //Нажатие кнопки регистрации, и переход к соответствующему окну
        signUpButton.setOnAction(event -> {
            //signUpButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("signup.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });

        logInButton.setOnAction(event -> {

            //Если коллекция логинов пустая, считываем соответствующие файлы
           if(SignUpController.userAccount.isEmpty()) {
               String fileName = "logins.txt";
               List<String> loginLines = new ArrayList<>();

               try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                   String line;
                   while ((line = br.readLine()) != null) {
                       String[] words = line.split(" ");
                       loginLines.addAll(Arrays.asList(words));
                   }
               } catch (IOException ex) {
                   ex.printStackTrace();
               }

               String nameFile = "passwords.txt";
               List<String> passwordLines = new ArrayList<>();
               try (BufferedReader br = new BufferedReader(new FileReader(nameFile))) {
                   String line;
                   while ((line = br.readLine()) != null) {
                       String[] words = line.split(" ");
                       passwordLines.addAll(Arrays.asList(words));
                   }
               } catch (IOException ex) {
                   ex.printStackTrace();
               }

               for(int i = 0; i < loginLines.size(); i++) {
                   SignUpController.userAccount.put(loginLines.get(i), passwordLines.get(i));
               }
           }


           String username = loginField.getText();
           String password = passwordField.getText();

            //Если пользователь еще ни разу не регистрировался
            if(!loginFails.containsKey(username)) {
                loginFails.put(username, 0);
            }

            //Если логина нет в БД
           if(!SignUpController.userAccount.containsKey(username)) {
               System.out.println("u re here");
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Information Dialog");
               alert.setHeaderText("Bro..Wrong Login");
               alert.setContentText("It's already in DB, don't waste my time");
               alert.showAndWait();
           }

           //Если логин верный, но пароль нет. Запускаем счетчик ошибок
           if(SignUpController.userAccount.containsKey(username) &&
                   !SignUpController.userAccount.containsValue(username)) {

               int failCounter = loginFails.get(username) + 1;
               loginFails.put(username, failCounter);


               if(loginFails.get(username) > 2) {
                    passwordField.setEditable(false);
                    logInButton.setDisable(true);
                    passwordField.setText("");
                    makePfEditable();
               }
           }
           if (SignUpController.userAccount.containsKey(username) &&
                   password.equals(SignUpController.userAccount.get(username))) {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Congratulation Message");
               alert.setHeaderText("You're finally in");
               alert.setContentText("Nothing is going to happen, it's just information message u know?");
               alert.showAndWait();

               loginFails.put(username, 0);
               //loginField.setEditable(false);
               //passwordField.setDisable(false);
           }
            if(SignUpController.userAccount.containsKey(username) &&
                    !password.equals(SignUpController.userAccount.get(username))) {
                if(loginFails.get(username) < 3) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("WRONG PASSWORD MAN");
                    int attemptsLeft = 3 - loginFails.get(username);
                    alert.setContentText("Attempts left: " + String.valueOf(attemptsLeft));
                    alert.showAndWait();
                }
                if(loginFails.get(username) >= 3) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Feels Bad Man");
                    alert.setContentText("You lost all you attempts, idiot");
                    alert.showAndWait();
                }
            }

        });



    }

}
