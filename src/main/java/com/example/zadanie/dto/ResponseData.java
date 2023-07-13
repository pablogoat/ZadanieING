package com.example.zadanie.dto;

import com.example.zadanie.models.Customer;
import com.example.zadanie.models.OneDayData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseData extends NewDataDTO {
    private Double R1;
    private Double R2;

    public ResponseData(Customer customer, OneDayData oneDayData){
        date = oneDayData.getDate()
                .toString();
        customerId = customer.getId();
        customerName = customer.getCustomerName();
        startDate = customer.getStartDate()
                .toString();
        customerType = customer.getCustomerType();
        customerIncome = oneDayData.getCustomerIncome()
                .toString();
        customerRiskClass = oneDayData.getCustomerRiskClass();
        customerBusinessType = customer.getCustomerBusinessType();
        R1 = oneDayData.getR1();
        R2 = oneDayData.getR2();

    }
}
