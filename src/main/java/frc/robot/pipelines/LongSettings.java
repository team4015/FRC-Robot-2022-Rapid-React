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
		rgbThresholdRed[1] = 150;
		rgbThresholdGreen[0] = 130;
		rgbThresholdGreen[1] = 255;
		rgbThresholdBlue[0] = 20;
		rgbThresholdBlue[1] = 255;

		filterContoursMinArea = 1.0;
		filterContoursMinPerimeter = 0.0;
		filterContoursMinWidth = 0.0;
		filterContoursMaxWidth = 1000.0;
		filterContoursMinHeight = 0.0;
		filterContoursMaxHeight = 100.0;
		filterContoursSolidity[0] = 20;
		filterContoursSolidity[1] = 50;
		filterContoursMaxVertices = 1000000.0;
		filterContoursMinVertices = 0.0;
		filterContoursMinRatio = 2;
		filterContoursMaxRatio = 100
		;
	}
}