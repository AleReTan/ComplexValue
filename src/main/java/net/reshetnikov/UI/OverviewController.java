package net.reshetnikov.UI;

        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.layout.AnchorPane;
        import javafx.stage.FileChooser;
        import net.reshetnikov.Logic.DataProcessing;
        import net.reshetnikov.Main;
        import java.io.File;


public class OverviewController {

    @FXML
    private void initialize() {
        categoryComboBox.getItems().addAll("A","A,B","A,B,C");
        categoryComboBox.setValue("A");
        quantifierComboBox.getItems().addAll("x"+(char)178,"x"+(char)179);
        quantifierComboBox.setValue("x"+(char)178);
        quantifierComboBox.setDisable(true);
    }
    @FXML
    private Button openPointButton;
    @FXML
    private Button openZoneButton;
    @FXML
    private Button calculateButton;
    @FXML
    private TextField textField;
    @FXML
    private ComboBox categoryComboBox;
    @FXML
    private ComboBox quantifierComboBox;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private AnchorPane anchorPane;

    public DataProcessing dataProcessing = new DataProcessing();

    private boolean flagOpenPoints = false;
    private boolean flagOpenZones = false;

    public void refresh(){
        if (radioButton1.isSelected()) quantifierComboBox.setDisable(false);
        else quantifierComboBox.setDisable(true);
    }

    public void openFilePoint(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File("D:\\University\\Diploma\\ComplexValue\\src\\main\\resources"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
//        File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
        File file = fileChooser.showOpenDialog(anchorPane.getScene().getWindow());
        if (file != null) {
            dataProcessing.loadPoints(file);
            flagOpenPoints = true;
        }
    }
    public void openFileZone(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File("D:\\University\\Diploma\\ComplexValue\\src\\main\\resources"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
//        File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
        File file = fileChooser.showOpenDialog(anchorPane.getScene().getWindow());
        if (file != null) {
            dataProcessing.loadZone(file);
            flagOpenZones = true;
        }
    }
    public void closeApp(){
        Main.getPrimaryStage().close();
    }

    public void calculate(){
        if (!flagOpenPoints) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error dialog");
            alert.setHeaderText("Откройте файл с точками");
            alert.setContentText("Не был открыт файл с набором точек");
            alert.showAndWait();
        }
        if (!flagOpenZones) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error dialog");
            alert.setHeaderText("Откройте файл с зонами");
            alert.setContentText("Не был открыт файл с набором зон");
            alert.showAndWait();
        }
        if (!(radioButton1.isSelected()||radioButton2.isSelected())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error dialog");
            alert.setHeaderText("Необходимо выбрать способ");
            alert.setContentText("Не был выбран способ определения обобщенной оценки значимой зоны");
            alert.showAndWait();
        }
        if (textField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error dialog");
            alert.setHeaderText("Необходимо выбрать параметр alpha");
            alert.setContentText("Не был выбрано значение параметра alpha, оно должно принадлежать от 0 до 1");
            alert.showAndWait();
        }
        if(flagOpenZones && flagOpenPoints && (radioButton1.isSelected()||radioButton2.isSelected()) && !textField.getText().equals("")) {
            String method = "";
            if (radioButton1.isSelected()) method = "OWA";
            if (radioButton2.isSelected()) method = "middle";
            dataProcessing.calculateMethod(categoryComboBox.getValue().toString(), method, quantifierComboBox.getValue().toString(),textField.getText());
        }
    }
}
