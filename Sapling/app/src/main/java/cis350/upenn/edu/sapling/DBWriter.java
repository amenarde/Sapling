package cis350.upenn.edu.sapling;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// @author: amenarde

class DBWriter implements FileDictionary<Date, DayData>{
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
    private String path;

    public DBWriter(String path) {
        if (path == null) { throw new IllegalArgumentException("null argument"); }
        if(!(new File(path)).exists() || !(new File(path)).isDirectory()) {
            throw new IllegalArgumentException("directory does not exist");
        }
        if(!path.endsWith("/")) {
            path += "/";
        }
        this.path = path;
    }

    public boolean put(Date date, DayData dayData) {
        if (dayData == null) { throw new IllegalArgumentException("null argument"); }
        String filepath = dateToFilename(date);

        try {
            PrintWriter writer = new PrintWriter(filepath);
            writer.append(DayDataToJSON(dayData));
            writer.close();

            return true;
        }
        catch (FileNotFoundException e) {
            //TODO: log and quit
            return false;
        }
    }

    // Returns null if day does not exist in system
    public DayData get(Date date) {
        if (date == null) { throw new IllegalArgumentException("null argument"); }

        String filepath = path + dateToFilename(date);
        File day = new File(filepath);
        if (!day.exists()) {
            return null;
        }

        try {
            String content = new Scanner(day).useDelimiter("\\Z").next();

            Gson gson = new Gson();
            return DayDataFromJSON(content);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public boolean has(Date date) {
        if (date == null) { throw new IllegalArgumentException("null argument"); }

        File[] filesList = new File(path).listFiles();
        String wantedFile = dateToFilename(date);

        for (File f : filesList) {
            if (wantedFile.equals(f.getPath())) {
                return true;
            }
        }

        return false;
    }

    public DayData remove(Date date) {
        if (date == null) { throw new IllegalArgumentException("null argument"); }

        DayData dayData = get(date);
        if(dayData == null) { return null; }

        File toDelete = new File(dateToFilename(date));
        toDelete.delete();
        return dayData;
    }

    public Set<Date> getKeySet() {
        File[] filesList = new File(path).listFiles();
        Set<Date> dates = new HashSet<Date>(filesList.length);

        for (File f : filesList) {
            String date = f.getPath();
            dates.add(filenameToDate(date));
        }

        return dates;
    }

    public Set<DayData> getValueSet() {
        File[] filesList = new File(path).listFiles();
        Set<DayData> dayData = new HashSet<DayData>(filesList.length);

        for (File f : filesList) {
            Date date = filenameToDate(f.getPath());
            dayData.add(get(date));
        }

        return dayData;
    }

    private String dateToFilename(Date date) {
        return path + this.sdf.format(date) + ".JSON";
    }

    private Date filenameToDate(String filename) {
        filename.replace(this.path, "");
        filename.replace(".JSON", "");
        try {
            return this.sdf.parse(filename);
        } catch (ParseException e){
            //TODO: LOG / assert false
            return null;
        }

    }

    private String DayDataToJSON(DayData dayData) {
        Gson gson = new Gson();
        return gson.toJson(dayData);
    }

    private DayData DayDataFromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DayData.class);
    }

}
