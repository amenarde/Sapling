package cis350.upenn.edu.sapling;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// @author: amenarde

class DBWriter {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");

    public boolean put(Date date, DayData dayData, Context context) {
        if (dayData == null) { throw new IllegalArgumentException("null argument"); }
        String filepath = dateToFilename(date);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filepath, Context.MODE_PRIVATE));
            outputStreamWriter.write(DayDataToJSON(dayData));
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    // Returns null if day does not exist in system
    public DayData get(Date date, Context context) {
        if (date == null) { throw new IllegalArgumentException("null argument"); }

        String filepath = dateToFilename(date);

        try {
            InputStream inputStream = new FileInputStream(new File(context.getFilesDir(), filepath));
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                String content = stringBuilder.toString();
                Gson gson = new Gson();
                return DayDataFromJSON(content);
            }
            else return null; //TODO throw errors
        }
        catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null; //TODO throw errors
        }

    }

    public boolean has(Date date, Context context) {
        if (date == null) { throw new IllegalArgumentException("null argument"); }

        File[] filesList = context.getFilesDir().listFiles();
        String wantedFile = dateToFilename(date);

        for (File f : filesList) {
            if (wantedFile.equals(f.getPath())) {
                return true;
            }
        }

        return false;
    }

    public DayData remove(Date date, Context context) {
        if (date == null) { throw new IllegalArgumentException("null argument"); }

        DayData dayData = get(date, context);
        if(dayData == null) { return null; }

        File toDelete = new File(dateToFilename(date));
        toDelete.delete();
        return dayData;
    }

    public Set<Date> getKeySet(Context context) {
        File[] filesList = context.getFilesDir().listFiles();
        Set<Date> dates = new HashSet<Date>(filesList.length);

        for (File f : filesList) {
            String date = f.getPath();
            dates.add(filenameToDate(date));
        }

        return dates;
    }

    public Set<DayData> getValueSet(Context context) {
        File[] filesList = context.getFilesDir().listFiles();
        Set<DayData> dayData = new HashSet<DayData>(filesList.length);

        for (File f : filesList) {
            Date date = filenameToDate(f.getPath());
            dayData.add(get(date, context));
        }

        return dayData;
    }

    private String dateToFilename(Date date) {
        return this.sdf.format(date) + ".JSON";
    }

    private Date filenameToDate(String filename) {
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
