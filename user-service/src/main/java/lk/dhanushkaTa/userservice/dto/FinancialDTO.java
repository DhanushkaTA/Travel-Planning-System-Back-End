package lk.dhanushkaTa.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class FinancialDTO {

    private double totalIncome;
    private double totalProfit;
    private List<DetailsDTO> details;
}
