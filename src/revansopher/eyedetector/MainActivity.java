package revansopher.eyedetector;

import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity implements CvCameraViewListener2 {

	protected static final String TAG = "EyeDetector";
	
	CascadeClassifier face_cascade;
	CascadeClassifier eyes_cascade;
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	    @Override
	    public void onManagerConnected(int status) {
	        switch (status) {
	            case LoaderCallbackInterface.SUCCESS:
	            {
	                Log.i(TAG, "OpenCV loaded successfully");
	                mOpenCvCameraView.enableView();
	            } break;
	            default:
	            {
	                super.onManagerConnected(status);
	            } break;
	        }
	    }
	};

	@Override
	public void onResume()
	{
	    super.onResume();
	    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
	}
	
	private CameraBridgeViewBase mOpenCvCameraView;

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	     Log.i(TAG, "called onCreate");
	     super.onCreate(savedInstanceState);
	     
	     if(!OpenCVLoader.initDebug()){
	    	 Log.e(TAG, "OpenCV Loading error");
	     }
	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	     setContentView(R.layout.activity_main);
	     mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.OpenCvView);
	     mOpenCvCameraView.setMaxFrameSize(256, 256); //tiny for speed
	     mOpenCvCameraView.setCameraIndex(1); //front
	     mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
	     mOpenCvCameraView.setCvCameraViewListener(this);
	     
	     //Load the cascades
	     face_cascade = new CascadeClassifier("/sdcard/"+"haarcascade_frontalface_alt.xml");
	     //eyes_cascade = new CascadeClassifier("/sdcard/"+"haarcascade_eye_tree_eyeglasses.xml");
	 }

	 @Override
	 public void onPause()
	 {
	     super.onPause();
	     if (mOpenCvCameraView != null)
	         mOpenCvCameraView.disableView();
	 }

	 public void onDestroy() {
	     super.onDestroy();
	     if (mOpenCvCameraView != null)
	         mOpenCvCameraView.disableView();
	 }

	 public void onCameraViewStarted(int width, int height) {
	 }

	 public void onCameraViewStopped() {
	 }

	 public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		 Mat frame = inputFrame.rgba();
		 
		 Mat frame_gray = inputFrame.gray();
		 Imgproc.equalizeHist(frame_gray, frame_gray);
		 MatOfRect faces = new MatOfRect();
		 face_cascade.detectMultiScale(frame_gray, faces);//,1.1, 2, 0|Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size(600, 600));
		 
		 List<Rect> faceList = faces.toList();
		 for(Rect face : faceList){
			 Point center = new Point( face.x + face.width/2, face.y + face.height/2);
			 Core.ellipse(frame, center, new Size( face.width/2, face.height/2), 0, 0, 360, new Scalar( 255, 0, 255), 2, 8, 0);
			 
			 /*
			 MatOfRect eyes = new MatOfRect();
			 eyes_cascade.detectMultiScale(frame_gray, eyes);//, 1.1, 2, 0|Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size(600, 600));
			 List<Rect> eyeList = eyes.toList();
			 for(Rect eye : eyeList){
				 Point eye_center = new Point(eye.x+eye.width/2, eye.y+eye.height/2);
				 int radius = (int)Math.round((eye.width+eye.height)*.25);
				 Core.circle(frame, eye_center, radius, new Scalar(255, 0, 0), 3, 8, 0);
				 
				 Log.d(TAG, "x: "+eye.x+", y: "+eye.y);
			 }
			 */
			 
			 Log.v(TAG, "Width: "+face.width);
			 
			 
		 }
		 
	     return frame;
	 }

}
