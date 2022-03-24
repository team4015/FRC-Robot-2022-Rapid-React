/* ==================================================
 * Authors: Daniel Ye and Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Used for testing different filter configurations at school or at competition.
 * ================================================== */

package frc.robot.pipelines;

public class TestSettings extends PipelineSettings {

	public TestSettings() {
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
		filterContoursSolidity[0] = 0;
		filterContoursSolidity[1] = 100;
		filterContoursMaxVertices = 1000000.0;
		filterContoursMinVertices = 0.0;
		filterContoursMinRatio = 1.1;
		filterContoursMaxRatio = 4;
	}
}