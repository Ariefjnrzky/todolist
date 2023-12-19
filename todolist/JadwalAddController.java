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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class JadwalAddController {
    // declare field
    @FXML
    private ComboBox fieldHari;
    @FXML
    private TextField fieldWaktu;
    @FXML
    private TextField fieldKegiatan;
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
    
    // fungsi inisiasi untuk dropdown hari
    @FXML
    private void initialize() {
        fieldHari.getItems().removeAll(fieldHari.getItems());
        fieldHari.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu");
        fieldHari.getSelectionModel().select("Senin");
    }
    
    // fungsi untuk menambah data jadwal
    @FXML void add(){
        // mengambil nilai dari input user
        Object combo = fieldHari.getValue();
        String hari = String.valueOf(combo);
        String waktu = fieldWaktu.getText();
        String kegiatan = fieldKegiatan.getText();
        // memriksa apakah semau inputan ada
        if (waktu.length() != 0 && waktu.length() <= 11 && kegiatan.length() != 0 && kegiatan.length() <= 255 && hari.length() != 0){ //seluruh field terisi
            showAlertWarning("Warning", "Apakah jadwal sudah benar?"); //alert validation saat menambahkan warning
            if (continueProcess) { // jika menekan tomobol ok di alert
                boolean succesStore = storeJadwal(hari,waktu,kegiatan); // menambahkan data jadwal
                if (succesStore) {//Penambahan Jadwal data jadwal berhasil
                    showAlert("Penambahan Jadwal Sucess", "Jadwal berhasil disimpan.");
                } else { // Penambahan Jadwal gagal
                    showAlert("Penambahan Jadwal Failed", "Silahkan ulangi.");
                }
            }
        }else{ // terdapat input field yang kosong
            showAlert("Penambahan Jadwal Failed", "Silahkan ulangi.");
        }
        // mengembalikan default value
        fieldHari.getSelectionModel().select("Senin");
        fieldWaktu.setText("");
        fieldKegiatan.setText("");
    }
    
    // menyimpan data ke table jadwal
    private boolean storeJadwal(String hari, String waktu, String kegiatan){
        boolean isStored = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "INSERT INTO jadwal (Hari, Waktu, Kegiatan, Username) VALUES (?, ?, ?, ?)"; // query insert data
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, hari);
                preparedStatement.setString(2, waktu);
                preparedStatement.setString(3, kegiatan);
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
    
    // fungsi berpindah ke halaman jadwal user
    @FXML
    private void openJadwal(){
     try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Jadwal.fxml"));
            Parent root = loader.load();
            // mengirimkan customer data ke JadwalController
            JadwalController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the jadwalAdd window
            Stage jadwalAddStage = (Stage) buttonBack.getScene().getWindow();
            jadwalAddStage.close();
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
