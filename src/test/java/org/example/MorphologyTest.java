package org.example;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MorphologyTest {
    @Test
    public void testApplyMorphology() throws Exception {
        ImageAPI api = new ImageAPI();

        String imgPath = "src/test/test_dir/adv.png";
        String outDir = "src/test/test_dir/morphology_results/";
        Files.createDirectories(Paths.get(outDir));

        int[] kernelSizes = {3, 5, 7, 9, 13, 15};
        int[] morphTypes = {Imgproc.MORPH_GRADIENT, Imgproc.MORPH_BLACKHAT};
        int[] shapeTypes = {Imgproc.MORPH_RECT, Imgproc.MORPH_ELLIPSE};

        for (int kernelSize : kernelSizes) {
            for (int morphType : morphTypes) {
                String morphPrefix = (morphType == Imgproc.MORPH_GRADIENT) ? "gr_" : "bl_";

                for (int shapeType : shapeTypes) {
                    String shapePrefix = (shapeType == Imgproc.MORPH_RECT) ? "rec_" : "ell_";

                    Mat result = MorphologyUtils.applyMorphology(imgPath, kernelSize, morphType, shapeType);

                    String filename = String.format("%s%s%s%d.jpg", outDir, morphPrefix, shapePrefix, kernelSize);
                    api.saveImage(filename, result);
                }
            }
        }
    }
}
