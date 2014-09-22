/*  
*   Reads input from file inputDistance.txt
*   Solving closest pair distance problem with with O(n*logn) divide and conquer algoritm or bruteforcing (for small n).
*   Given array of point's coordinates is first sorted by x and by y coordinates. In the main function points are divided by half according to their X coordinate and problem is recursevly solved on both halves of input. After finding current minimum distance on left anf right parts, splitPairDistance function finds smallest distance between pairs in different halves - delta. It compares only points from different sides which x coordinate is within delta distance from middle x coordinate which divided two halves (because other points are further away from each other than already found minimum distance - delta). Furthermore, among such points only those are considered which y coordinates are also not further apart than delta.
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MinDistance { 
    
    private static class Point {
        
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
        
        public boolean equals(Point b) {
            return (x == b.x && y == b.y);
        }

    }
    
    private static class xComparator implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {
            return a.x - b.x;
        }
    }
    
    private static class yComparator implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {
            return a.y - b.y;
        }
    }
    
    private static double closestPairDistance(List<Point> inputX, List<Point> inputY) {
        if (inputX.size() < 2) return Double.MAX_VALUE;
        
        int middleIndex = (int) (inputX.size()-1)/2;
        int middleX = inputX.get(middleIndex).x;
        ArrayList<Point> inputYLeft = new ArrayList<>();
        ArrayList<Point> inputYRight = new ArrayList<>();
        for (Point p : inputY) {
            if (p.x <= middleX) inputYLeft.add(p);
            else inputYRight.add(p);
        }
        
        ArrayList<Point> inputXLeft = new ArrayList<>(inputX.subList(0, middleIndex + 1));
        ArrayList<Point> inputXRight = new ArrayList<>(inputX.subList(middleIndex + 1, inputX.size()));
        // if points to the right of the middle have same x coordinate as middle, such points will be included in inputYLeft, but should be additionally transfered from inputXRight to inputXLeft
        for (int i = middleIndex + 1; i < inputX.size(); i++) {
            if (inputX.get(i).x == middleX) {
                Point p = inputXRight.remove(0);
                inputXLeft.add(p);
            }
            else break;
        }
        
        // if right half is empty, last Point from the left half is transfered to the right half to ensure convergence of recursion
        if (inputXRight.isEmpty()) {
            Point p = inputXLeft.remove(inputXLeft.size()-1);
            inputXRight.add(p);
            inputYLeft.remove(p);
            inputYRight.add(p);
        }
        
        
        double delta;
        delta = closestPairDistance(inputXLeft, inputYLeft);
        delta = Math.min(delta, closestPairDistance(inputXRight, inputYRight));
        delta = splitPairDistance(inputYLeft, inputYRight, middleX, delta);
        return delta;
        
    }
    
    private static double splitPairDistance (List<Point> inputYLeft, List<Point> inputYRight, double middle, double delta) {
        ArrayList<Point> testL = new ArrayList<>(); 
        for (Point p : inputYLeft) {
            if (p.x >= middle - delta) testL.add(p);
        }
        ArrayList<Point> testR = new ArrayList<>(); 
        for (Point p : inputYRight) {
            if (p.x <= middle + delta) testR.add(p);
        }
        
        for (Point p : testL) {
            for (Point r : testR) {
                if (r.y < p.y) continue;
                if (r.y > p.y + delta) break;
                delta = Math.min(delta, euclidDistance(p, r));
            }
        }
        return delta;
    }
    
    private static double bruteforceAllPairs (ArrayList<Point> inputX) {
        double Answer = Double.MAX_VALUE;
        for (int i = 0; i < inputX.size(); i++) {
            for (int j = i + 1; j < inputX.size(); j++) {
                Answer = Math.min(euclidDistance(inputX.get(i), inputX.get(j)), Answer);
            }
        }
        return Answer;
    }
    
    private static double euclidDistance (Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y- b.y, 2));
    }
    
    public static void main(String[] args) throws FileNotFoundException {   
        Scanner in = new Scanner(new File("inputDistance.txt")); 
        ArrayList<Point> inputX = new ArrayList<>(); 
        int x;
        int y;
        if (!in.hasNextInt()) {
            throw new IllegalArgumentException("Wrong input!");
        }
        while (in.hasNext()) {
            if (!in.hasNextInt()) {
                throw new IllegalArgumentException("Wrong input!");
            }
            else {
                x = in.nextInt();
                if (!in.hasNextInt()) {
                    throw new IllegalArgumentException("Wrong input!");
                }
                else {
                    y = in.nextInt();
                    inputX.add(new Point(x,y));
                }
            }
        }
        ArrayList<Point> inputY = new ArrayList<>(inputX);
        
        Collections.sort(inputX, new xComparator());
        Collections.sort(inputY, new yComparator());
        
        double Answer = (inputX.size() <= 30) ? bruteforceAllPairs(inputX) : closestPairDistance(inputX, inputY);
//      double Answer = closestPairDistance(inputX, inputY);
//      double Answer = bruteforceAllPairs(inputX);
        
        System.out.println(Answer);        
    }
}



