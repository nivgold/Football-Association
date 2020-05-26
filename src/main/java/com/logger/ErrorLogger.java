package com.logger;

import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ErrorLogger {
    private static ErrorLogger errorLogger = new ErrorLogger();

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ErrorLogger.class);


    public static ErrorLogger getInstance() {
        return errorLogger;
    }

    private ErrorLogger() {
        String confPath = "src\\main\\resources\\error_logger.properties";
        PropertyConfigurator.configure(confPath);
    }

    public void saveError(String error){
        LOGGER.error(error);
    }


    public String watchLogFile(){
        String fileContent = "";
        try{
            fileContent = new String(Files.readAllBytes(Paths.get("error_file.log")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
