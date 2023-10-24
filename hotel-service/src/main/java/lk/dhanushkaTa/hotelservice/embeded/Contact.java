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
public class Contact {
    private String hotelContact_1;
    private String hotelContact_2;
}
