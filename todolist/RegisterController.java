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

public class RegisterController {
    
    // declare input field
    @FXML
    private TextField fieldUsername;
    @FXML
    private TextField fieldPassword;
    @FXML
    private TextField fieldNamaLengkap;
    @FXML
    private TextField fieldEmail;
    // decalare button
    @FXML
    private Button buttonDaftar;
    @FXML
    private Text buttonMasuk;
    //data User
    private String username;
    private String password;
    private String email;
    private String namaLengkap;
    
    // fungsi registrasi customer
    @FXML
    private void daftar(){
        // mengambil nilai dari inputan
        username = fieldUsername.getText();
        password = fieldPassword.getText();
        email = fieldEmail.getText();
        namaLengkap = fieldNamaLengkap.getText();
        
        // memeriksa apakah seluruh inputan terisi
        if (username.length() != 0 && password.length() != 0 && email.length() != 0 && namaLengkap.length() != 0){ // seluruh inputan terisi
            boolean isExistUser = isExistUser(); // memanggil fungsi isExistUser untuk memeriksa apakah email user telah terdaftar atau tidak
            if (isExistUser == false){
                boolean successRegistUser = registUser();
                if (successRegistUser){
                    showAlert("Registrasi Berhasil", "Anda telah terdaftar, silahkan login.");
                    openLogin();
                }else{
                    showAlert("Registrasi Gagal", "Silahkan ulang kembali.");
                }
            } else {
                showAlert("Registrasi Gagal", "Email " + email + " telah terdaftar, silahkan menggunakan email lain.");
            }
        } else { // terdapat inputan yang kosong
            showAlert("Registrasi Gagal", "Isi seluruh masukkan.");
        }
    }
    
    // fungsi untuk memeriksa email user yang dimasukkan terdaftar atau tidak
    private boolean isExistUser(){
        boolean isExist = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "SELECT Email FROM users WHERE Email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email); // memasukkan variable email ke dalam string query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        isExist = true; // menemukan email customer yang sama
                    }
                    resultSet.close();
                }preparedStatement.close();
            }connection.close();
        } catch (SQLException e) {
        }return isExist;
    }
    
    // fungsi untuk menambahkan data user
    private boolean registUser(){
        boolean isStored = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "INSERT INTO users (Username, Password, Email, NamaLengkap) VALUES (?, ?, ?, ?)"; // query insert data
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, namaLengkap);
                int rowsAffected = preparedStatement.executeUpdate();
                isStored = rowsAffected > 0; //Data tersimpan ketika rowsAffected > 0 dan membuat nilai isStored->true!
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
        }
        return isStored;
    }
    
    // fungsi berpindah ke halaman daftar customer
    @FXML
    private void openLogin(){
     try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the register window
            Stage registerStage = (Stage) buttonMasuk.getScene().getWindow();
            registerStage.close();
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
