package todolist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import todolist.HomeController;

public class LoginController {
    // declare input field
    @FXML
    private TextField fieldUsername;
    @FXML
    private TextField fieldPassword;
    // decalare button
    @FXML
    private Button buttonLogin;
    @FXML
    private Text buttonDaftar;
    //data User
    private String username;
    private String password;
    private String email;
    private String namaLengkap;
    
    // fungsi login user
    @FXML
    private void login(){
        username = fieldUsername.getText();
        password = fieldPassword.getText();
        // memeriksa apakah seluruh inputan terisi
        if (username.length() != 0 && password.length() != 0){ // seluruh inputan terisi
            boolean isUser = authenticateUser(); // memanggil fungsi authenticateCustomer untuk memeriksa apakah email dan password user benar atau tidak
            if (isUser){
                System.out.println(username + " # " + password );
                openHomeUser(); // memanggil fungsi openHomeUser untuk membuka halaman Home
            } else {
                showAlert("Login Gagal", "Email atau Password salah.");
            }
        } else { // terdapat inputan yang kosong
            showAlert("Login Gagal", "Isi seluruh masukkan.");
        }
    }
    
    // fungsi untuk memeriksakan username dan password user ke database
    private boolean authenticateUser(){
        boolean successLogin = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "SELECT Username, Password, Email, NamaLengkap FROM users WHERE Username = ? AND Password = ?"; // query mencari username dan password yang sama
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username); // memasukkan variable username ke dalam string query
                preparedStatement.setString(2, password); // memasukkan variable password ke dalam string query

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        successLogin = true; // menemukan usern yang sama
                        // memasukkan hasil wuery kedalam variable local
                        username = resultSet.getString("Username");
                        password = resultSet.getString("Password");
                        email = resultSet.getString("Email");
                        namaLengkap = resultSet.getString("NamaLengkap");
                    }
                    resultSet.close();
                }
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
        }
        return successLogin;
    }
    
    // fungsi berpindah ke halaman home user
    @FXML
    private void openHomeUser(){
     try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml")); // memanggil halaman home
            Parent root = loader.load();
            // mengirimkan customer data ke LoginCustomerController
            HomeController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the login window
            Stage loginStage = (Stage) buttonLogin.getScene().getWindow();
            loginStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
    // fungsi berpindah ke halaman daftar customer
    @FXML
    private void openDaftar(){
     try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml")); // memanggil halaman register
            Parent root = loader.load();
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the login customer window
            Stage loginStage = (Stage) buttonDaftar.getScene().getWindow();
            loginStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
    // fungsi untuk menampilkan alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
