/* 
 * Takes as input path to file, calculates total time between login and logout for each unique user and outputs it in reversed order.
 * Assumpitons: 
 *   - logfile has structure unix_time_stamp, user_id, action (e.g. 123456, 12, login)
 *   - user always logs out before logging in again
 *   - all users who logged in, logged out
 *   - logfile actions only contain login and logout actions
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class LogParser {
    
    private static class User {
        int id;
        int timeSpent; // since 1)current unix time is around 1.4mln, 2)10 years ~ 310k of seconds, 3)everyone logs out before logging in again, int should suffice            
        public User(int id, int timeSpent) {
            this.id = id;
            this.timeSpent = timeSpent;
        }        
        @Override
        public String toString() {
            return id + ",  " + timeSpent;
        }
    }
    
    private static class timeComparator implements Comparator<User> {
        @Override
        public int compare(User a, User b) {
            return b.timeSpent - a.timeSpent; // reversed order comparator
        }
    }    
    
    private static HashMap<Integer, User> readLog (String FILE_NAME) throws FileNotFoundException {
        int id;
        int time;
        String action;
        String LOGIN = "login";
        HashMap<Integer, User> times = new HashMap<>();
        Scanner input = new Scanner(new File(FILE_NAME)); 
        Scanner line;
        
        while (input.hasNextLine()) {
            line = new Scanner(input.nextLine());
            line.useDelimiter(",\\p{javaWhitespace}");
            time = line.nextInt();
            id = line.nextInt();
            action = line.next();
            if (action.equals(LOGIN))
                time *= -1; 
            if (times.containsKey(id))
                times.get(id).timeSpent += time;
            else
                times.put(id, new User(id, time));
        }
        return times; 
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        try {
            String FILE_NAME = args[0];
            int SEC2HOURS = 60*60;
            HashMap<Integer, User> times = readLog(FILE_NAME);
            ArrayList<User> timesSorted = new ArrayList<>(times.values());
            Collections.sort(timesSorted, new timeComparator());
            System.out.println("Results (id, time spent in seconds, time spent in hours(approx)):");
            for (User u : timesSorted) 
                // since we assume that everyone logs out, do not check here for negative total times
                System.out.println(u + ",  " + (int) u.timeSpent/SEC2HOURS);            
        }
        catch (Exception e) {
            System.out.println("Error: Path to file undetermined.\n" +
                    "Please, start with path to log file as command-line parameter. E.g.:\n" +
                    "java LogParser /home/username/log\n");
            e.printStackTrace();
        }
    }
}