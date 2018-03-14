package cis350.upenn.edu.sapling;

//@author: amenarde

public class DataManager {
    private static DataManager dataManager;

    private DataModel dataModel;
    private DBWriter dbWriter;

	private DataManager() {
        dataModel = DataModel.getinstance();
        dbWriter = new DBWriter("./data/");
    }

    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }

        return dataManager;
    }
}
