package todolist;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import todolist.HomeController;

public class TugasController {
    
    // decalare button
    @FXML
    private ImageView buttonBack;
    @FXML
    private ImageView buttonAdd;
    // declare layout card
    @FXML
    private VBox layoutCard;
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
        
        loadDataTugas(); // memanggil fungsi loadDataTugas
    }
    
    // fungsi untuk mengambil data Tugas di database
    private void loadDataTugas(){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "SELECT Date FROM tugas WHERE Username = '" + username + "' GROUP BY Date Order BY Date ASC"; // query mengambil data jadwal
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) { // perulanagan untuk mengambil seluruh baris hasil query
                        Date date = resultSet.getDate("Date");
                        addCard(date); // memanggil fungsi addCard untuk menambahkan card
                    }
                }
            }
        } catch (SQLException e) {
        }
    }
    
    // fungsi menambahkan card tugas
    private void addCard(Date date){
        // declare element
        Text title = new Text();
        VBox taskCard = new VBox();
        // parsing date menjadi format seperti "01 January 2023"
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String Title = (date != null) ? dateFormat.format(date) : "";
        //set title dari nilai date
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        title.setText(Title);
        // set style
        VBox.setMargin(taskCard, new Insets(0, 12, 0, 12));
        taskCard.setStyle("-fx-background-color: #C6F0E2; -fx-padding: 12px; -fx-spacing: 12; -fx-background-radius: 4");
        // menggabungkan title dengan card
        taskCard.getChildren().add(title);
        // memanggil fungsi addTasks untuk menambahkan task-task yang memiliki tanggal yang ditentukan
        addTasks(taskCard, date);
        //meanmbahkan card yang telah dibentuk kedalam layout utama
        layoutCard.getChildren().add(taskCard);
    }
    
    // fungsi menambahkan tugas-tugas yang memiliki hari yang dipilih
    private void addTasks(VBox taskCard, Date date){
        //declare element
        VBox layoutTasks = new VBox();
        // set style
        layoutTasks.setStyle("-fx-spacing: 8;");
        //memanggil fungsi loadDataTasks untuk menambahkan tugas-tugas ke dalam layout
        loadDataTasks(layoutTasks, date);
        //menambahkan tugas-tugas terpilih kedalam card sesuai tanggalnya
        taskCard.getChildren().add(layoutTasks);
    }
    
    // fungsi untuk untuk menambahkan tugas-tugas ke dalam layout
    private void loadDataTasks(VBox layoutTasks, Date date){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "SELECT IDTugas, Deadline, Tugas, Status FROM tugas WHERE Username = ? AND Date = ? ORDER BY Date ASC"; // query mengambil data tugas
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setDate(2, new java.sql.Date(date.getTime()));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) { 
                        // perulanagan untuk mengambil seluruh baris hasil query
                        Integer id_tugas = resultSet.getInt("IDTugas");
                        String deadline = resultSet.getString("Deadline");
                        String tugas = resultSet.getString("Tugas") + " (" + deadline + ")";
                        Boolean status = resultSet.getBoolean("Status");
                        //menambahkan task satu persatu
                        addTask(layoutTasks, id_tugas, tugas, status);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // untuk menambahkan task kedalam layout satu per satu
    private void addTask(VBox layoutTasks, Integer IDTugas, String Tugas, Boolean Status) {
        // declare element
        HBox layoutTask = new HBox();
        Text textTask = new Text(Tugas);
        CheckBox checkBox = new CheckBox();
        // set style text
        textTask.setWrappingWidth(320);
        textTask.setStyle("-fx-font-size: 16px;");
        // set default status bedasarkan valus yang ada di database
        checkBox.setSelected(Status);
        // membuat triger ketika checkbox diklik akan mengedit nilai dari status
        checkBox.setOnAction(e -> editTugas(IDTugas, checkBox.isSelected()));
        // menyatukan text dan checkbox
        layoutTask.getChildren().addAll(textTask, checkBox);
        // menyatukan layout satuan kedalam layout kumpulan tugas
        layoutTasks.getChildren().add(layoutTask);
    }
    
    // fungsi untuk merubah status task ketika checkbox diklik
    private void editTugas(Integer IDTugas, Boolean Status){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "")) {
            String query = "UPDATE tugas SET Status = ? WHERE IDTugas = ?"; // query insert data
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, Status);
                preparedStatement.setInt(2, IDTugas);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // fungsi berpindah ke halaman add jadwal
    @FXML
    private void openAdd(){
     try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TugasAdd.fxml")); // memanggil halaman tambah tugas
            Parent root = loader.load();
            // mengirimkan customer data ke TugasAddController
            TugasAddController user = loader.getController();
            user.setUserData(username, password, email, namaLengkap);
            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Close the Tugas customer window
            Stage tugasStage = (Stage) buttonAdd.getScene().getWindow();
            tugasStage.close();
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
            // Close the Tugas window
            Stage tugasStage = (Stage) buttonBack.getScene().getWindow();
            tugasStage.close();
            // Show the new stage
            stage.show();
        } catch (IOException e) {
        }
    }
}