package cis350.upenn.edu.sapling;

// @author: amenarde

class DataModel {
    private static DataModel instance;

    static DataModel getinstance() {
        if (instance == null) {
            instance = new DataModel();
        }

        return instance;
    }

    private DataModel() {

    }

    class Model {

    }
}
