/* ==================================================
 * Authors: Daniel Ye and Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Tuned for use with the prop hub we have at school
 * with one long piece of tape
 * ================================================== */

package frc.robot.pipelines;

public class LongSettings extends PipelineSettings {

	public LongSettings() {
		isRGB = true;
		cameraExposure = 25;

		rgbThresholdRed[0] = 0;
		rgbThresholdRed[1] = 220;
		rgbThresholdGreen[0] = 100;
		rgbThresholdGreen[1] = 255;
		rgbThresholdBlue[0] = 60;
		rgbThresholdBlue[1] = 255;

		hsvThresholdHue[0] = 36;
		hsvThresholdHue[1] = 95;
		hsvThresholdSaturation[0] = 40; //was 100
		hsvThresholdSaturation[1] = 255;
		hsvThresholdValue[0] = 0;
		hsvThresholdValue[1] = 255.0;

		filterContoursMinArea = 0.25; //suggestion: to 0
		filterContoursMinPerimeter = 0.0;
		filterContoursMinWidth = 0.0;
		filterContoursMaxWidth = 30.0;
		filterContoursMinHeight = 0.0;
		filterContoursMaxHeight = 30.0;
		filterContoursSolidity[0] = 0;
		filterContoursSolidity[1] = 100;
		filterContoursMaxVertices = 1000000.0;
		filterContoursMinVertices = 0.0;
		filterContoursMinRatio = 0.9;// Suggestion: to 0.9
		filterContoursMaxRatio = 30;
	}
}