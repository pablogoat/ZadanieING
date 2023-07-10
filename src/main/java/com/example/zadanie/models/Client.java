package com.example.zadanie.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    private String id;
    private String customerName;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    private String customerType;
    private BigDecimal customerIncome;
    private String customerRiskClass;
    private String customerBusinessType;
    private float R1;
    private float R2;
}
