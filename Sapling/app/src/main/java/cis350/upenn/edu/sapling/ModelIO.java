package cis350.upenn.edu.sapling;

//@author: juezhou

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import android.util.Log;
import android.content.Context;
import android.app.Activity;
import java.util.Set;
/*
    I/O class under DataModel that maintains a persistent file for
    all currently tracked / deprecated metrics

    ModelIO updates the txt file when the updateFile() function is called, and rewrites the txt
    file based on strings and metrics stored in the maps and sets within DataModel.
*/
public class ModelIO {
    private File file;
    private static DataModel dm;
    private static String modelFilePath;
    private static String path;


    public ModelIO (DataModel dm, String path) {
        if (path == null) { throw new IllegalArgumentException("null argument"); }
        if(!(new File(path)).exists() || !(new File(path)).isDirectory()) {
            throw new IllegalArgumentException("directory does not exist");
        }
        if(!path.endsWith("/")) {
            path += "/";
        }
        this.path = path;
        this.path = "";
        this.dm = dm;
        this.modelFilePath = "path.txt";
        File f = new File(this.modelFilePath);
        if (f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasActiveMetric(String s, Context c) {
        return fileCheckHelper(s,"Active Metrics", c);
    }

    public boolean hasinactiveMetric(String s, Context c) {
        return fileCheckHelper(s,"Inactive Metrics", c);
    }

    public boolean hasActiveGoal(String s, Context c) {
        return fileCheckHelper(s,"Active Goals", c);
    }

    public boolean hasinactiveGoal(String s, Context c) {
        return fileCheckHelper(s, "Inactive Goals", c);
    }

    // helper method that parses through the data file on disk
    // inputs: s - target string to check contains for, category - category of metric/goals
    public boolean fileCheckHelper(String s, String category, Context c) {
        File f = new File(c.getFilesDir(), modelFilePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String l;
            while ((l = br.readLine()) != null) {
                // searches within the target
                if (l.startsWith(category)) {
                    // read the segment
                    while ((l = br.readLine()) != null && l.length() != 0) {
                        if (l.contains(s)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // updates the file on disk by re-writing the file using the sets & maps in DataModel
    public void updateFile(Context c) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(c.getFilesDir(),
                                this.modelFilePath), false));
        if (this.dm.getActiveGoals().size() > 0) {
            writer.write("Active Goals: \n");
            writeGoalContent(writer, this.dm.getActiveGoals());
        }
        writer.write("\r\n");
        if (this.dm.getinactiveGoals().size() > 0) {
            writer.write("Inactive Goals: \n");
            writeGoalContent(writer, this.dm.getinactiveGoals());
        }
        writer.write("\r\n");
        if (this.dm.getActiveMetrics().size() > 0) {
            writer.write("Active Metrics: \n");
            System.out.println("ac");
            writeMetricContent(writer, this.dm.getActiveMetrics());
        }
        writer.write("\r\n");
        if (this.dm.getinactiveMetrics().size() > 0) {
            writer.write("Inactive Metrics: \n");
            System.out.println("Inac");
            writeMetricContent(writer, this.dm.getinactiveMetrics());
        }
        writer.write("\r\n");
        writer.close();
        System.out.println("done");
    }

    // helper method that writes the content of a set to a file
    public void writeMetricContent(OutputStreamWriter writer, Map<String, Metric> content) throws IOException {
        for (String s : content.keySet()) {
            Metric metric = content.get(s);
            System.out.println(metric.getName() + ", " + metric.getPositive() + "\n");
            writer.write(metric.getName() + ", " + metric.getPositive() + "\n");
        }
    }

    // helper method that writes the content of a set to a file
    public void writeGoalContent(OutputStreamWriter writer, Set<String> content) throws IOException {
        for (String s : content) {
            writer.write(s + "\n");
        }
    }
    
    public static void main(String[] args) {
        //ModelIO io = new ModelIO("./path.txt", DataModel.getInstance());
    }

}
