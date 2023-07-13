package com.example.zadanie.service;

import com.example.zadanie.dto.NewDataDTO;
import com.example.zadanie.dto.ResponseData;
import com.example.zadanie.models.Customer;
import com.example.zadanie.models.OneDayData;
import com.example.zadanie.repository.OneDayDataRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class FetchingDataService {
    private final String dataPath;
    private final OneDayDataRepository oneDayDataRepository;
    private final EmailGenerateService emailGenerateService;

    public FetchingDataService(OneDayDataRepository oneDayDataRepository, EmailGenerateService emailGenerateService) {
        this.oneDayDataRepository = oneDayDataRepository;
        this.emailGenerateService = emailGenerateService;

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

    public Optional<ResponseData> getNewestDataByCustomer(String customerId, Optional<LocalDate> date){
        return oneDayDataRepository.getNewestDataByCustomer(customerId, date);
    }

    public void fetchNewData() throws IOException {
        List<NewDataDTO> newData = new CsvToBeanBuilder<NewDataDTO>(new FileReader(dataPath))
                .withType(NewDataDTO.class)
                .build()
                .parse();

        newData.forEach(System.out::println);

        for(NewDataDTO data : newData){
            Optional<Customer> customer = oneDayDataRepository.getCustomer(data.getCustomerId());

            if (customer.isEmpty()) {
                var customerToSave = Customer.builder()
                        .id(data.getCustomerId())
                        .customerName(data.getCustomerName())
                        .startDate(LocalDate.parse(data.getStartDate()))
                        .customerType(data.getCustomerType())
                        .customerBusinessType(data.getCustomerBusinessType())
                        .build();

                customer = Optional.of(customerToSave);
                oneDayDataRepository.saveCustomer(customerToSave);
            }

            var dataToSave = OneDayData.builder()
                    .date(LocalDate.parse(data.getDate()))
                    .customer(customer.get())
                    .customerIncome(new BigDecimal(data.getCustomerIncome()))
                    .customerRiskClass(data.getCustomerRiskClass())
                    .R1(calculateR1(data))
                    .R2(calculateR2(data))
                    .build();

            oneDayDataRepository.saveOneDayData(dataToSave);
        }

    }

    private double calculateR1(NewDataDTO newData){
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

            if (customerBusinessTypeType.equals("BR_3")) {
                R1 *= 0.1;
            } else {
                R1 *= 0.2;
            }
        }

        return R1;
    }

    private double calculateR2(NewDataDTO newData){
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

    private void potentialEmailSend(NewDataDTO newData) {
        List<OneDayData> latestData = getCustomerData(newData.getCustomerId(), 1);

        if(!latestData.isEmpty()) {
            String latestCustomerRiskClass = latestData.get(0).getCustomerRiskClass();

            if(!latestCustomerRiskClass.equals(newData.getCustomerRiskClass())){
                SimpleMailMessage message;

                if(
                        newData.getCustomerType().equals("TYPE_A2")
                        &&
                        newData.getCustomerBusinessType().equals("BR_2")
                ){
                    message = emailGenerateService.generateMail(
                            List.of("koordynatorkoordynatorow@fikcyjnafirma.com"),
                            String.format("Proszę o przegląd ryzyka klienta %s", newData.getCustomerId()));
                    //wyslanie wiadomosci
                } else if (
                        newData.getCustomerType().equals("TYPE_A1")
                        ||
                        newData.getCustomerType().equals("TYPE_A5")
                ) {
                    message = emailGenerateService.generateMail(
                            List.of("koordynatorkoordynatorow@fikcyjnafirma.com",
                            "dyrektorjednostki@fikcyjnafirma.com"),
                            String.format("Uprzejmie informuję, że zmieniła się klasa ryzyka dlaklienta %s",
                                    newData.getCustomerId()));
                    //wyslanie wiadomosci
                }
            }
        }
    }

    public List<OneDayData> getCustomerData(String customerId, int rows){
        return oneDayDataRepository.getCustomerData(customerId, rows);
    }

}