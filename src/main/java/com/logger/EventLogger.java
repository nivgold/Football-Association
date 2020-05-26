package com.logger;

import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EventLogger {
    private static EventLogger eventLogger = new EventLogger();

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(EventLogger.class);

    public static EventLogger getInstance() {
        return eventLogger;
    }

    private EventLogger() {
        String confPath = "src\\main\\resources\\event_logger.properties";
        PropertyConfigurator.configure(confPath);
    }

    public void saveLog(String log){
        LOGGER.info(log);
    }

    public String watchLogFile(){
        String fileContent = "";
        try{
            fileContent = new String(Files.readAllBytes(Paths.get("event_file.log")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
