package com.example.zadanie.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Customer customer;
    private BigDecimal customerIncome;
    private String customerRiskClass;
    private Double R1;
    private Double R2;
}
