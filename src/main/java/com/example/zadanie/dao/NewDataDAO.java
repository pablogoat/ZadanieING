package com.example.zadanie.dao;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class NewDataDAO {

    @CsvBindByName(column = "info_as_of_date")
    private String date;

    @CsvBindByName(column = "customer_id")
    private String customerId;

    @CsvBindByName(column = "customer_name")
    private String customerName;

    @CsvBindByName(column = "customer_start_date")
    private String startDate;

    @CsvBindByName(column = "customer_type")
    private String customerType;

    @CsvBindByName(column = "customer_income")
    private String customerIncome;

    @CsvBindByName(column = "customer_risk_class")
    private String customerRiskClass;

    @CsvBindByName(column = "customer_business_type")
    private String customerBusinessType;


}
