/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package ps0.turtle;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle     the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        for (int i = 0; i < 4; i++) {
            turtle.forward(sideLength);
            turtle.turn(90);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon; you
     * should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        return (double) 180 * (sides - 2) / sides;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular
     * polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see
     * java.lang.Math). HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        return (int) Math.round(360 / (180 - angle));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to
     * draw.
     * 
     * @param turtle     the turtle context
     * @param sides      number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double degree = 180 - calculateRegularPolygonAngle(sides);
        for (int i = 0; i < sides; i++) {
            turtle.forward(sideLength);
            turtle.turn(degree);
        }
    }

    /**
     * Given the current direction, current location, and a target location,
     * calculate the heading towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in
     * the direction of the target point (targetX,targetY), given that the turtle is
     * already at the point (currentX,currentY) and is facing at angle
     * currentHeading. The angle must be expressed in degrees, where 0 <= angle <
     * 360.
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX       current location x-coordinate
     * @param currentY       current location y-coordinate
     * @param targetX        target point x-coordinate
     * @param targetY        target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY, int targetX,
            int targetY) {
        return (90 - Math.toDegrees(Math.atan2(targetY - currentY, targetX - currentX)) - currentHeading + 360) % 360;
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get
     * from each point to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0
     * degrees). For each subsequent point, assumes that the turtle is still facing
     * in the direction it was facing when it moved to the previous point. You
     * should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of
     *         points) == 0, otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        int n = xCoords.size();
        if (n == 0)
            throw new IllegalArgumentException("xCoords and yCoords must have at least one element");
        if (n != yCoords.size())
            throw new IllegalArgumentException("xCoords and yCoords must have the same size");
        double currentHeading = 0;
        int currentX = xCoords.get(0);
        int currentY = yCoords.get(0);
        List<Double> headings = new ArrayList<Double>();
        for (int i = 1; i < n; i++) {
            double turnHeading = calculateHeadingToPoint(currentHeading, currentX, currentY, xCoords.get(i),
                    yCoords.get(i));
            currentHeading += turnHeading;
            headings.add(turnHeading);
            currentX = xCoords.get(i);
            currentY = yCoords.get(i);
        }
        return headings;
    }

    /**
     * Calculate the distance between each pair of consecutive points.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of distances between points
     */

    public static List<Double> calculateLengths(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> lengths = new ArrayList<Double>();
        int n = xCoords.size();
        if (n == 0)
            throw new IllegalArgumentException("xCoords and yCoords must have at least one element");
        if (n != yCoords.size())
            throw new IllegalArgumentException("xCoords and yCoords must have the same size");
        int currentX = xCoords.get(0);
        int currentY = yCoords.get(0);
        for (int i = 1; i < n; i++) {
            int nextX = xCoords.get(i), nextY = yCoords.get(i);
            int dx = nextX - currentX, dy = nextY - currentY;
            double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            lengths.add(length);
            currentX = nextX;
            currentY = nextY;
        }
        return lengths;
    }

    /**
     * Draw the art by linking dots one by one
     * 
     * @param turtle  a turtle object
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     */

    public static void drawPersonalArt(DrawableTurtle turtle, List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> headings = calculateHeadings(xCoords, yCoords);
        List<Double> lengths = calculateLengths(xCoords, yCoords);
        int n = headings.size();
        for (int i = 0; i < n; i++) {
            turtle.turn(headings.get(i));
            turtle.forward(lengths.get(i));
        }
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();
        List<Integer> xCoords = Arrays.asList(0, 
                10, 25, 45, 70, 95, 120, 140, 155, 165, 170, 165, 155, 140, 120, 95, 70, 45, 25, 10, 
                15, 35, 55, 75, 90, 95, 90, 75, 55, 35, 15, 
                0, 
                -10, -25, -45, -70, -95, -120, -140, -155, -165, -170, -165, -155, -140, -120, -95, -70, -45, -25, -10,
                -15, -35, -55, -75, -90, -95, -90, -75, -55, -35, -15, 
                0 
        );
        List<Integer> yCoords = Arrays.asList(0, 
                200, 210, 218, 223, 225, 223, 218, 210, 195, 170, 140, 105, 70, 35, 0, -30, -55, -75, -85, 
                -75, -55, -30, 0, 35, 70, 105, 140, 170, 195, 205, 
                0, 
                200, 210, 218, 223, 225, 223, 218, 210, 195, 170, 140, 105, 70, 35, 0, -30, -55, -75, -85, 
                -75, -55, -30, 0, 35, 70, 105, 140, 170, 195, 205,
                0 
        );
        drawPersonalArt(turtle, xCoords, yCoords);
        // draw the window
        turtle.draw();
    }

}
