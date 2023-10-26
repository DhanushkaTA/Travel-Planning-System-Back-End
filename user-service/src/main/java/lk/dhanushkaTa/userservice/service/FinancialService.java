package lk.dhanushkaTa.userservice.service;

import lk.dhanushkaTa.userservice.dto.FinancialDTO;

public interface FinancialService {

    FinancialDTO getIncomeDetails(String date);
}
