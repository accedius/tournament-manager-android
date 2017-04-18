package fit.cvut.org.cz.tournamentmanager.presentation.helpers;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tournamentmanager.business.helpers.JsonFileValidator;

/**
 * Class for working with files.
 */
public class FilesHelper {
    private static final String EXTENSION = ".json";

    /**
     * Saves file to Download folder.
     * @param filename name of file to be created
     * @param json file content
     * @return True if file was created, false otherwise.
     */
    public static boolean saveFile(String filename, String json) {
        if (isExternalStorageWritable()) {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!folder.exists() && !folder.mkdirs())
                return false;
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

    /**
     * Load file names in Download folder by given sport.
     * @param sportContext name of sport
     * @return file names
     */
    public static String [] getFileNames(String sportContext) {
        List<File> files = getFiles(sportContext);
        List<String> fileNames = new ArrayList<>();
        for (File f : files)
            fileNames.add(f.getName());
        String [] ret = new String[fileNames.size()];
        fileNames.toArray(ret);
        return ret;
    }

    /**
     * Load files in Download folder by given sport.
     * @param sportContext
     * @return files
     */
    public static List<File> getFiles(String sportContext) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File directory = new File(path);
        List<File> files = new ArrayList<>();
        if (!directory.exists())
            return files;

        File [] allFiles = directory.listFiles();
        for (File f : allFiles) {
            if (!f.getName().endsWith(EXTENSION))
                continue;

            String fileContent = loadFileContent(f);
            if (fileContent == null) {
                Log.d("IMPORT", "Load file content failed! - "+f.getName());
                continue;
            }

            if (!JsonFileValidator.valid(fileContent, sportContext)) {
                Log.d("IMPORT", "Invalid json file! - "+f.getName());
                continue;
            }

            files.add(f);
        }
        return files;
    }

    /**
     * Load content by given file
     * @param file given file to be loaded
     * @return file content
     */
    public static String loadFileContent(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            isr.close();
            fis.close();
            return sb.toString().replaceAll("\\s+","");
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
