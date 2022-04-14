/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Tuned for use with the hub at Provincials
 * ================================================== */

package frc.robot.pipelines;

public class ProvincialSettings extends PipelineSettings {

	public ProvincialSettings() {
		isRGB = true;
		cameraExposure = 25;

		rgbThresholdRed[0] = 100;
		rgbThresholdRed[1] = 250;
		rgbThresholdGreen[0] = 140;
		rgbThresholdGreen[1] = 255;
		rgbThresholdBlue[0] = 110;
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
		filterContoursMaxHeight = 30.0;
		filterContoursSolidity[0] = 0;
		filterContoursSolidity[1] = 100;
		filterContoursMaxVertices = 1000000.0;
		filterContoursMinVertices = 0.0;
		filterContoursMinRatio = 1.1;
		filterContoursMaxRatio = 30;
	}
}