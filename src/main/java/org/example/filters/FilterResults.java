package org.example.filters;

import org.opencv.core.Mat;

public class FilterResults {
    public Mat blur;
    public Mat gaussian;
    public Mat median;
    public Mat bilateral;

    public FilterResults(Mat blur, Mat gaussian, Mat median, Mat bilateral) {
        this.blur = blur;
        this.gaussian = gaussian;
        this.median = median;
        this.bilateral = bilateral;
    }
}
