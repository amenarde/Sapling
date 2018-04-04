package cis350.upenn.edu.sapling;

//@author: juezhou

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import android.util.Log;
import android.content.Context;
import android.app.Activity;

// I/O class under DataModel that maintains a persistent file for
// all currently tracked / deprecated metrics
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
        this.dm = dm;
        this.modelFilePath = path + "path.txt";
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
                        if (l.equals(s)) {
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

    // updates the file on disk by re-writing the file using the hashsets in DataModel
    public void updateFile(Context c) throws IOException {
        File f = new File(c.getFilesDir(), modelFilePath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        if (this.dm.getActiveGoals().size() > 0) {
            writer.write("Active Goals: \n");
            writeContent(writer, this.dm.getActiveGoals());
        }
        writer.write("\r\n");
        if (this.dm.getinactiveGoals().size() > 0) {
            writer.write("Inactive Goals: \n");
            writeContent(writer, this.dm.getinactiveGoals());
        }
        writer.write("\r\n");
        if (this.dm.getActiveMetrics().size() > 0) {
            writer.write("Active Metrics: \n");
            writeContent(writer, this.dm.getActiveMetrics());
        }
        writer.write("\r\n");
        if (this.dm.getinativeMetrics().size() > 0) {
            writer.write("Inactive Metrics: \n");
            writeContent(writer, this.dm.getinativeMetrics());
        }
        writer.write("\r\n");
        writer.close();
    }

    // helper method that writes the content of a set to a file
    public void writeContent(BufferedWriter writer, HashSet<String> content) throws IOException {
        for (String s : content) {
            writer.write(s + "\n");
        }
    }
    
    public static void main(String[] args) {
        //ModelIO io = new ModelIO("./path.txt", DataModel.getInstance());
    }

}
