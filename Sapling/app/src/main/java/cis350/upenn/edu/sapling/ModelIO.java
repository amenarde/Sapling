package cis350.upenn.edu.sapling;

//@author: amenarde

import java.io.BufferedWriter;
import java.io.File;
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
        // TODO
        return false;
    }

    public boolean hasinactiveMetric(String s) {
        // TODO
        return false;
    }

    public boolean hasActiveGoal(String s) {
        // TODO
        return false;
    }

    public boolean hasinactiveGoal(String s) {
        // TODO
        return false;
    }

    public void updateFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
        if (this.dm.getActiveGoals().size() > 0) {
            writer.write("Active Goals: \n");
            writeContent(writer, this.dm.getActiveGoals());
        }
        if (this.dm.getinactiveGoals().size() > 0) {
            writer.write("Inactive Goals: \n");
            writeContent(writer, this.dm.getinactiveGoals());
        }
        if (this.dm.getActiveMetrics().size() > 0) {
            writer.write("Active Metrics: \n");
            writeContent(writer, this.dm.getActiveMetrics());
        }
        if (this.dm.getinativeMetrics().size() > 0) {
            writer.write("Inactive Metrics: \n");
            writeContent(writer, this.dm.getinativeMetrics());
        }
        writer.close();
    }

    public void writeContent(BufferedWriter writer, HashSet<String> content) throws IOException {
        for (String s : content) {
            writer.write(s + "\n");
        }
    }

}
