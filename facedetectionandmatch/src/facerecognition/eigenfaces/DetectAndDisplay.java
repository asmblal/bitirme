package facerecognition.eigenfaces;

import static facedetectionandmatch.videotaramaController.debug;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class DetectAndDisplay {

    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    int count = 0;
    Mat eslesenresim;
    boolean eslestimi = false;
    List<Image> eslesenresimlerimage = new ArrayList<>();
    List<Mat> bulunanresimler = new ArrayList<>();
    private double[] inputFaceData = null;
    
    public void CascadeClassbulucu(String path) {
        this.faceCascade = new CascadeClassifier(path);
    }

    public boolean detectedimage(Mat frame, String path, int navigator) throws IOException {

        eslesenresimlerimage.clear();
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        Mat resizeimage = new Mat();
        Mat size_image = new Mat();
        Size sz = new Size(125, 150);
        int size = 0;

        // convert the frame in gray scale
        if (frame.type() != 0) {
          Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        } else {
            grayFrame = frame;
        }

        Rect rectCrop = null;
        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        count++;
         int i = 0;
        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (Rect facesArray1 : facesArray) {

            if (count % 25 == 1) {
                rectCrop = new Rect(facesArray1.x, facesArray1.y, facesArray1.width, facesArray1.height);
                resizeimage = new Mat(grayFrame, rectCrop);

                Imgproc.resize(resizeimage, size_image, sz);
                // Imgcodecs.imwrite("ccc/camera" + i + ".png", size_image);
                BufferedImage images;
                byte[] data = new byte[125 * 150 * (int) size_image.elemSize()];
                int type;
                size_image.get(0, 0, data);

                if (size_image.channels() == 1) {
                    type = BufferedImage.TYPE_BYTE_GRAY;
                } else {
                    type = BufferedImage.TYPE_3BYTE_BGR;
                }

                images = new BufferedImage(125, 150, type);

                images.getRaster().setDataElements(0, 0, 125, 150, data);

                int imageWidth = images.getWidth();
                int imageHeight = images.getHeight();
                inputFaceData = new double[imageWidth * imageHeight];
                images.getData().getPixels(0, 0, imageWidth, imageHeight, inputFaceData);
                Matrix2D inputFace1 = new Matrix2D(inputFaceData, 1);
                inputFace1.normalise();
                try {
                    List<String> newFileNames = new FaceRec().parseDirectory(path, "png");
                    size = newFileNames.size();
                } catch (FaceRecError ex) {
                    Logger.getLogger(FaceRec.class.getName()).log(Level.SEVERE, null, ex);
                }

              try{
                  int a=0;
                  String b="";
                  if(navigator==1){
                      a=1;
                      b="0.19";
                  }
                   
                  else
                  {
                      if(size>3)
                       a=3;
                      else
                          a=size-1;
                      b="0.09";
                  }
                      MatchResult r = new FaceRec().processSelections(inputFace1, path,a, b);
                  if (r.getMatchSuccess()) {
                        

                            eslestimi = true;
                            File file = new File(r.getMatchFileName());
                            Image image = new Image(file.toURI().toString());
                            eslesenresimlerimage.add(image);

                            debug("ccc/camera" + i + ".png" + " matches " + r.getMatchFileName() + " at distance=" + r.getMatchDistance());

                       
                    } else {
                       Imgcodecs.imwrite("C:\\aa\\resim" + i + ".png",size_image);
                        eslestimi = false;
                    }
                
              }
              catch(Exception e){
                  
              }
              
            }
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(255, 255, 0), 1);
            Imgproc.putText(frame, "Yuz "+String.valueOf(i+1), new Point  (facesArray1.x,facesArray1.y-5), Core.FONT_ITALIC,0.7,new  Scalar(255));
            i++;
        }

        if (navigator == 1) {
            return eslestimi;

        } else {
            return true;
        }
    }

    public boolean videoinject(Mat frame, String path) throws IOException {

        eslesenresimlerimage.clear();
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        Mat resizeimage = new Mat();
        Mat size_image = new Mat();
        Size sz = new Size(125, 150);
        int size = 0;

        // convert the frame in gray scale
        if (frame.type() != 0) {
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        } else {
            grayFrame = frame;
        }

        Rect rectCrop = null;
        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        this.faceCascade.detectMultiScale(grayFrame, faces,1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        count++;
         int i = 0;
        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (Rect facesArray1 : facesArray) {
 
            if (count % 25 ==1/*||count % 25 ==7||count % 25 ==18*/) {
                rectCrop = new Rect(facesArray1.x, facesArray1.y, facesArray1.width, facesArray1.height);
                resizeimage = new Mat(grayFrame, rectCrop);

                Imgproc.resize(resizeimage, size_image, sz);
                // Imgcodecs.imwrite("ccc/camera" + i + ".png", size_image);
                try {
                    List<String> newFileNames = new FaceRec().parseDirectory(path, "png");
                    size = newFileNames.size();
                } catch (FaceRecError ex) {
                    Logger.getLogger(FaceRec.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (size < 3) {
                    Imgcodecs.imwrite("C:\\bb\\resim" + System.nanoTime() + ".png", size_image);

                } else {
                    
               BufferedImage images;
                byte[] data = new byte[125 * 150 * (int) size_image.elemSize()];
                int type;
                size_image.get(0, 0, data);

                if (size_image.channels() == 1) {
                    type = BufferedImage.TYPE_BYTE_GRAY;
                } else {
                    type = BufferedImage.TYPE_3BYTE_BGR;
                }

                images = new BufferedImage(125, 150, type);

                images.getRaster().setDataElements(0, 0, 125, 150, data);

                int imageWidth = images.getWidth();
                int imageHeight = images.getHeight();
                inputFaceData = new double[imageWidth * imageHeight];
                images.getData().getPixels(0, 0, imageWidth, imageHeight, inputFaceData);
                Matrix2D inputFace1 = new Matrix2D(inputFaceData, 1);
                inputFace1.normalise();
                    

                    MatchResult r = new FaceRec().processSelections(inputFace1, path,3, "0.06");
                    System.out.println(size);
                    if (r.getMatchSuccess()) {

                        File file = new File(r.getMatchFileName());
                        Image image = new Image(file.toURI().toString());
                       
                            eslesenresimlerimage.add(image);

                        debug("eşleşen" + " matches " + r.getMatchFileName() + " at distance=" + r.getMatchDistance());
                    } else {
                          
                        Imgcodecs.imwrite("C:\\bb\\resim" + System.nanoTime() + ".png", size_image);
       
                    }

                }
            }
              
           Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(255, 255, 0), 1);
            Imgproc.putText(frame, "Yuz "+String.valueOf(i+1), new Point  (facesArray1.x,facesArray1.y-5), Core.FONT_ITALIC,0.7,new  Scalar(255));
            i++;
        }

        return true;

    }

    public Mat detected(Mat frame) {

        eslesenresimlerimage.clear();
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        Mat resizeimage = new Mat();
        Size sz = new Size(125, 150);

        if (frame.type() != 0) {
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        } else {
            grayFrame = frame;
        }

        Rect rectCrop = null;
         this.faceCascade.detectMultiScale(grayFrame, faces,1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (Rect facesArray1 : facesArray) {
            rectCrop = new Rect(facesArray1.x, facesArray1.y, facesArray1.width, facesArray1.height);
            resizeimage = new Mat(grayFrame, rectCrop);
         
            Imgproc.resize(resizeimage, resizeimage, sz);

            bulunanresimler.add(resizeimage);
           Imgproc.putText(frame, "Bulunan yuzler", new Point  (facesArray1.x,facesArray1.y-5), Core.FONT_ITALIC, 0.5,new  Scalar(0, 255, 0));
            Imgproc.rectangle(frame, facesArray1.tl(), facesArray1.br(), new Scalar(255, 255, 0), 1);
        }

        return frame;
    }

    public List<Image> list(int navigator) {

        return eslesenresimlerimage;

    }

    public List<Mat> listbulunan() {

        return bulunanresimler;

    }

    public Image mat2Image(Mat frame) {
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer, according to the PNG format
        Imgcodecs.imencode(".jpg", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    public static BufferedImage MatToBufferedImage(Mat matrix) {

        MatOfByte mb = new MatOfByte();
        BufferedImage image = null;
        Imgcodecs.imencode(".png", matrix, mb);
        try {
            image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();

        }

        return image;
    }

}
