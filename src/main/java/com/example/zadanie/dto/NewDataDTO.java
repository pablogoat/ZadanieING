package com.example.zadanie.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class NewDataDTO {

    @CsvBindByName(column = "info_as_of_date")
    protected String date;

    @CsvBindByName(column = "customer_id")
    protected String customerId;

    @CsvBindByName(column = "customer_name")
    protected String customerName;

    @CsvBindByName(column = "customer_start_date")
    protected String startDate;

    @CsvBindByName(column = "customer_type")
    protected String customerType;

    @CsvBindByName(column = "customer_income")
    protected String customerIncome;

    @CsvBindByName(column = "customer_risk_class")
    protected String customerRiskClass;

    @CsvBindByName(column = "customer_business_type")
    protected String customerBusinessType;


}
