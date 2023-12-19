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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InformasiController {   
    // decalare button
    @FXML
    private ImageView buttonBack;
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
            // Close the informasi window
            Stage informasiStage = (Stage) buttonBack.getScene().getWindow();
            informasiStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
    
    
}
