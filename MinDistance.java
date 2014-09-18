import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MinDistance { 
    
    public static void main(String[] args) throws FileNotFoundException {   
        Scanner in = new Scanner(new File("inputDistance.txt")); 
        ArrayList<Integer> x = new ArrayList<>();  
        ArrayList<Integer> y = new ArrayList<>();
        // read input 
        while (in.hasNext()) {
            x.add(in.nextInt());
            y.add(in.nextInt());
        }
        // compute min distance
        double Answer = Integer.MAX_VALUE;
        double Current;
        for (int i = 0; i < x.size(); i++) {
            for (int j = i + 1; j < x.size(); j++) {
                Current = Math.sqrt(Math.pow(x.get(i) - x.get(j), 2) + Math.pow(y.get(i)-y.get(j), 2));
                Answer = Math.min(Current, Answer);
            }
        }
        // printing results
        System.out.println(Answer);        
    }
}