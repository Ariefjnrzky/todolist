package todolist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class TugasAddController {
    
    // declare field
    @FXML
    private DatePicker fieldTanggal;
    @FXML
    private TextField fieldDeadline;
    @FXML
    private TextField fieldTugas;
    // decalare button
    @FXML
    private ImageView buttonBack;
    @FXML
    private Button buttonAdd;
    //data User
    private String username;
    private String password;
    private String email;
    private String namaLengkap;
    // variable untuk pause alert warning
    private boolean continueProcess;
    
    // memberikan nilai pada data User
    void setUserData(String Username, String Password, String Email, String NamaLengkap){
        username = Username;
        password = Password;
        email = Email;
        namaLengkap = NamaLengkap;
    }
    
    // fungsi untuk menambah data tugas
    @FXML void add(){
        // mengambil nilai dari input user
        LocalDate tanggal = fieldTanggal.getValue();
        String deadline = fieldDeadline.getText();
        String tugas = fieldTugas.getText();
        // memriksa apakah semau inputan ada
        if (deadline.length() != 0 && deadline.length() <= 5 && tugas.length() != 0 && tugas.length() <= 255 && tanggal != null){ //seluruh field terisi
            showAlertWarning("Warning", "Apakah Tugas sudah benar?"); //alert validation saat menambahkan warning
            if (continueProcess) { // jika menekan tomobol ok di alert
                boolean succesStore = storeTugas(tanggal,deadline,tugas); // menambahkan data tugas
                if (succesStore) {//Penambahan Tugas data jadwal berhasil
                    showAlert("Penambahan Tugas Sucess", "Jadwal berhasil disimpan.");
                } else { // Penambahan Tugas gagal
                    showAlert("Penambahan Tugas Failed", "Silahkan ulangi.");
                }
            }
        }else{ // terdapat input field yang kosong
            showAlert("Penambahan Tugas Failed", "Silahkan ulangi.");
        }
        // mengembalikan default value
        fieldDeadline.setText("");
        fieldTugas.setText("");
        
    }
    
    // menyimpan data ke table tugas
    private boolean storeTugas(LocalDate tanggal, String deadline, String tugas){
        boolean isStored = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "INSERT INTO tugas (Date, Deadline, Tugas, Username) VALUES (?, ?, ?, ?)"; // query insert data
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                java.sql.Date date = java.sql.Date.valueOf(tanggal);
                preparedStatement.setDate(1, date);
                preparedStatement.setString(2, deadline);
                preparedStatement.setString(3, tugas);
                preparedStatement.setString(4, username);
                
                int rowsAffected = preparedStatement.executeUpdate();
                isStored = rowsAffected > 0; // bernilai true ketika Data tersimpan
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
        }
        return isStored;
    }
    
    // fungsi berpindah ke halaman tugas user
    @FXML
    private void openTugas(){
     try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tugas.fxml"));
            Parent root = loader.load();
            // mengirimkan customer data ke TugasController
            TugasController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the tugasAdd customer window
            Stage tugasAddStage = (Stage) buttonBack.getScene().getWindow();
            tugasAddStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
    // fungsi menampilkan alert warning
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
