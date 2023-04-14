package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignUpController {


    String complexPasswordRegex =  "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=._-])(?=\\S+$).{8,16}$";
    Pattern passwordPattern = Pattern.compile(complexPasswordRegex);
    String complexUsername = "^[.a-zA-Z0-9_-]{2,20}$";
    Pattern usernamePattern = Pattern.compile(complexUsername);

    public static Map<String, String> userAccount = new HashMap<>();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Button registrateButton;

    @FXML
    private TextField signUpField;

    @FXML
    private AnchorPane signUpFrame;

    @FXML
    private Label signUpLabel;

    @FXML
    void initialize() {
        //Вернуться обратно к логинскрину
        backButton.setOnAction(event -> {
            backButton.getScene().getWindow().hide();
        });

        //Кнопка регистрации
        registrateButton.setOnAction(event -> {
            String username = signUpField.getText();
            String password = newPasswordField.getText();

            //Считываем и запоминаем все данные файла logins.txt
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

            //Считываем и запоминаем все данные файла passwords.txt
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
                userAccount.put(loginLines.get(i), passwordLines.get(i));
            }

            //Если логин не соответствует регулярному выражению
            if(!usernamePattern.matcher(username).matches()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information dialog");
                alert.setHeaderText("wrong login u know?");
                alert.setContentText("Wrong login format, bro");

                alert.showAndWait();
            }

            //Если пароль не соответствует регулярному выражению
            if(!passwordPattern.matcher(password).matches()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information dialog");
                alert.setHeaderText("Wrong password i guess...");
                alert.setContentText("Wrong format, Jeremy[cries]");

                alert.showAndWait();
            }

            //Если логин уже присутствует в базе
            if(userAccount.containsKey(username)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information dialog");
                alert.setHeaderText("wrong login u know?");
                alert.setContentText("This login is already in DB");

                alert.showAndWait();
            }

            //Условие успешной регистрации
            if(!userAccount.containsKey(username) && passwordPattern.matcher(password).matches() &&
                    usernamePattern.matcher(username).matches()) {
                userAccount.put(username, password);

                //Записываем данные регистрации в файлы для логинов и паролей.
                try {
                    FileWriter loginWriter = new FileWriter("logins.txt", true);
                    FileWriter passwordWriter = new FileWriter("passwords.txt", true);

                    loginWriter.write(" " + username);
                    passwordWriter.write(" " + password);

                    loginWriter.close();
                    passwordWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information dialog");
                alert.setHeaderText("Yare yare daze");
                alert.setContentText("Now you are a part of the Groove street, CJ");
                alert.showAndWait();

                registrateButton.getScene().getWindow().hide();
                /*FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("login.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();*/
            }
        });
    }
}
