/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facedetectionandmatch;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Asım Bilal
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    private Button resimtara;
   
    
    @FXML
    private void resimtaraekranınıAc(ActionEvent event) throws IOException {
       
       Parent root;
       root = FXMLLoader.load(getClass().getResource("ResimTarama.fxml"));
       Stage stage = new Stage();
       stage.setTitle("Resim Tara");
         Scene scene= new Scene(root, 600, 552);
       scene.getStylesheets().add(getClass().getResource("img/asd.css").toExternalForm());
       stage.setScene(scene);
       stage.show();
       ((Node)(event.getSource())).getScene().getWindow().hide();
      
    }
      @FXML
    private void resimyuzcikarac(ActionEvent event) throws IOException {
       
       Parent root;
       root = FXMLLoader.load(getClass().getResource("resimyuzcr.fxml"));
       Stage stage = new Stage();
       stage.setTitle("Resim Bul Kaydet");
      Scene scene= new Scene(root, 736, 600);
      
      	scene.getStylesheets().add(getClass().getResource("img/asd.css").toExternalForm());
       stage.setScene(scene);
       stage.setMaxHeight(600);
       stage.setMinHeight(600);
       stage.setMaxWidth(736);
       stage.setMinWidth(736);
       stage.getIcons().add(new Image(getClass().getResource("img/bul.png").toExternalForm()));
       stage.show();
       ((Node)(event.getSource())).getScene().getWindow().hide();
      
    }
   
    @FXML
    private void videotaraekranınıAc(ActionEvent event) throws IOException {
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
       Parent root;
       root = FXMLLoader.load(getClass().getResource("videoTara.fxml"));
       Stage stage = new Stage();
       stage.setTitle("Video Tara");
       
    Scene scene=new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
    scene.getStylesheets().add(getClass().getResource("img/asd.css").toExternalForm());
       stage.setScene(scene);
       stage.show();
       ((Node)(event.getSource())).getScene().getWindow().hide();
       
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
       
       
    }
        @FXML
    private void videoinject(ActionEvent event) throws IOException {
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
       Parent root;
       root = FXMLLoader.load(getClass().getResource("videoinject.fxml"));
       Stage stage = new Stage();
       stage.setTitle("Video inject");
       
    Scene scene=new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
    scene.getStylesheets().add(getClass().getResource("img/asd.css").toExternalForm());
       stage.setScene(scene);
       stage.show();
       ((Node)(event.getSource())).getScene().getWindow().hide();
       
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
       
       
    }
     @FXML
    private void realtime(ActionEvent event) throws IOException {
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
       Parent root;
       root = FXMLLoader.load(getClass().getResource("realtime.fxml"));
       Stage stage = new Stage();
       stage.setTitle("Video Tara");
       
    Scene scene=new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
    scene.getStylesheets().add(getClass().getResource("img/asd.css").toExternalForm());
       stage.setScene(scene);
       stage.show();
       ((Node)(event.getSource())).getScene().getWindow().hide();
       

        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
       
       
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
