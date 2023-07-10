package com.example.zadanie.service;

import com.example.zadanie.dao.NewDataDAO;
import com.example.zadanie.models.OneDayData;
import com.example.zadanie.repository.OneDayDataRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FetchingDataService {
    private final String dataPath;
    private final OneDayDataRepository oneDayDataRepository;

    public FetchingDataService(OneDayDataRepository oneDayDataRepository) {
        this.oneDayDataRepository = oneDayDataRepository;
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

    public void fetchNewData() throws IOException {
        List<NewDataDAO> newData = new CsvToBeanBuilder<NewDataDAO>(new FileReader(dataPath))
                .withType(NewDataDAO.class)
                .build()
                .parse();

        for(NewDataDAO data : newData){
            var dataToSave =OneDayData.builder()
                    .date(LocalDate.parse(data.getDate()))
                    .customerId(data.getCustomerId())
                    .customerName((data.getCustomerName()))
                    .startDate(LocalDate.parse(data.getStartDate()))
                    .customerType(data.getCustomerType())
                    .customerIncome(new BigDecimal(data.getCustomerIncome()))
                    .customerRiskClass(data.getCustomerRiskClass())
                    .customerBusinessType(data.getCustomerBusinessType())
                    .R1(calculateR1(data))
                    .R2(calculateR2(data))
                    .build();

            oneDayDataRepository.save(dataToSave);
        }

    }

    private double calculateR1(NewDataDAO newData){
        // R1 = (CUSTOMER_INCOME/10) * F1
        double R1 = Double.parseDouble(newData.getCustomerIncome()) / 10;

        String customerType = newData.getCustomerType();

        if (customerType.equals("TYPE_A1")) {
            OptionalDouble avg = getCustomerData(newData.getCustomerId(), 30).stream()
                    .mapToDouble(o -> o.getCustomerIncome().doubleValue())
                    .average();

            if (avg.isEmpty()) {
                R1 *= 0.25;
            }
            else if (
                    avg.getAsDouble() < (0.8 * Double.parseDouble(newData.getCustomerIncome()))
            ) {
                R1 *= 0.3;
            }
            else {
                R1 *= 0.2;
            }
        }
        else if (
                customerType.equals("TYPE_A5")
                        &&
                newData.getCustomerRiskClass().equals("A3")){
            String customerBusinessTypeType = newData.getCustomerBusinessType();

            switch (customerBusinessTypeType) {
                case "BR_3" -> R1 *= 0.1;
                default -> R1 *= 0.2;
            }
        }

        return R1;
    }

    private double calculateR2(NewDataDAO newData){
        // R1 = (CUSTOMER_INCOME/10) * F1
        double R2 = Double.parseDouble(newData.getCustomerIncome()) / 100;

        String customerType = newData.getCustomerType();

        if (customerType.equals("TYPE_A1")) {
            String customerBusinessTypeType = newData.getCustomerBusinessType();

            switch (customerBusinessTypeType) {
                case "BT_1", "BR_2" -> R2 *= 0.05;
                case "BR_3" -> R2 *= 0.08;
                default -> R2 *= 0.09;
            }
        }

        return R2;
    }

    public List<OneDayData> getCustomerData(String customerId, int rows){
        return oneDayDataRepository.getCustomerData(customerId, rows);
    }

}