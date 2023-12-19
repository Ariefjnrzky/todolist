package todolist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AboutMeController { 
    // decalare button
    @FXML
    private ImageView buttonBack;
    @FXML
    private Button buttonDelete;
    // declare input field
    @FXML
    private TextField fieldUsername;
    @FXML
    private TextField fieldNamaLengkap;
    @FXML
    private TextField fieldEmail;
    // variabel untuk konfirmasi popup
    private boolean continueProcess;
    //data User
    private String username;
    private String password;
    private String email;
    private String namaLengkap;
    
    // memberikan nilai pada data User
    void setUserData(String Username, String Password, String Email, String NamaLengkap){
        username = Username;
        password = Password;
        email = Email;
        namaLengkap = NamaLengkap;
        // memasukkan nilai kedalam field
        fieldUsername.setText(Username);
        fieldEmail.setText(Email);
        fieldNamaLengkap.setText(NamaLengkap);
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
            // Close the aboutMe window
            Stage aboutMeStage = (Stage) buttonBack.getScene().getWindow();
            aboutMeStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
    // fungsi untuk menghapus akun
    @FXML
    private void delete(){
        showAlertWarning("Danger", "Apakah yakin untuk menghapus akun?. Akun yang telah dihapus tidak dapat dipulihkan.");
        if (continueProcess) {
            Boolean isDelete = deleteDataUser();
            if (isDelete){
                showAlert("Delete Akun berhasil", "Data akun berhasil terhapus.");
                openLogin(); // memanggil fungsi openLogin untuk keluar
            } else {
                showAlert("Delete Akun Failed", "Silahkan coba kembali.");
            }
        }
    }
    
    // berpindah ke halaman login
    private void openLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));  // memanggil view login bagi user
            Parent root = loader.load();
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the aboutMe window
            Stage aboutMeStage = (Stage) buttonDelete.getScene().getWindow();
            aboutMeStage.close();
            // Show the new stage
            stage.show();
            } catch (IOException e) {
        }
    }
    
    //    method untuk menghapus Data
    private boolean deleteDataUser(){
        boolean isDeleted = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "DELETE FROM users WHERE username = ?"; // query
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                int rowsAffected = preparedStatement.executeUpdate();
                isDeleted = rowsAffected > 0; //Data deleted successfully, if isDeleted true
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
        }
        return isDeleted;
    }
    
    // fungsi untuk alert warning
    private void showAlertWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(result -> {
            continueProcess = !result.getButtonData().isCancelButton(); // User clicked the close button (X)
        });
    }
    
    // fungsi untuk alert information
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
