/* =================================== 
 * Author: Lucas Jacobs
 * ------------------------------
 * Desc: Template class for all the 
 * different vision pipelines
 * ===================================*/

package frc.robot.pipelines;

import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import edu.wpi.first.vision.VisionPipeline;

public abstract class PipelineTemplate implements VisionPipeline {
    public boolean isRGB; // true - uses rgb filter, false - uses hsv filter
    public int cameraExposure;

    public double[] rgbThresholdRed = new double[2];
	public double[] rgbThresholdGreen = new double[2];
	public double[] rgbThresholdBlue = new double[2];

    public double[] hsvThresholdHue = new double[2];
	public double[] hsvThresholdSaturation = new double[2];
	public double[] hsvThresholdValue = new double[2];

    public double filterContoursMinArea = 0;
    public double filterContoursMinPerimeter = 0;
    public double filterContoursMinWidth = 0;
	public double filterContoursMaxWidth = 0;
	public double filterContoursMinHeight = 0;
	public double filterContoursMaxHeight = 0;
	public double[] filterContoursSolidity = new double[2];
    public double filterContoursMaxVertices = 1000000.0;
	public double filterContoursMinVertices = 0.0;
    public double filterContoursMinRatio = 0;
	public double filterContoursMaxRatio = 0;

    public abstract ArrayList<MatOfPoint> filterContoursOutput();
    public abstract Mat rgbThresholdOutput();
    public abstract Mat hsvThresholdOutput();
}
