/* 
 * Takes as input path to file, calculates total time between login and logout for each unique user and outputs it in reversed order.
 * Assumpitons: 
 *   - logfile has structure unix_time_stamp, user_id, action (e.g. 123456, 12, login), sorted by timestamp
 *   - user always logs out before logging in again
 *   - all users who logged in, logged out in the log data (and vice versa)
 *   - logfile actions only contain login and logout actions
 *   - will be used > 20 years => should use long for storing time spent logged in
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogParser {
    
    private static class User {
        int id;
        long timeSpent;             
        public User(int id, long timeSpent) {
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
            return Long.compare(b.timeSpent, a.timeSpent); //reversed order comparator
        }
    }    
    
    private static HashMap<Integer, User> readLog (String FILE_NAME) {
        int id;
        long time;
        String action;
        String LOGIN = "login";
        String CHARSET = "UTF-8";
        HashMap<Integer, User> times = new HashMap<>();
        
        Charset charset = Charset.forName(CHARSET);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_NAME), charset)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] splits = line.split(",\\p{javaWhitespace}");
                time = Long.parseLong(splits[0]);
                id = Integer.parseInt(splits[1]);
                action = splits[2];
                if (action.equals(LOGIN))
                    time *= -1; 
                if (times.containsKey(id))
                    times.get(id).timeSpent += time;
                else
                    times.put(id, new User(id, time));
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return times; 
    }
    public static void main(String[] args) {
        try {
            String FILE_NAME = args[0];
            int SEC2HOURS = 60*60;
            HashMap<Integer, User> times = readLog(FILE_NAME);
            ArrayList<User> timesSorted = new ArrayList<>(times.values());
            Collections.sort(timesSorted, new timeComparator());
            System.out.println("Results (id, time spent in seconds, time spent in hours(approx)):");
            for (User u : timesSorted) 
                // since we assume that everyone logs out, do not check here for negative total times
                System.out.println(u + ",  " + u.timeSpent/SEC2HOURS);            
        }
        catch (Exception e) {
            System.out.println("Error: Path to file undetermined.\n" +
                    "Please, start with path to log file as command-line parameter. E.g.:\n" +
                    "java LogParser /home/username/log\n");
        }
    }
}