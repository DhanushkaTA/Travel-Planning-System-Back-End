package lk.dhanushkaTa.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DetailsDTO {
    private String date;
    private double guideFee;
    private double hotelFee;
    private double vehicleFee;
    private double total;
    private double profit;
}