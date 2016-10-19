package fit.cvut.org.cz.tournamentmanager.business.serialization;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.serialization.BaseFileValidator;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 19.10.2016.
 */
public class FilesHelper {
    private static final String EXTENSION = ".json";

    /* Save file to Download folder */
    public static boolean saveFile(String filename, String json) {
        if (isExternalStorageWritable()) {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + filename + EXTENSION);
            try {
                file.createNewFile();
                OutputStream os = new FileOutputStream(file);
                os.write(json.getBytes());
                os.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    /* Load files in Download folder */
    public static String [] getFileNames(String sportContext) {
        List<File> files = getFiles(sportContext);
        List<String> fileNames = new ArrayList<>();
        for (File f : files)
            fileNames.add(f.getName());
        String [] ret = new String[fileNames.size()];
        fileNames.toArray(ret);
        return ret;
    }

    /* Load files in Download folder */
    public static List<File> getFiles(String sportContext) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File directory = new File(path);
        File [] allFiles = directory.listFiles();
        List<File> files = new ArrayList<>();
        for (File f : allFiles) {
            if (!f.getName().endsWith(EXTENSION))
                continue;

            String fileContent = loadFileContent(f);
            if (fileContent == null) {
                Log.d("IMPORT", "Load file content failed! - "+f.getName());
                continue;
            }

            if (!BaseFileValidator.validJsonFile(fileContent, sportContext)) {
                Log.d("IMPORT", "Invalid json file! - "+f.getName());
                continue;
            }

            files.add(f);
        }
        return files;
    }

    /* Loads file content */
    public static String loadFileContent(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
