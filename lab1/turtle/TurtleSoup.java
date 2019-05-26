/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        for(int i=0;i<4;i++) {
        	turtle.forward(sideLength);
        	turtle.turn(90.0);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
    	double angle=0;
        if(sides>2) {
        	angle = ((sides-2)*(180.0))/sides;
        }
        else System.out.println("There is no polygon at all");
        return angle;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        int sides = 0;
        sides = (int)Math.round(2/(1 - angle/180));
        return sides;
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double angle = (double)180.0-calculateRegularPolygonAngle(sides);
        for(int i=0;i<sides;i++) {
        	turtle.forward(sideLength);
        	turtle.turn(angle);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
    	double angle=0.0;	// default up
        if(currentX==targetX) {
        	if(currentY>=targetY)angle=180.0;	// down
        }
        else if(currentY==targetY) {
        	if(currentX>=targetX)angle=270.0;	//right
        	else angle = 90.0;	// left
        }
        else {
        	double distY = targetY-currentY, distX = targetX-currentX;
        	angle = Math.toDegrees(Math.atan2(distY, distX));
        	if(distY>0&&distX<0) angle = 360.0 - angle;
        	else angle = 90.0-angle;
        }
        angle -= currentBearing;
        if (angle < 0) angle += 360.0;
        return angle;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> res = new ArrayList<>();
        int n = xCoords.size();
        double bearing = 0.0;
        for(int i=0;i<n-1;i++) {
        	bearing = calculateBearingToPoint(bearing,xCoords.get(i),yCoords.get(i),xCoords.get(i+1),yCoords.get(i+1));
        	res.add(bearing);
        }
        return res;
    }
    
    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        if(points.size()<4)return points;
        Set<Point> res = new HashSet <Point> ();
        Point curr,temp,next,stan;
        int visited= 0 ;
        temp = new Point(1,1);
        Iterator<Point> p = points.iterator();
        while(p.hasNext()) {
        	curr = p.next();
        	if(visited == 0) {
        		temp = curr;
        		visited++;
        		continue;
        	}
        	if(curr.y()<temp.y()||(curr.y()==temp.y()&&curr.x()>temp.x())) temp = curr;
        }
        stan = temp;
        curr = temp;
        next = temp;
        while(true) {
        	p = points.iterator();
        	visited = 0;
        	while(p.hasNext()) {
        		if(visited == 0) {
        			curr = p.next();
        			visited++;
        			continue;
        		}
        		next = p.next();
        		double c = cross(curr,temp, next);
        		if(c < 0) curr = next;
        		else if(c == 0) {
        			if(distance(curr,temp)<distance(next,temp))curr = next;
        		}
        	}
        	visited++;
        	temp = curr;
        	res.add(curr);
        	if(temp==stan)break;
        }
        return res;
    }
    
    // to calculate the cross product
    public static double cross(Point a, Point b, Point c) {
    	return ((a.x()-b.x())*(b.y()-c.y())-(a.y()-b.y())*(b.x()-c.x()));
    }
    
    // to calculate the distance between point a and point b
    public static double distance(Point a, Point b) {
    	return (Math.hypot((a.x() - b.x()), (a.y() - b.y())));
    }
    
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
    	int len = 30;
        for(int j=0;j<36;j++) {
        	if(j%3==0)turtle.color(PenColor.PINK);
        	else if(j%3==1) turtle.color(PenColor.ORANGE);
        	else turtle.color(PenColor.MAGENTA);
        	for(int k=0;k<5;k++) {
        		turtle.forward(len);
        		turtle.turn(90.0);
        	}
        	len+=3;
        	turtle.turn(10);
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
        //drawSquare(turtle, 50);
        //drawRegularPolygon(turtle,5,50);
        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
    }

}
