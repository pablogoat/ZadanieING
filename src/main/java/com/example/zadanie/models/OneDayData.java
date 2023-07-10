package com.example.zadanie.models;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "daydata")
public class OneDayData {
    @Id
    @GeneratedValue
    private Integer id;
    @Temporal(TemporalType.DATE)
    private LocalDate date;
    /*@ManyToOne
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "ID", nullable = false)
    private Client client;*/
    private String customerId;
    private String customerName;
    @Temporal(TemporalType.DATE)
    private LocalDate startDate;
    private String customerType;
    private BigDecimal customerIncome;
    private String customerRiskClass;
    private String customerBusinessType;
    private double R1;
    private double R2;
}