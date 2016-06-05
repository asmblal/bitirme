package facedetectionandmatch;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class videoinjectcontroller {

    FileChooser fileChooser = new FileChooser();
    File selectedDirectory, file;
    Thread thread;
    Image imageToShow;
    Mat eslesenresim;
    int count;
    List<Image> eslesenresimler = new ArrayList<>();
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive, bittimi = false;
    facerecognition.eigenfaces.DetectAndDisplay classfinder = new facerecognition.eigenfaces.DetectAndDisplay();
    DirectoryChooser directoryChooser = new DirectoryChooser();
    @FXML
    private ImageView eslesen1, videogoster, eslesen, eslesen2;
    @FXML
    private Label label, label2;
    @FXML
    private CheckBox haarClassifier;
    @FXML
    private CheckBox lbpClassifier;
    // FXML buttons
    @FXML
    private Button cameraButton;
    ScheduledExecutorService scheduledExecutorService;

    protected void init() {
        this.capture = new VideoCapture();
        //this.faceCascade = new CascadeClassifier();

    }
    
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
    private void videotarama(ActionEvent event) throws IOException {

        fileChooser.setTitle("Rseim Seçiniz");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("video Files", "*.mp4", "*.mpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            label2.setText(file.getPath());

        }

    }


    @FXML
    private void taramayabasla() {

        init();
        // preserve image ratio
        videogoster.setPreserveRatio(true);
        if (file != null ) {
            if (!this.cameraActive) {
                // disable setting checkboxes
                this.haarClassifier.setDisable(true);
                this.lbpClassifier.setDisable(true);
                scheduledExecutorService = Executors.newSingleThreadScheduledExecutor((Runnable r) -> {
                    thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                });

                // start the video capture
                this.capture.open(file.getPath());

                // is the video stream available?
                if (this.capture.isOpened()) {
                    // is the video stream available?
                    if (this.capture.isOpened()) {
                        this.cameraActive = true;

                        imageToShow = grabFrame();

                        scheduledExecutorService.scheduleAtFixedRate(() -> {
                            Task<Image> task = new Task<Image>() {
                                @Override
                                public Image call() {
                                    imageToShow = grabFrame();
                                    return imageToShow;
                                }

                            };

                            if (imageToShow == null) {

                                task.setOnSucceeded((WorkerStateEvent event) -> {
                                    cameraActive = true;
                                    cameraButton.fire();
                                });
                            } else {
                                task.setOnSucceeded((WorkerStateEvent event) -> {
                                    videogoster.setImage(task.getValue());
                                     eslesenresimler = classfinder.list(1);
                                     
                                  if(eslesenresimler.size()>0){
                                         for (int i = 0; i < eslesenresimler.size(); i++) {
                                             eslesen.setImage(eslesenresimler.get(0));
                                             if(i==1)
                                                eslesen1.setImage(eslesenresimler.get(1)); 
                                             if(i==2)
                                              eslesen2.setImage(eslesenresimler.get(2)); 
                                             
                                         }
                                     }
                                });
                            }
                            task.run();
                        }, 2, 33, TimeUnit.MILLISECONDS);

                        // update the button content
                        this.cameraButton.setText("Durdur");
                    } else {
                        // log the error
                        System.err.println("Failed to open the camera connection...");
                    }
                }
            } else {
                if (!scheduledExecutorService.isShutdown()) {
                    scheduledExecutorService.shutdown();

                }
                // the camera is not active at this point

                // update again the button content
                cameraActive = false;
                this.cameraButton.setText("Start Camera");
                // enable classifiers checkboxes
                this.haarClassifier.setDisable(false);
                this.lbpClassifier.setDisable(false);

                // release the camera
                this.capture.release();
                // clean the frame
                this.videogoster.setImage(null);
                this.eslesen.setImage(null);
                this.eslesen1.setImage(null);
                this.eslesen2.setImage(null);
              

            }
        } else {
            Dialogs.create()
                    .title("Uyarı")
                    .message("Lütfen video ve resimlerin bulunduğu dossyayı seçiniz!")
                    .showInformation();
        }
    }

    private Image grabFrame() {
        // init everything
        Image resimgoster = null;
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // face detection
                  if( classfinder.videoinject(frame, "C:\\bb"))
                    resimgoster = classfinder.mat2Image(frame);
                } else {
                    bittimi = true;

                }

            } catch (Exception e) {
                // log the (full) error
                System.err.println("ERROR: " + e);
            }
        }

        return resimgoster;
    }

    public static void debug(String msg) {
        System.out.println(msg);
    }
    
     @FXML
    protected void dosyaac(Event event) throws IOException {
        
         File file1 = new File ("C:\\bb\\");
                 Desktop desktop = Desktop.getDesktop();
                   desktop.open(file1);
    }
    

    /**
     * The action triggered by selecting the Haar Classifier checkbox. It loads
     * the trained set to be used for frontal face detection.
     */
    @FXML
    protected void haarSelected(Event event) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // check whether the lpb checkbox is selected and deselect it
        if (this.lbpClassifier.isSelected()) {
            this.lbpClassifier.setSelected(false);

        }

        if (this.lbpClassifier.isSelected() || this.haarClassifier.isSelected()) {
            this.cameraButton.setDisable(false);
        } else {
            this.cameraButton.setDisable(true);
        }
        classfinder.CascadeClassbulucu("C:/opencv/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
        //this.checkboxSelection("C:/opencv/sources/data/haarcascades/haarcascade_frontalface_alt.xml");

    }

    /**
     * The action triggered by selecting the LBP Classifier checkbox. It loads
     * the trained set to be used for frontal face detection.
     */
    @FXML
    protected void lbpSelected(Event event) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // check whether the haar checkbox is selected and deselect it
        if (this.haarClassifier.isSelected()) {
            this.haarClassifier.setSelected(false);

        }
        if (this.lbpClassifier.isSelected() || this.haarClassifier.isSelected()) {
            this.cameraButton.setDisable(false);
        } else {
            this.cameraButton.setDisable(true);
        }
        classfinder.CascadeClassbulucu("resources/lbpcascades/lbpcascade_frontalface.xml");

    }

}
