package cis350.upenn.edu.sapling;

//@author: amenarde

import java.io.File;
import java.util.Set;

public class ModelIO {
    private File file;

    public ModelIO (String fileName) {
        if (file == null) { throw new IllegalArgumentException("null argument"); }
        this.file = new File(fileName);
        if(!this.file.exists() || !this.file.isFile()) {
            throw new IllegalArgumentException("file does not exist");
        }
    }

    public boolean add() {
        return false;
    }

    public boolean has(Object o) {
        return false;
    }

    public Set toSet() {
        return null;
    }

    
    public void fetch(DataModel dm) {

    }
}
