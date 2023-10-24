package lk.dhanushkaTa.hotelservice.embeded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Embeddable
public class RoomOption {
    private double hotelRoom_Opt_1;
    private double hotelRoom_Opt_2;
    private double hotelRoom_Opt_3;
    private double hotelRoom_Opt_4;
}
