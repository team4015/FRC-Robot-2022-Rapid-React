/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Standard pipline that vision runs. Its settings get 
 * overwritten by PipelineSettings.
 * ================================================== */

package frc.robot.pipelines;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.*;

import edu.wpi.first.vision.VisionPipeline;

public class StandardPipeline implements VisionPipeline {

	//Outputs
	private Mat rgbThresholdOutput = new Mat();
	private Mat maskOutput = new Mat();
	private Mat hsvThresholdOutput = new Mat();
	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
	private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public boolean isRGB = true; // true - uses rgb filter, false - uses hsv filter
    public int cameraExposure = -1;

    public double[] rgbThresholdRed = new double[2];
	public double[] rgbThresholdGreen = new double[2];
	public double[] rgbThresholdBlue = new double[2];

    public double[] hsvThresholdHue = new double[2];
	public double[] hsvThresholdSaturation = new double[2];
	public double[] hsvThresholdValue = new double[2];

    public double filterContoursMinArea = 0;
	public double maxArea = 150;
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


	/* ========================================================
	 * Author: Lucas Jacobs
	 * Desc: Sets the settings of the pipline to be the same as 
	 * the given setting class 
	 * ======================================================== */
	public void set(PipelineSettings settings) {
		isRGB = settings.isRGB;
		cameraExposure = settings.cameraExposure;

		rgbThresholdRed = settings.rgbThresholdRed;
		rgbThresholdGreen = settings.rgbThresholdGreen;
		rgbThresholdBlue = settings.rgbThresholdBlue;

		hsvThresholdHue = settings.hsvThresholdHue;
		hsvThresholdSaturation = settings.hsvThresholdSaturation;
		hsvThresholdValue = settings.hsvThresholdValue;

		filterContoursMinArea = settings.filterContoursMinArea;
		filterContoursMinPerimeter = settings.filterContoursMinPerimeter;
		filterContoursMinWidth = settings.filterContoursMinWidth;
		filterContoursMaxWidth = settings.filterContoursMaxWidth;
		filterContoursMinHeight = settings.filterContoursMinHeight;
		filterContoursMaxHeight = settings.filterContoursMaxHeight;
		filterContoursSolidity = settings.filterContoursSolidity;
		filterContoursMaxVertices = settings.filterContoursMaxVertices;
		filterContoursMinVertices = settings.filterContoursMinVertices;
		filterContoursMinRatio = settings.filterContoursMinRatio;
		filterContoursMaxRatio = settings.filterContoursMaxRatio;
	}

	/**
	 * This is the primary method that runs the entire pipeline and updates the outputs.
	 */
	public void process(Mat source0) {
		// Step RGB_Threshold0:
		Mat rgbThresholdInput = source0;
		rgbThreshold(rgbThresholdInput, rgbThresholdRed, rgbThresholdGreen, rgbThresholdBlue, rgbThresholdOutput);

		// Step HSV_Threshold0:
		Mat hsvThresholdInput = source0;
		hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

		// Step Mask0:
		Mat maskInput = hsvThresholdOutput;
		Mat maskMask = rgbThresholdOutput;
		mask(maskInput, maskMask, maskOutput);

		// Step Find_Contours0:
		Mat findContoursInput = maskOutput;
		boolean findContoursExternalOnly = true;
		findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

		// Step Filter_Contours0:
		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
		filterContours(filterContoursContours, maxArea, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);

	}

	/**
	 * This method is a generated getter for the output of a RGB_Threshold.
	 * @return Mat output from RGB_Threshold.
	 */
	public Mat rgbThresholdOutput() {
		return rgbThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a Mask.
	 * @return Mat output from Mask.
	 */
	public Mat maskOutput() {
		return maskOutput;
	}

	/**
	 * This method is a generated getter for the output of a HSV_Threshold.
	 * @return Mat output from HSV_Threshold.
	 */
	public Mat hsvThresholdOutput() {
		return hsvThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public ArrayList<MatOfPoint> findContoursOutput() {
		return findContoursOutput;
	}

	/**
	 * This method is a generated getter for the output of a Filter_Contours.
	 * @return ArrayList<MatOfPoint> output from Filter_Contours.
	 */
	public ArrayList<MatOfPoint> filterContoursOutput() {
		return filterContoursOutput;
	}


	/**
	 * Segment an image based on color ranges.
	 * @param input The image on which to perform the RGB threshold.
	 * @param red The min and max red.
	 * @param green The min and max green.
	 * @param blue The min and max blue.
	 * @param output The image in which to store the output.
	 */
	private void rgbThreshold(Mat input, double[] red, double[] green, double[] blue,
		Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2RGB);
		Core.inRange(out, new Scalar(red[0], green[0], blue[0]),
			new Scalar(red[1], green[1], blue[1]), out);
	}

	/**
	 * Filter out an area of an image using a binary mask.
	 * @param input The image on which the mask filters.
	 * @param mask The binary image that is used to filter.
	 * @param output The image in which to store the output.
	 */
	private void mask(Mat input, Mat mask, Mat output) {
		mask.convertTo(mask, CvType.CV_8UC1);
		Core.bitwise_xor(output, output, output);
		input.copyTo(output, mask);
	}

	/**
	 * Segment an image based on hue, saturation, and value ranges.
	 *
	 * @param input The image on which to perform the HSL threshold.
	 * @param hue The min and max hue
	 * @param sat The min and max saturation
	 * @param val The min and max value
	 * @param output The image in which to store the output.
	 */
	private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
	    Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
		Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
			new Scalar(hue[1], sat[1], val[1]), out);
	}

	/**
	 * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
	 * @param input The image on which to perform the Distance Transform.
	 * @param type The Transform.
	 * @param maskSize the size of the mask.
	 * @param output The image in which to store the output.
	 */
	private void findContours(Mat input, boolean externalOnly,
		List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		}
		else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);
	}


	/**
	 * Filters out contours that do not meet certain criteria.
	 * @param inputContours is the input list of contours
	 * @param output is the the output list of contours
	 * @param minArea is the minimum area of a contour that will be kept
	 * @param minPerimeter is the minimum perimeter of a contour that will be kept
	 * @param minWidth minimum width of a contour
	 * @param maxWidth maximum width
	 * @param minHeight minimum height
	 * @param maxHeight maximimum height
	 * @param Solidity the minimum and maximum solidity of a contour
	 * @param minVertexCount minimum vertex Count of the contours
	 * @param maxVertexCount maximum vertex Count
	 * @param minRatio minimum ratio of width to height
	 * @param maxRatio maximum ratio of width to height
	 */
	private void filterContours(List<MatOfPoint> inputContours, double maxArea, double minArea,
		double minPerimeter, double minWidth, double maxWidth, double minHeight, double
		maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
		minRatio, double maxRatio, List<MatOfPoint> output) {
		final MatOfInt hull = new MatOfInt();
		output.clear();
		//operation
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			final Rect bb = Imgproc.boundingRect(contour);
			if (bb.width < minWidth || bb.width > maxWidth) continue;
			if (bb.height < minHeight || bb.height > maxHeight) continue;
			final double area = Imgproc.contourArea(contour);
			if (area < minArea) continue;
			if (area > maxArea) continue;
			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
			Imgproc.convexHull(contour, hull);
			MatOfPoint mopHull = new MatOfPoint();
			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++) {
				int index = (int)hull.get(j, 0)[0];
				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
				mopHull.put(j, 0, point);
			}
			final double solid = 100 * area / Imgproc.contourArea(mopHull);
			if (solid < solidity[0] || solid > solidity[1]) continue;
			if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
			final double ratio = bb.width / (double)bb.height;
			if (ratio < minRatio || ratio > maxRatio) continue;
			output.add(contour);
		}
	}




}

