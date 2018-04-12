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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// @author: amenarde

// this class represents the interface for a file-system database for the
// main data in the application

class DBWriter {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");

    // files are written at the top level directory in the format
    // YYYY-MM-dd.JSON, which consists of a json version of a DayData
    // @returns true if information is successfully written, false if writing fails
    // (quietly fails)
    public boolean put(Date date, DayData dayData, Context context) {
        if (dayData == null) {
            throw new IllegalArgumentException("DayData cannot be null");
        }

        String filepath = dateToFilename(date);

        try {

            // Replace the old DayData with a new one
            File maybeOldFile = new File(context.getFilesDir(), filepath);
            if (maybeOldFile.exists()) {
                maybeOldFile.delete();
            }

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filepath, Context.MODE_PRIVATE));
            outputStreamWriter.write(DayDataToJSON(dayData));
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    // reaches in to acquire the data, returns null if the DayData does not exist
    // note that by the time the DataManager hands back a null day it may hand it
    // back as an empty day rather than as null
    // parsing failures return as null
    public DayData get(Date date, Context context) {
        if (date == null) {
            throw new IllegalArgumentException("Date can not be null");
        }

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
        if (date == null) {
            throw new IllegalArgumentException("Date can not be null");
        }

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
        if (date == null) {
            throw new IllegalArgumentException("Date can not be null");
        }

        DayData dayData = get(date, context);
        if (dayData == null) {
            return null;
        }
        else {
            File oldFile = new File(context.getFilesDir(), dateToFilename(date));
            if (oldFile.exists()) {
                oldFile.delete();
            }
            return dayData;
        }

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

    // uses standard format YYYY-MM-DD.JSON
    private String dateToFilename(Date date) {
        return this.sdf.format(date) + ".JSON";
    }

    // removes the .JSON and uses simple date format to get back to a date
    private Date filenameToDate(String filename) {
        filename.replace(".JSON", "");
        try {
            return this.sdf.parse(filename);
        } catch (ParseException e){
            //TODO: LOG / assert false
            return null;
        }

    }

    // leverages Gson to convert to json, so it is agnostic to specifics;
    // this however may mean if DayData is changed significantly old stored data will need an
    // adapter to work
    private String DayDataToJSON(DayData dayData) {
        Gson gson = new Gson();
        return gson.toJson(dayData);
    }

    private DayData DayDataFromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DayData.class);
    }

}
