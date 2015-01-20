package fr.axin.gservices.util;

import java.io.PrintWriter;

import fr.axin.gservices.servlet.html.HTMLPageFactory;

public abstract class LogUtils {
    public static void log(String msg, HTMLPageFactory out, PrintWriter log) {
        if (out == null && log == null) {
            System.out.println("[LOG] " + msg);
        } else {
            if(out != null) {
                out.write("<p>" + msg + "</p>");
            }
            if(log != null) {
                log.println(msg);
            }
        }

    }
}
