package fr.axin.gservices.servlet.html;

import java.io.File;

public class LogFileList {

    private static File dir;
    private static File[] list;
    private static String logDir;

    public LogFileList(String d) {
        dir = new File(d);
        list = dir.listFiles();
        logDir = d;
    }

    public int getCount() {
        if (list == null)
            return 0;
        else
            return list.length;
    }

    public static File[] getFileList() {
        list = dir.listFiles();
        return list;
    }

    public File getFileAt(int i) {
        return this.list[i];
    }

    public String getLogDir() {
        return this.logDir;
    }
}
