/*************************************************************************
 *  Compilation:  javac GrahamaScan.java
 *  Execution:    java GrahamScan < input.txt
 *  Dependencies: Point2D.java
 * 
 *  Create points from standard input and compute the convex hull using
 *  Graham scan algorithm.
 *
 *  May be floating-point issues if x- and y-coordinates are not integers.
 *
 *************************************************************************/

import java.lang.Object;
import java.util.Arrays;
import java.util.Stack;
import java.util.*;

public class GrahamScan {
    private Stack<Point2D> hull = new Stack<Point2D>();
  //  private Stack<Point2D> PointsSet = new Stack<Point2D>();

    public static int num_iter_n;
    
    public GrahamScan(ArrayList<Point2D> pts) {

        // defensive copy
        int N = pts.size();
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++)
            points[i] = pts.get(i);
        
        // preprocess so that points[0] has lowest y-coordinate; break ties by x-coordinate
        // points[0] is an extreme point of the convex hull
        // (alternatively, could do easily in linear time)
        Arrays.sort(points);

        // sort by polar angle with respect to base point points[0],
        // breaking ties by distance to points[0]
        Arrays.sort(points, 1, N, points[0].POLAR_ORDER);

        hull.push(points[0]);       // p[0] is first extreme point

        // find index k1 of first point not equal to points[0]
        int k1;
        for (k1 = 1; k1 < N; k1++)
            if (!points[0].equals(points[k1])) break;
        if (k1 == N) return;        // all points equal

        // find index k2 of first point not collinear with points[0] and points[k1]
        int k2;
        for (k2 = k1 + 1; k2 < N; k2++)
            if (Point2D.ccw(points[0], points[k1], points[k2]) != 0) break;
        hull.push(points[k2-1]);    // points[k2-1] is second extreme point

        // Graham scan; note that points[N-1] is extreme point different from points[0]
        for (int i = k2; i < N; i++) {
        	
        	//num_iter_n++;
        	
            Point2D top = hull.pop();
            
            while (Point2D.ccw(hull.peek(), top, points[i]) <= 0) {
                top = hull.pop();
               // num_iter_n++;
            }
            
            hull.push(top);
            hull.push(points[i]);
        }

        assert isConvex();
    }

    // return extreme points on convex hull in counterclockwise order as an Iterable
    public Iterable<Point2D> hull() {
        Stack<Point2D> s = new Stack<Point2D>();
        for (Point2D p : hull) s.push(p);
        return s;
    }

    // check that boundary of hull is strictly convex
    private boolean isConvex() {
        int N = hull.size();
        if (N <= 2) return true;

        Point2D[] points = new Point2D[N];
        int n = 0;
        for (Point2D p : hull()) {
            points[n++] = p;
        }

        for (int i = 0; i < N; i++) {
            if (Point2D.ccw(points[i], points[(i+1) % N], points[(i+2) % N]) <= 0) {
                return false;
            }
        }
        return true;
    }

    // test client
    public static void main(String[] args) {
    	
    	//ititizlie **********
    	num_iter_n = 0;
    	int time_counter = 0;
    	long time_start = 0, time_stop = 0, total_exe_time, start_time_program, end_time_program, exe_time_program;
    	
    	start_time_program = System.currentTimeMillis();
    	
    	
    	double avg_iterations, avg_exe_time;
    
     	int[] Nvalues = {20,50,100,200,500,1000,2000,3000};
        //int[] Nvalues = {10};
     	
     	long[] exe_time = new long[Nvalues.length];
     	
     	for (int k =0; k< Nvalues.length; k++)
     	{
     		total_exe_time = 0;
     		avg_iterations = 0.0;
	     for (int m = 0; m < 250; m++){	
	    	//int N = StdIn.readInt();				//number of points in the graph
	        int N = Nvalues[k];
	     	//System.out.println("The value of N is: "+ N);
	     	
	     	//Point2D[] points = new Point2D[N];
	     	ArrayList<Point2D> points2 = new ArrayList<Point2D>();
	     	
	     	time_start = System.currentTimeMillis();
	        
	     	for (int i = 0; i < N; i++) {			// for each point it has a x an y coordinate 
	            //int x = StdIn.readInt();
	           // int y = StdIn.readInt();
	        	float x = randomcoordinate();
	            float y = randomcoordinate();
	        	points2.add(new Point2D(x, y));		// add the point to Point2D
	        	//System.out.println(x*100+","+ y*100);
	        }
	     	
	     	/*While points not empty
	     	 *  run graphham scan on the points 
	     	 *  increment the counter 
	     	 *  delete points from the points array the same points in graham array
	     	 */
	     	
	     	 while(points2.size() != 0)
	     	 {
	     		GrahamScan graham = new GrahamScan(points2);			//give GrahamScan the list of points 
	   
	     		int count = 0;	
	     		for (Point2D p : graham.hull())
	     		{
	     	       
	     			for(int h = 0; h < points2.size(); h++)
	     			{
	     			
	     				if(p.equals( points2.get(h)))
	     				{
	     				    points2.remove(h);	
	     				}	
	     					     				
	     			}
	  		
	     		}
	     	
	     		num_iter_n++; 		
	     	 }
	     	time_stop = System.currentTimeMillis();
     		exe_time[time_counter] = time_stop - time_start;
     		avg_iterations += (double) num_iter_n;
     		total_exe_time +=exe_time[time_counter];
     		num_iter_n = 0;
	     	}
	     	avg_iterations = (double) avg_iterations / 250;
	     	avg_exe_time = (double) total_exe_time / 250;
	     	time_counter++;
	       // GrahamScan graham = new GrahamScan(points);			//give GrahamScan the list of points 
	       // System.out.println("The following are the convexhull points:");
	        //for (Point2D p : graham.hull())
	         //   StdOut.println(p);			// print out the convexhull coordinates
	     	//System.out.println("The number of points: "+Nvalues[time_counter]);
	        System.out.format("The total avg number of iterations: %f\n",(float) avg_iterations);
	        //StdOut.println(num_iter_n);
	        System.out.format("Total avg execution time: %f\n",(float) avg_exe_time);
     	}
     	
     	end_time_program = System.currentTimeMillis();
     	exe_time_program = end_time_program - start_time_program;
     	System.out.format("Program execution time: %f\n",(float) exe_time_program);
    }
    
    
    
    public static float randomcoordinate()
    {
    	Random generator = new Random();
    	float r = generator.nextFloat();
    	//System.out.println("The value of r is: "+ r);
    	return r;
    }
    
    
    /*
     * 
     * 
       0,2			2,2

	  		0,1

	   0,0			2,0
     
     */

}
