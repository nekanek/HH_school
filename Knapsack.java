import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/*  Given task can be solved with the algorythm for solving Knapsack problem. In this particular case weights of items (constraints) equal tehir value (parameter to be maximized). Function fillKnapsack finds set of items which maximizes values given the constraint (in the first case we should fill it half the weight of all items given, in the second - maxWeight equals 100).Since weight equals value for each particular item, fillKnapsack only takes weights of items which act both as weights and as values of items.
*   In class Knapsack field itemsInKnapsack stores indexes of items which maximize sum of their values given maxWeight of the knapsack. Indexes correspond to the elements of ArrayList of input values. Solution[] array hold max value found for the knapsack of weight x, where x is array index.
*   
*/ 
public class Knapsack { 
    
    private ArrayList<Integer> itemsInKnapsack; 
    private String txtAnswer = "";
    private int maxWeight;
    
    private void fillKnapsack (ArrayList<Integer> weight) {
        int n = weight.size(); 
        int[] solution = new int[maxWeight+1];
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listOfItems = (ArrayList<Integer>[])new ArrayList<?>[maxWeight+1];
        for (int i = 0; i < listOfItems.length; i++) {
            listOfItems[i] = new ArrayList<>();
        }        

        for (int i = 0; i < n; i++) {
            for (int x = maxWeight; x >= 0; x--) {
                // check whether item will fit into knapsack of size x
                if (weight.get(i) > maxWeight || weight.get(i) > x) {
                }
                else {
                    // check whether we should add item i (compare value of items of knapsack of lesser weight plus item i and value of knapsack of given weight without item i)
                    if (solution[x] <= solution[x-weight.get(i)] + weight.get(i)) {
                        listOfItems[x] = new ArrayList<>();
                        listOfItems[x].addAll(listOfItems[x-weight.get(i)]);
                        listOfItems[x].add(i);
                        solution[x] = solution[x-weight.get(i)] + weight.get(i);
                    }
                }
            }
        }
        
        int result = solution[maxWeight];        
        if (result != maxWeight) { txtAnswer = "no";}
        else {
            for (Integer i : listOfItems[maxWeight]) {
                txtAnswer += weight.get(i) + " ";
            }
        }
        itemsInKnapsack = listOfItems[maxWeight];
    }
    
    public static void main(String[] args) throws FileNotFoundException {   
        Scanner in = new Scanner(new File("input.txt")); 
        ArrayList<Integer> weight = new ArrayList<>();
        
        int input;
        if (!in.hasNextInt()) {
            throw new IllegalArgumentException("Wrong input!");
        }
        while (in.hasNext()) {
            if (!in.hasNextInt()) {
                throw new IllegalArgumentException("Wrong input!");
            }
            else {
                input = in.nextInt();
                if (input < 1) {
                    throw new IllegalArgumentException("Only natural numbers allowed (1, 2, 3..).");
                }
                else {
                    weight.add(input);
                }
            }
        }

        int totalWeight = 0;
        for (Integer i : weight) {
           totalWeight += i; 
        }

        Knapsack balancedKnapsack = new Knapsack();
        if (totalWeight % 2 != 0) {balancedKnapsack.txtAnswer = "no";}
        else {
            balancedKnapsack.maxWeight = totalWeight/2;
            balancedKnapsack.fillKnapsack(weight);
        }

        Knapsack knapsack100 = new Knapsack();
        if (totalWeight < 100) {knapsack100.txtAnswer = "no";}
        else {
            knapsack100.maxWeight = 100;
            knapsack100.fillKnapsack(weight);
        }

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

        if (!"no".equals(knapsack100.txtAnswer)) {
            knapsack100.txtAnswer = "yes";
        }
        System.out.println(knapsack100.txtAnswer);        
    }
}