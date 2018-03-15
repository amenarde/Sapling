package cis350.upenn.edu.sapling;

//@author: juezhou

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// I/O class under DataModel that maintains a persistent file for all currently tracked metrics
public class ModelIO {
    private File file;
    private static DataModel dm;

    public ModelIO (String filePath, DataModel dm) {
        this.file = new File(filePath);
        if (this.file == null) { throw new IllegalArgumentException("null argument"); }
        if(!this.file.exists() || !this.file.isFile()) {
            throw new IllegalArgumentException("file does not exist");
        }
        this.dm = dm;
    }

    public boolean hasActiveMetric(String s) {
        return fileCheckHelper(s,"Active Goals");
    }

    public boolean hasinactiveMetric(String s) {
        return fileCheckHelper(s,"Inactive Goals");
    }

    public boolean hasActiveGoal(String s) {
        return fileCheckHelper(s,"Active Metrics");
    }

    public boolean hasinactiveGoal(String s) {
        return fileCheckHelper(s, "Inactive Metrics");
    }

    // helper method that parses through the data file on disk
    // inputs: s - target string to check contains for, category - category of metric/goals
    public boolean fileCheckHelper(String s, String category) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.file));
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
    public void updateFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
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

}
