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

    public double[] rgbThresholdRed;
	public double[] rgbThresholdGreen;
	public double[] rgbThresholdBlue;

    public double[] hsvThresholdHue;
	public double[] hsvThresholdSaturation;
	public double[] hsvThresholdValue;

    public double filterContoursMinArea;
    public double filterContoursMinWidth;
	public double filterContoursMaxWidth;
	public double filterContoursMinHeight;
	public double filterContoursMaxHeight;
	public double[] filterContoursSolidity;
    public double filterContoursMinRatio;
	public double filterContoursMaxRatio;

    public abstract ArrayList<MatOfPoint> filterContoursOutput();
    public abstract Mat rgbThresholdOutput();
    public abstract Mat hsvThresholdOutput();
}
