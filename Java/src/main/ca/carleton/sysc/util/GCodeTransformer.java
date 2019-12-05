package main.ca.carleton.sysc.util;

import main.ca.carleton.sysc.types.Command;

import java.util.List;

public class GCodeTransformer {

    private static final List<String> CONVERTIBLE_CODES = List.of("G0", "G1");

    private static final String X_DIST = "dist.x";

    private static final String Y_DIST = "dist.y";

    private static final String X_MARGIN = "margin.x";

    private static final String Y_MARGIN = "margin.y";

    private final ConfigManager configManager;

    private final double width;

    private final double x0;

    private final double y0;

    private final double a0;

    private final double b0;

    private final double initialA;

    private final double initialB;

    private double previousX;

    private double previousY;

    public GCodeTransformer() {
        this.configManager = new ConfigManager();

        this.width = Integer.parseInt(this.configManager.getConfig(X_DIST));;
        this.x0 = Integer.parseInt(this.configManager.getConfig(X_MARGIN));;
        this.y0 = -Integer.parseInt(this.configManager.getConfig(Y_MARGIN));;
        this.a0 = this.calcA(this.x0, this.y0);
        this.b0 = this.calcB(this.x0, this.y0);
        this.initialA = this.calcInitialA();
        this.initialB = this.calcInitialB();
        this.previousX = this.x0;
        this.previousY = this.y0;
    }

    public String cartesianToVPlot(final String line) {
        final String code = this.getCode(line);
        if (line.contains("Z") || !CONVERTIBLE_CODES.contains(code)) {
            return line;
        }

        final double x = this.getX(line) + x0;
        final double y = this.getY(line) + y0;

        // calculate the distance of string travel
        double a = this.calcA(x, y);
        double b = this.calcB(x, y);

        // add offsets to set lengths into cartesian coordinates
        a = a - this.a0 + this.initialA;
        b = b - this.b0 + this.initialB;

        a = Math.round(a * 1000d) / 1000d;
        b = Math.round(b * 1000d) / 1000d;

        return String.format("%s X%s Y%s", code, a, b);
    }

    /**
     * Calculate the initial position of the 'A' wire controlled by the x coordinate when placed at the top left of the margin area
     * @return  initial position of the 'A' wire at top left corner of drawing area
     */
    public double calcInitialA() {
        final int yDist = Integer.parseInt(this.configManager.getConfig(Y_DIST));
        final int xMargin = Integer.parseInt(this.configManager.getConfig(X_MARGIN));
        final int yMargin = Integer.parseInt(this.configManager.getConfig(Y_MARGIN));

        final double a0 = yDist;
        final double a1 = Math.sqrt(xMargin * xMargin + yMargin * yMargin);

        return -(a0 - a1);
    }

    /**
     * Calculate the initial position of the 'B' wire controlled by the x coordinate when placed at the top left of the margin area
     * @return  initial position of the 'B' wire at top left corner of drawing area
     */
    public double calcInitialB() {
        final int yDist = Integer.parseInt(this.configManager.getConfig(Y_DIST));
        final int xDist = Integer.parseInt(this.configManager.getConfig(X_DIST));
        final int xMargin = Integer.parseInt(this.configManager.getConfig(X_MARGIN));
        final int yMargin = Integer.parseInt(this.configManager.getConfig(Y_MARGIN));

        final double b0 = Math.sqrt(xDist * xDist + yDist * yDist);
        final double b1 = Math.sqrt((xDist - xMargin) * (xDist - xMargin) + yMargin * yMargin);

        return -(b0 - b1);
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
