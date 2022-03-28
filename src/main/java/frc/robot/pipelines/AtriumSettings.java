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
		cameraExposure = 25;

		rgbThresholdRed[0] = 100;
		rgbThresholdRed[1] = 242;
		rgbThresholdGreen[0] = 140;
		rgbThresholdGreen[1] = 255;
		rgbThresholdBlue[0] = 124;
		rgbThresholdBlue[1] = 255;

		hsvThresholdHue[0] = 60;
		hsvThresholdHue[1] = 164;
		hsvThresholdSaturation[0] = 25;
		hsvThresholdSaturation[1] = 255;
		hsvThresholdValue[0] = 140;
		hsvThresholdValue[1] = 255.0;

		filterContoursMinArea = 1.0;
		filterContoursMinPerimeter = 0.0;
		filterContoursMinWidth = 0.0;
		filterContoursMaxWidth = 1000.0;
		filterContoursMinHeight = 0.0;
		filterContoursMaxHeight = 100.0;
		filterContoursSolidity[0] = 10;
		filterContoursSolidity[1] = 100;
		filterContoursMaxVertices = 1000000.0;
		filterContoursMinVertices = 0.0;
		filterContoursMinRatio = 1.1;
		filterContoursMaxRatio = 30;
	}
}