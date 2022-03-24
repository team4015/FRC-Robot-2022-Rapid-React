/* ==================================================
 * Authors: Daniel Ye and Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Settings for the vision pipeline based on videos from Humber
 * ================================================== */

package frc.robot.pipelines;

public class HumberSettings extends PipelineSettings {

	public HumberSettings() {
		isRGB = false;
		cameraExposure = -1;

		hsvThresholdHue[0] = 33.99280575539568;
		hsvThresholdHue[1] = 89.38566552901024;
		hsvThresholdSaturation[0] = 0;
		hsvThresholdSaturation[1] = 24.368600682593875;
		hsvThresholdValue[0] = 217.85071942446038;
		hsvThresholdValue[1] = 255.0;

		filterContoursMinArea = 2.0;
		filterContoursMinPerimeter = 0.0;
		filterContoursMinWidth = 5.0;
		filterContoursMaxWidth = 1000.0;
		filterContoursMinHeight = 0.0;
		filterContoursMaxHeight = 100.0;
		filterContoursSolidity[0] = 0.0;
		filterContoursSolidity[1] = 76.43097643097643;
		filterContoursMaxVertices = 1000000.0;
		filterContoursMinVertices = 0.0;
		filterContoursMinRatio = 1.5;
		filterContoursMaxRatio = 5.0;
	}
}