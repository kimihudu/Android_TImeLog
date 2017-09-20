package dev.android.timelog;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static android.content.ContentValues.TAG;
import static dev.android.timelog.BuildConfig.DEBUG;

/**
 * Created by GeorgyGeo on 6/21/2017.
 */

public class Data {


    private String nameFile;
    private String contentFile = "";
    private Context context;

    public Data() {
    }

    public Data(Context context, String nameFile) {

        if (nameFile.indexOf("@") > 0) {
            nameFile = nameFile.substring(0, nameFile.indexOf("@"));
        }
        this.nameFile = nameFile;
        this.context = context;
    }

    public Data(Context context, String nameFile, String contentFile) {

        if (nameFile.indexOf("@") > 0) {
            nameFile = nameFile.substring(0, nameFile.indexOf("@"));
        }
        this.context = context;
        this.nameFile = nameFile;
        this.contentFile = contentFile;
    }

    public Data(Context context, String nameFile, Object model) {

        if (nameFile.indexOf("@") > 0) {
            nameFile = nameFile.substring(0, nameFile.indexOf("@"));
        }
        this.context = context;
        Gson gson = new Gson();
        this.nameFile = nameFile;
        this.contentFile = gson.toJson(model);
    }

    //    TODO: read file then convert content to obj
    public Object getData(Class model) {
        Object jsonData = null;

        File path = context.getExternalFilesDir(null);
        File file = new File(path, nameFile + ".json");

        InputStream inputStream = null;
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
                .create();
        try {
            inputStream = new FileInputStream(file);
            InputStreamReader streamReader;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                streamReader = new InputStreamReader(inputStream,
                        StandardCharsets.UTF_8);
            } else {
                streamReader = new InputStreamReader(inputStream, "UTF-8");
            }

            jsonData = gson.fromJson(streamReader, model);
            streamReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (DEBUG) Log.e(TAG, "loadJson, FileNotFoundException e: '" + e + "'");
        } catch (IOException e) {
            e.printStackTrace();
            if (DEBUG) Log.e(TAG, "loadJson, IOException e: '" + e + "'");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    if (DEBUG) Log.e(TAG, "loadJson, finally, e: '" + e + "'");
                }
            }
        }
        return jsonData;
    }

    //    TODO: setData by write file
    public boolean setData(Object obj) {
        boolean flag = false;
        File path = context.getExternalFilesDir(null);
        File file = new File(path, nameFile + ".json");
        OutputStream outputStream = null;
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
                .create();
        try {
            outputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,
                        StandardCharsets.UTF_8));
            } else {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            }

            gson.toJson(obj, bufferedWriter);
            bufferedWriter.close();
            flag = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (DEBUG) Log.e("SAVE_JSON", "saveUserData, FileNotFoundException e: '" + e + "'");
        } catch (IOException e) {
            e.printStackTrace();
            if (DEBUG) Log.e("SAVE_JSON", "saveUserData, IOException e: '" + e + "'");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    if (DEBUG) Log.e("SAVE_JSON", "saveUserData, finally, e: '" + e + "'");
                }
            }
        }

        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);
        return flag;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        if (nameFile.indexOf("@") > 0) {
            nameFile = nameFile.substring(0, nameFile.indexOf("@"));
        }

        this.nameFile = nameFile;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }


    // TODO:    check file exist or not
    public Boolean isFileExist(String nameFile) {
        File path = context.getExternalFilesDir(null);
        File file = new File(path, nameFile + ".timelog");

        return file.exists();
    }

    public String time2String(DateTime time, String format){

        String _stringTime = "";
        DateTimeFormatter sdf = DateTimeFormat.forPattern(format);
        _stringTime = sdf.print(time);
        return _stringTime;

    }
}
