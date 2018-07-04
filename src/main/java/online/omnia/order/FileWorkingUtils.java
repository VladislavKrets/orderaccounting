package online.omnia.order;


import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 12.07.2017.
 */
public class FileWorkingUtils {
    private static BufferedReader fileReader;
    private static FileWriter logWriter;

    static {
        File file = new File("log/");
        if (!file.exists()) file.mkdir();
        file = new File("log/order_accounting.log");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            logWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Map<String, String> iniFileReader() {
        Map<String, String> properties = new HashMap<>();
        try {
            fileReader = new BufferedReader(new FileReader("configuration.ini"));
            String property;
            String[] propertyArray;
            while ((property = fileReader.readLine()) != null) {
                if (property.trim().isEmpty() || property.trim().startsWith("#")) continue;
                propertyArray = property.split("=");
                properties.put(propertyArray[0], propertyArray[1]);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static synchronized void writeLog(Date date, Time time, String url) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(date).append(" ").append(time).append(" ").append(url).append("\n");
        try {
            logWriter.write(logBuilder.toString());
            logWriter.flush();
        } catch (IOException e) {
            System.out.println("Error log writing");
        }
        logBuilder = null;
    }
}
