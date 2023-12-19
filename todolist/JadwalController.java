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
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JadwalController {  
    // decalare button
    @FXML
    private ImageView buttonBack;
    @FXML
    private ImageView buttonAdd;
    // declare layout card harian
    @FXML
    private VBox layoutTaskSenin;
    @FXML
    private VBox layoutTaskSelasa;
    @FXML
    private VBox layoutTaskRabu;
    @FXML
    private VBox layoutTaskKamis;
    @FXML
    private VBox layoutTaskJumat;
    @FXML
    private VBox layoutTaskSabtu;
    @FXML
    private VBox layoutTaskMinggu;
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
        
        loadDataJadwal(); // memanggil fungsi loadDataJadwal
    }
    
    // fungsi untuk mengambil data jadwal di database
    private void loadDataJadwal(){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "SELECT IDJadwal, Hari, Waktu, Kegiatan, Status FROM jadwal WHERE Username = '" + username + "' ORDER BY Waktu ASC"; // query mengambil data jadwal
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) { // perulanagan untuk mengambil seluruh baris hasil query
                        Integer id_jadwal = resultSet.getInt("IDJadwal");
                        String hari = resultSet.getString("Hari");
                        String kegiatan = resultSet.getString("Kegiatan") + " (" + resultSet.getString("Waktu") + ")";
                        Boolean status = resultSet.getBoolean("Status");
                        addTask(id_jadwal, hari, kegiatan, status); //menambahkan task kedalam card hari
                    }
                }
            }
        } catch (SQLException e) {
        }
    }
    
    // untuk menambahkan task kedalam card
    private void addTask(Integer IDJadwal, String hari, String kegiatan, Boolean status) {
        // declare element
        HBox layoutTask = new HBox();
        Text textTask = new Text(kegiatan);
        CheckBox checkBox = new CheckBox();
        // setup style text jadwal
        textTask.setWrappingWidth(320);
        textTask.setStyle("-fx-font-size: 16px;");
        // setup checkbox
        checkBox.setSelected(status);
        // membuat triger ketika diklik, akan menjalankan fungsi editJadwal
        checkBox.setOnAction(e -> editJadwal(IDJadwal, checkBox.isSelected())); 
        // menyatukan text jadwal dan checkbox
        layoutTask.getChildren().addAll(textTask, checkBox);
        // menambahkan task kedalam card yang sesuai
        switch (hari) {
            case "Senin" -> layoutTaskSenin.getChildren().add(layoutTask);
            case "Selasa" -> layoutTaskSelasa.getChildren().add(layoutTask);
            case "Rabu" -> layoutTaskRabu.getChildren().add(layoutTask);
            case "Kamis" -> layoutTaskKamis.getChildren().add(layoutTask);
            case "Jumat" -> layoutTaskJumat.getChildren().add(layoutTask);
            case "Sabtu" -> layoutTaskSabtu.getChildren().add(layoutTask);
            case "Minggu" -> layoutTaskMinggu.getChildren().add(layoutTask);
            default -> {
            }
        }
    }
    
    // fungsi untuk merubah status task ketika checkbox diklik
    private void editJadwal(Integer IDJadwal, Boolean Status){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "UPDATE jadwal SET Status = ? WHERE IDJadwal = ?"; // query edit data
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, Status);
                preparedStatement.setInt(2, IDJadwal);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
        }
    }
    
    // fungsi berpindah ke halaman add jadwal
    @FXML
    private void openAdd(){
     try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JadwalAdd.fxml")); // memanggil halaman jadwalAdd
            Parent root = loader.load();
            // mengirimkan customer data ke JadwalAddController
            JadwalAddController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the jadwal window
            Stage jadwalStage = (Stage) buttonAdd.getScene().getWindow();
            jadwalStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
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
            // Close the jadwal window
            Stage jadwalStage = (Stage) buttonBack.getScene().getWindow();
            jadwalStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
    
}
