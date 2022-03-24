/* =================================== 
 * Author: Lucas Jacobs
 * ------------------------------
 * Desc: Template class for all the 
 * different vision pipeline setting classes
 * ===================================*/

package frc.robot.pipelines;

public abstract class PipelineSettings {
    public boolean isRGB; // true - uses rgb filter, false - uses hsv filter
    public int cameraExposure;

    public double[] rgbThresholdRed = {0, 255};
	public double[] rgbThresholdGreen = {0, 255};
	public double[] rgbThresholdBlue = {0, 255};

    public double[] hsvThresholdHue = {0, 255};
	public double[] hsvThresholdSaturation = {0, 255};
	public double[] hsvThresholdValue = {0, 255};

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
}
