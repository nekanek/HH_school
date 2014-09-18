import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Knapsack { 
    
    private ArrayList<Integer> itemsInKnapsack; // indexes of items in Knapsack
    private String txtAnswer = "";
    
    private void fillKnapsack(int W, ArrayList<Integer> values, ArrayList<Integer> weight) {
        int n = values.size(); // number of items
        int[] solution = new int[W+1];// array of solution we fill
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listOfItems = (ArrayList<Integer>[])new ArrayList<?>[W+1];
        for (int i = 0; i < listOfItems.length; i++) {
            listOfItems[i] = new ArrayList<>();
        }        

        for (int i = 0; i < n; i++) {
            for (int x = W; x >= 0; x--) {
                if (weight.get(i) > W || weight.get(i) > x) {
                }
                else {
                    if (solution[x] <= solution[x-weight.get(i)] + values.get(i)) {
                        listOfItems[x] = new ArrayList<>();
                        listOfItems[x].addAll(listOfItems[x-weight.get(i)]);
                        listOfItems[x].add(i);
                        solution[x] = solution[x-weight.get(i)] + values.get(i);
                    }
                }
            }
        }
        
        int result = solution[W];        
        if (result != W) { txtAnswer = "no";}
        else {
            for (Integer i : listOfItems[W]) {
                txtAnswer += weight.get(i) + " ";
            }
        }
        itemsInKnapsack = listOfItems[W];
    }
    
    public static void main(String[] args) throws FileNotFoundException {   
        Scanner in = new Scanner(new File("input.txt")); 
        ArrayList<Integer> values = new ArrayList<>();  
        ArrayList<Integer> weight = new ArrayList<>();
        // read input 
        int input;
        while (in.hasNext()) {
            input = in.nextInt();
            values.add(input);
            weight.add(input);
        }
        // compute total weight of items
        int totalWeight = 0;
        for (Integer i : weight) {
           totalWeight += i; 
        }
        // testing whether items can be balanced
        Knapsack balancedKnapsack = new Knapsack();
        if (totalWeight % 2 != 0) {balancedKnapsack.txtAnswer = "no";}
        else {
            int W = totalWeight/2;
            balancedKnapsack.fillKnapsack(W, values, weight);
        }
        // testing whether items can be chosen to equal 100
        Knapsack knapsack100 = new Knapsack();
        if (totalWeight < 100) {knapsack100.txtAnswer = "no";}
        else {
            int W = 100;
            knapsack100.fillKnapsack(W, values, weight);
        }
        // printing results
        // balanced
        if (!"no".equals(balancedKnapsack.txtAnswer)) {
            balancedKnapsack.txtAnswer += "- ";
            int index = 0;
            int itemIndex = balancedKnapsack.itemsInKnapsack.get(index);
            for (int i = 0; i < weight.size(); i++) {
                if (i == itemIndex) {
                    if (index < balancedKnapsack.itemsInKnapsack.size() - 1)
                    itemIndex = balancedKnapsack.itemsInKnapsack.get(++index);
                }
                else {
                    balancedKnapsack.txtAnswer += weight.get(i) + " ";
                }
            }
        }
        System.out.println(balancedKnapsack.txtAnswer);
        // 100
        if (!"no".equals(knapsack100.txtAnswer)) {
            knapsack100.txtAnswer = "yes";
        }
        System.out.println(knapsack100.txtAnswer);        
    }
}