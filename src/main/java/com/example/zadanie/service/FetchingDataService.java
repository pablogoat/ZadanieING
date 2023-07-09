package com.example.zadanie.service;

import com.example.zadanie.dao.NewDataDAO;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Service
public class FetchingDataService {
    private final String dataPath;

    public FetchingDataService() {
        Properties properties = new Properties();
        try {
            String path = new File("src/main/resources/config.properties").getAbsolutePath();
            FileInputStream fileInputStream = new FileInputStream(path);
            properties.load(fileInputStream);

            dataPath = properties.getProperty("app.dataPath");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<NewDataDAO> fetchNewData() throws IOException {
        return new CsvToBeanBuilder<NewDataDAO>(new FileReader(dataPath))
                .withType(NewDataDAO.class)
                .build()
                .parse();
    }
}