package com.example.zadanie.dao;

import com.example.zadanie.dto.ResponseData;
import com.example.zadanie.dto.ResponseNoteDTO;
import com.example.zadanie.models.Customer;
import com.example.zadanie.models.Note;
import com.example.zadanie.models.OneDayData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DAO {
    public void saveOneDayData(OneDayData data);
    public void saveCustomer(Customer customer);
    public void saveNote(Note note);
    public Optional<Customer> getCustomer(String customerId);
    public List<OneDayData> getCustomerData(String customerId, int rows);
    public Optional<ResponseData> getNewestDataByCustomer(String customerId, Optional<LocalDate> date);
    public List<ResponseNoteDTO> getNotes(String customerId, LocalDate since, LocalDate until);
}
