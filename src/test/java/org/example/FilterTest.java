package org.example;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FilterTest {
    @Test
    public void testApplyFilters() throws Exception {
        ImageAPI api = new ImageAPI();

        String imgPath = "/home/vboxuser/Pictures/car.jpeg";
        String outDir = "/home/vboxuser/Pictures/filter_results/";
        Files.createDirectories(Paths.get(outDir));

        int[] kernelSizes = {3, 5, 7};

        for (int kernelSize : kernelSizes) {
            FilterResults filterResults = FilterUtils.applyFilters(imgPath, kernelSize);

            if (filterResults == null) {
                System.err.println("Filter results are null for kernel size: " + kernelSize);
                continue;
            }

            api.showImage(filterResults.blur);
            api.saveImage(String.format("%scar_blur_%d.jpeg", outDir, kernelSize), filterResults.blur);

            api.showImage(filterResults.bilateral);
            api.saveImage(String.format("%scar_bilateral_%d.jpeg", outDir, kernelSize), filterResults.bilateral);

            api.showImage(filterResults.gaussian);
            api.saveImage(String.format("%scar_gaussian_%d.jpeg", outDir, kernelSize), filterResults.gaussian);

            api.showImage(filterResults.median);
            api.saveImage(String.format("%scar_median_%d.jpeg", outDir, kernelSize), filterResults.median);
        }
    }
}
