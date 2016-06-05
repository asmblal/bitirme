package facedetectionandmatch;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.controlsfx.dialog.Dialogs;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Asım Bilal
 */
public class REsimTaraController implements Initializable {

    File selectedDirectory, file;
    FileChooser fileChooser = new FileChooser();
    CascadeClassifier faceDetector;
    DirectoryChooser directoryChooser = new DirectoryChooser();
    facerecognition.eigenfaces.DetectAndDisplay classfinder ;
    BufferedImage images;
   
    @FXML
    private ImageView taranacakresim, taratilanresim;
    
    
      @FXML
    private void returnhome(ActionEvent event) throws IOException {
       Parent root;
       root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
       Stage stage = new Stage();
       stage.setTitle("Resim Bul Kaydet");
      Scene scene= new Scene(root, 498, 537);
      
      	scene.getStylesheets().add(getClass().getResource("img/asd.css").toExternalForm());
       stage.setScene(scene);
       stage.setMaxHeight(537);
       stage.setMinHeight(498);
       stage.setMaxWidth(537);
       stage.setMinWidth(498);
       stage.show();
       ((Node)(event.getSource())).getScene().getWindow().hide();
        
        
    }

    @FXML
    private void TaranacakResim(ActionEvent event) throws IOException {

        fileChooser.setTitle("Resim Seçiniz");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        file = fileChooser.showOpenDialog(null);

        if (file != null) {

            try {
                images = ImageIO.read(file);
                Image image = new Image(file.toURI().toString());
                taranacakresim.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(resimyuzcikarConroller.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @FXML
    private void TaratilanResim(ActionEvent event) throws IOException {

        selectedDirectory = directoryChooser.showDialog(null);

    }

    @FXML
    private void tara(ActionEvent event) throws IOException {
        if (file != null && selectedDirectory != null) {
            System.out.println(file.getAbsolutePath());
            Mat image;
            classfinder = new facerecognition.eigenfaces.DetectAndDisplay();
            classfinder.CascadeClassbulucu("C:/opencv/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
            byte[] data = ((DataBufferByte) images.getRaster().getDataBuffer()).getData();
            if (images.getType() == 5) {
                image = new Mat(images.getHeight(), images.getWidth(), CvType.CV_8UC3);

                image.put(0, 0, data);
                // Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
            } else {

                image = new Mat(images.getHeight(), images.getWidth(), CvType.CV_8UC1);
                image.put(0, 0, data);

            }
            String path=selectedDirectory.getAbsolutePath();
            if (classfinder.detectedimage(image,path ,1)) {

                System.out.println("eşleşti");
                List<Image> eslesenresimler = classfinder.list(2);
                taratilanresim.setImage(eslesenresimler.get(0));
              
            } else {
                Dialogs.create()
                    .title("Bidirim")
                    .message("Eşleşen Resim Bulunamadı")
                    .showInformation();
                System.out.println("eşleşmedi");
            }
            Image resim = classfinder.mat2Image(image);
            taranacakresim.setImage(resim);

        } else {
            Dialogs.create()
                    .title("Uyarı")
                    .message("Lütfen Bir Resim Veya Klasör Seçiniz!")
                    .showInformation();

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
