package main.ca.carleton.sysc.util;

import java.util.List;

public class GCodeTransformer {

    private final List<String> CONVERTIBLE_CODES = List.of("G0", "G1");

    private final double width;

    private final double x0;

    private final double y0;

    private double previousX;

    private double previousY;

    private double minusA = 0;

    private double minusB = 0;

    public GCodeTransformer(final double width, final double x0, final double y0) {
        this.width = width;
        this.x0 = x0;
        this.y0 = y0;
        this.previousX = x0;
        this.previousY = y0;
    }

    public String cartesianToVPlot(final String line) {
        final String code = this.getCode(line);
        if (line.contains("Z") || !CONVERTIBLE_CODES.contains(code)) {
            return line;
        }

        final double x = this.getX(line) + x0;
        final double y = this.getY(line) + y0;
        double a = this.calcA(x, y);
        double b = this.calcB(x, y);

        if(this.minusA == 0) {
            this.minusA = a;
            this.minusB = b;
        }

        a = Math.round((a - this.minusA) * 1000d) / 1000d;
        b = Math.round((b - this.minusB) * 1000d) / 1000d;

        return String.format("%s X%s Y%s", code, a, b);
    }

    private String getCode(final String line) {
        return line.split("\\s+")[0];
    }

    private double getX(final String line) {
        final String[] split = line.split("\\s+");
        final double x = split[1].contains("X") ? Double.parseDouble(split[1].substring(1)) : this.previousX;
        this.previousX = x;
        return x;
    }

    private double getY(final String line) {
        final String[] split = line.split("\\s+");
        final double y;

        if(split[1].contains("Y")) {
            y = Double.parseDouble(split[1].substring(1));
        } else if(split.length > 2 && split[2].contains("Y")) {
            y = Double.parseDouble(split[2].substring(1));
        } else {
            y = this.previousY;
        }
        this.previousY = y;
        return y;
    }

    private double calcA(final double x, final double y) {
        final double a;

        a = Math.sqrt(x * x + y * y);
        return Math.round(a * 1000d) / 1000d;
    }

    private double calcB(final double x, final double y) {
        final double b;

        b = Math.sqrt((this.width - x) * (this.width - x) + y * y);
        return Math.round(b * 1000d) / 1000d;
    }

}
