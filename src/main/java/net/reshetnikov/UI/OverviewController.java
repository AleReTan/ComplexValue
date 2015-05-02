package net.reshetnikov.UI;

        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.ComboBox;
        import javafx.scene.control.RadioButton;
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
        }
    }
    public void closeApp(){
        Main.getPrimaryStage().close();
    }

    public void calculate(){

        dataProcessing.testMethod();

    }
}
