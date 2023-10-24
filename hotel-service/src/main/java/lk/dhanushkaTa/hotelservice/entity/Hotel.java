package lk.dhanushkaTa.hotelservice.entity;

import lk.dhanushkaTa.hotelservice.embeded.Contact;
import lk.dhanushkaTa.hotelservice.embeded.RoomOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    private String hotelId;

    @Column(nullable = false)
    private String hotelName;

    @Column(nullable = false)
    private String hotelCategory;

    @Column(nullable = false)
    private String hotelLocation;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String hotelGmapLocation;

    @Column(nullable = false,unique = true)
    private String hotelEmail;

    @Column(nullable = false)
    private Contact hotelContact;

    @Column(nullable = false)
    private String hotelPetAllow;

    @Column(nullable = false)
    private String hotelCancellationCriteria;

    @Column(nullable = false)
    private RoomOption hotelRoomOpt;

    @Column(columnDefinition = "TEXT")
    private String hotelImage;

}
