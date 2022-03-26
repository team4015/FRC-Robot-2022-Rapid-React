/* ==================================================
 * Authors: Daniel Ye and Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Tuned for use with the prop hub we have in the atrium
 * with separate pieces of tape.
 * ================================================== */

package frc.robot.pipelines;

public class AtriumSettings extends PipelineSettings {

	public AtriumSettings() {
		isRGB = true;
		cameraExposure = 22;

		rgbThresholdRed[0] = 100;
		rgbThresholdRed[1] = 240;
		rgbThresholdGreen[0] = 120;
		rgbThresholdGreen[1] = 255;
		rgbThresholdBlue[0] = 100;
		rgbThresholdBlue[1] = 180;

		hsvThresholdHue[0] = 0;
		hsvThresholdHue[1] = 255;
		hsvThresholdSaturation[0] = 0;
		hsvThresholdSaturation[1] = 255;
		hsvThresholdValue[0] = 0;
		hsvThresholdValue[1] = 255.0;

		filterContoursMinArea = 25.0;
		filterContoursMinPerimeter = 0.0;
		filterContoursMinWidth = 25.0;
		filterContoursMaxWidth = 1000.0;
		filterContoursMinHeight = 0.0;
		filterContoursMaxHeight = 100.0;
		filterContoursSolidity[0] = 20;
		filterContoursSolidity[1] = 100;
		filterContoursMaxVertices = 1000000.0;
		filterContoursMinVertices = 0.0;
		filterContoursMinRatio = 1.1;
		filterContoursMaxRatio = 30;
	}
}