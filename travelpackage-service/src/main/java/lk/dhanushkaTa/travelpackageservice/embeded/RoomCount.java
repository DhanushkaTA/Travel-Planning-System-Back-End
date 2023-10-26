package lk.dhanushkaTa.travelpackageservice.embeded;

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
public class RoomCount {
    private int full_double_Opt_1;
    private int half_double_Opt_2;
    private int full_triple_Opt_3;
    private int half_triple_Opt_4;
}
