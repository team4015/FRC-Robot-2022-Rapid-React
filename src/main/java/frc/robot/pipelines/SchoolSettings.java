/* ==================================================
 * Authors: Daniel Ye and Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Tuned for use with the prop hub we have at school
 * with separate pieces of tape.
 * ================================================== */

package frc.robot.pipelines;

public class SchoolSettings extends PipelineSettings {

	public SchoolSettings() {
		isRGB = true;
		cameraExposure = 25;

		rgbThresholdRed[0] = 0;
		rgbThresholdRed[1] = 146;
		rgbThresholdGreen[0] = 147;
		rgbThresholdGreen[1] = 255;
		rgbThresholdBlue[0] = 138;
		rgbThresholdBlue[1] = 255;

		filterContoursMinArea = 1.0;
		filterContoursMinPerimeter = 0.0;
		filterContoursMinWidth = 0.0;
		filterContoursMaxWidth = 1000.0;
		filterContoursMinHeight = 0.0;
		filterContoursMaxHeight = 100.0;
		filterContoursSolidity[0] = 60;
		filterContoursSolidity[1] = 100;
		filterContoursMaxVertices = 1000000.0;
		filterContoursMinVertices = 0.0;
		filterContoursMinRatio = 1.1;
		filterContoursMaxRatio = 30;
	}
}