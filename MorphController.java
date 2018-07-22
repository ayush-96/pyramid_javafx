package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

public class MorphController {

	@FXML
	RadioButton dilate_btn;
	@FXML
	RadioButton erode_btn;
	@FXML
	RadioButton open_btn;
	@FXML
	RadioButton close_btn;
	@FXML
	RadioButton rect_btn;
	@FXML
	RadioButton ellipse_btn;
	@FXML
	RadioButton cross_btn;
	@FXML
	Slider slider;
	@FXML
	ImageView currentFrame;

	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}


	public void disableAll(String choice){
		if(choice.equals("D")){
			erode_btn.setSelected(false);
			open_btn.setSelected(false);
			close_btn.setSelected(false);
			dilate_btn.setSelected(true);
		}
		else if(choice.equals("E")){
			dilate_btn.setSelected(false);
			open_btn.setSelected(false);
			close_btn.setSelected(false);
			erode_btn.setSelected(true);
		}
		else if(choice.equals("O")){
			erode_btn.setSelected(false);
			dilate_btn.setSelected(false);
			close_btn.setSelected(false);
			open_btn.setSelected(true);
		}
		else if(choice.equals("C")){
			erode_btn.setSelected(false);
			open_btn.setSelected(false);
			dilate_btn.setSelected(false);
			close_btn.setSelected(true);
		}
		else if(choice.equals("Rect")){
			cross_btn.setSelected(false);
			ellipse_btn.setSelected(false);
			rect_btn.setSelected(true);
		}
		else if(choice.equals("Ellipse")){
			cross_btn.setSelected(false);
			rect_btn.setSelected(false);
			ellipse_btn.setSelected(true);
		}
		else if(choice.equals("Cross")){
			rect_btn.setSelected(false);
			ellipse_btn.setSelected(false);
			cross_btn.setSelected(true);
		}
	}

	public Mat getImage(){
		String s="E:/blog/featured.jpg";
		Mat mat=new Mat();
		mat=Imgcodecs.imread(s);
		return mat;
	}

	public int getShape(){
		if(rect_btn.isSelected())
			return 0;
		else if(ellipse_btn.isSelected())
			return 2;
		else
			return 1;
	}

	public Mat getKernelFromShape(int elementSize,int elementShape){
		return Imgproc.getStructuringElement(elementShape, new Size(elementSize*2+1, elementSize*2+1), new Point(elementSize,elementSize));
	}

	public WritableImage matToImage(Mat mat)throws IOException{
		MatOfByte buf=new MatOfByte();
		Imgcodecs.imencode(".jpg", mat, buf);
		byte[] buffer=buf.toArray();
		InputStream in=new ByteArrayInputStream(buffer);
		BufferedImage bf=ImageIO.read(in);
		WritableImage wt=SwingFXUtils.toFXImage(bf, null);
		return wt;
	}

	public void onDilate() throws IOException{
		disableAll("D");
		Mat mat=getImage();
		Mat dst=new Mat();
		Mat kernelMat=getKernelFromShape((int)slider.getValue(),getShape());
		Imgproc.dilate(mat, dst, kernelMat);
		currentFrame.setImage(matToImage(dst));
	}

	public void onErode() throws IOException{
		disableAll("E");
		Mat mat=getImage();
		Mat dst=new Mat();
		Mat kernelMat=getKernelFromShape((int)slider.getValue(),getShape());
		Imgproc.erode(mat, dst, kernelMat);
		currentFrame.setImage(matToImage(dst));
	}

	public void onOpen() throws IOException{
		disableAll("O");
		Mat mat=getImage();
		Mat dst=new Mat();
		Mat kernelMat=getKernelFromShape((int)slider.getValue(),getShape());
		Imgproc.morphologyEx(mat, dst, Imgproc.MORPH_OPEN, kernelMat);
		currentFrame.setImage(matToImage(dst));
	}

	public void onClose() throws IOException{
		disableAll("C");
		Mat mat=getImage();
		Mat dst=new Mat();
		Mat kernelMat=getKernelFromShape((int)slider.getValue(),getShape());
		Imgproc.morphologyEx(mat, dst, Imgproc.MORPH_CLOSE, kernelMat);
		currentFrame.setImage(matToImage(dst));
	}

	public void onRect(){
		disableAll("Rect");
	}

	public void onEllipse(){
		disableAll("Ellipse");
	}

	public void onCross(){
		disableAll("Cross");
	}

}
