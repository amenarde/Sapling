package cis350.upenn.edu.sapling;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

// @author: amenarde

class DBWriter {
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

    public boolean putDay(Day day) {
        if (day == null) { throw new IllegalArgumentException("null argument"); }
        String filepath = path + day.getDate().toString() + ".JSON";

        try {
            PrintWriter writer = new PrintWriter(filepath);
            writer.append(day.toJSON());
            writer.close();

            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }

    public Day getDay(LocalDate date) {
        if (date == null) { throw new IllegalArgumentException("null argument"); }

        String filepath = path + date.toString() + ".JSON";
        File day = new File(filepath);

        try {
            String content = new Scanner(day).useDelimiter("\\Z").next();

            Gson gson = new Gson();
            return gson.fromJson(content, Day.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public boolean hasDay() {
        // TODO: Empty method
        return false;
    }

    public Day removeDay() {
        // TODO: Empty method
        return null;
    }

    public List<Day> getDayList() {
        // TODO: Empty method
        return null;
    }
}
