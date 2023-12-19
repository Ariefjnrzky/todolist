package todolist;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HomeController {   
    
    // declare menu
    @FXML
    private ImageView menuJadwal;
    @FXML
    private ImageView menuTugas;
    @FXML
    private ImageView menuInformasi;
    @FXML
    private ImageView menuAboutme;
    // declare button
    @FXML
    private ImageView buttonLogout;
    // variabel untuk konfirmasi popup
    private boolean continueProcess;
    //data User
    private String username;
    private String password;
    private String email;
    private String namaLengkap;
    
    // memberikan nilai pada data User
    public void setUserData(String Username, String Password, String Email, String NamaLengkap){
        username = Username;
        password = Password;
        email = Email;
        namaLengkap = NamaLengkap;
    }
    
    // fungsi berpindah ke halaman jadwal user
    @FXML
    private void openJadwal(){
        System.out.println("Jadwal Open");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Jadwal.fxml"));
            Parent root = loader.load();
            // mengirimkan customer data ke JadwalController
            JadwalController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the Home window
            Stage homeStage = (Stage) menuJadwal.getScene().getWindow();
            homeStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // fungsi berpindah ke halaman tugas user
    @FXML
    private void openTugas(){
        System.out.println("Tugas Open");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tugas.fxml"));
            Parent root = loader.load();
            // mengirimkan customer data ke TugasController
            TugasController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the Home window
            Stage homeStage = (Stage) menuTugas.getScene().getWindow();
            homeStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
    // fungsi berpindah ke halaman informasi user
    @FXML
    private void openInformasi(){
        System.out.println("Informasi Open");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Informasi.fxml")); // Memanggil halaman informasi
            Parent root = loader.load();
            // mengirimkan customer data ke InformasiController
            InformasiController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the Home window
            Stage homeStage = (Stage) menuInformasi.getScene().getWindow();
            homeStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
    // fungsi berpindah ke halaman about me user
    @FXML
    private void openAboutMe(){
        System.out.println("About Me Open");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutMe.fxml")); // memanggil halaman About Me
            Parent root = loader.load();
            // mengirimkan customer data ke AboutMeController
            AboutMeController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the Home window
            Stage homeStage = (Stage) menuAboutme.getScene().getWindow();
            homeStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
    // fungsi berpindah ke halaman login user
    @FXML
    private void logout(){
        showAlertWarning("Warning", "Apakah yakin untuk logout?");
        if (continueProcess) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));  // memanggil view login bagi user
                Parent root = loader.load();
                // Create a new stage and set the scene
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                // Close the Home window
                Stage homeStage = (Stage) buttonLogout.getScene().getWindow();
                homeStage.close();
                // Show the new stage
                stage.show();
            } catch (IOException e) {
            }
        }
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
