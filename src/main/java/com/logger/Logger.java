package com.logger;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Logger {
    private static Logger logger = new Logger();

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Logger.class);

    public static Logger getInstance() {
        return logger;
    }

    private Logger() {
        String confPath = "src\\main\\resources\\log4j.properties";
        PropertyConfigurator.configure(confPath);
    }

    public void saveLog(String log){
        LOGGER.info(log);
    }

    public String watchLogFile(){
        String fileContent = "";
        try{
            fileContent = new String(Files.readAllBytes(Paths.get("log_file.log")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
