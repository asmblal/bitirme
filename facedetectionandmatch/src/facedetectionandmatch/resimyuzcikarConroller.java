
package facedetectionandmatch;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.controlsfx.dialog.Dialogs;
import org.opencv.imgcodecs.Imgcodecs;

/*
 * @author Asım Bilal
 */
public class resimyuzcikarConroller implements Initializable {

    FileChooser fileChooser = new FileChooser();
    File file, savefile, selectedDirectory;
    facerecognition.eigenfaces.DetectAndDisplay classfinder = new facerecognition.eigenfaces.DetectAndDisplay();
    BufferedImage images;
    DirectoryChooser directoryChooser = new DirectoryChooser();
    boolean tarandı = false;

    @FXML
    private ImageView resimgoster;
    @FXML
    private Button kaydet;

    @FXML
    private void resmibul(ActionEvent event) throws IOException {

        fileChooser.setTitle("Rseim Seçiniz");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("video Files", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                images = ImageIO.read(file);

                Image image = SwingFXUtils.toFXImage(images, null);

                resimgoster.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(resimyuzcikarConroller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 Mat image;
    @FXML
    private void yuztara(ActionEvent event) throws IOException {
        if (file != null) {
            System.out.println(file.getAbsolutePath());
           
            try {
                image = img2Mat(images);
                classfinder.CascadeClassbulucu("resources/haarcascades/haarcascade_frontalface_alt.xml");

                classfinder.detected(image);
                resimgoster.setImage(classfinder.mat2Image(image));

                tarandı = true;

            } catch (Exception e) {
                Dialogs.create()
                        .title("Uyarı")
                        .message("Resim taratılamıyor")
                        .showInformation();
            }

        } else {
            Dialogs.create()
                    .title("Uyarı")
                    .message("Lütfen Bir Resim Seçiniz!")
                    .showInformation();

        }

    }

    @FXML
    private void kaydet(ActionEvent event) throws IOException {
        if (file != null && tarandı) {
             List<Mat> eslesenresimler = classfinder.listbulunan();
            if(!eslesenresimler.isEmpty() ){
                       for (int i = 0; i < eslesenresimler.size(); i++) {

                       Imgcodecs.imwrite("C:\\aa\\resim" + i + ".png", eslesenresimler.get(i));

                      }
                      
                    File file1 = new File ("C:\\aa\\");
                 Desktop desktop = Desktop.getDesktop();
                   desktop.open(file1);
                     
                    
                 

            
            
        }
            else {
                    Dialogs.create()
                            .title("Uyarı")
                            .message("Kaydedilecek yüz bulunamadı!")
                            .showInformation();

                }
        } else {
            Dialogs.create()
                    .title("Uyarı")
                    .message("Lütfen Önce bir resim seçip taratınız!")
                    .showInformation();
        }

    }

    public static Mat img2Mat(BufferedImage image) {
        Mat mat = null;

        if (image.getType() == 5) {
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            mat.put(0, 0, data);
        } else {

            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
            mat.put(0, 0, data);

        }
        return mat;
    }

    @FXML
    private void returnhome(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Resim Bul Kaydet");
        Scene scene = new Scene(root, 498, 537);

        scene.getStylesheets().add(getClass().getResource("img/asd.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaxHeight(537);
        stage.setMinHeight(498);
        stage.setMaxWidth(537);
        stage.setMinWidth(498);
        stage.show();
        ((Node) (event.getSource())).getScene().getWindow().hide();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}


