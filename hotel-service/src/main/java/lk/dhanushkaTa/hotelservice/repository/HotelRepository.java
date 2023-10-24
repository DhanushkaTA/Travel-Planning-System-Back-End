package lk.dhanushkaTa.hotelservice.repository;

import lk.dhanushkaTa.hotelservice.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,String> {

    List<Hotel> findByHotelNameIsLikeIgnoreCaseOrderByHotelCategoryAsc(String hotelName);
    List<Hotel> findByHotelNameStartingWithOrderByHotelCategoryAsc(String hotelName);
//    Hotel findByHotelName(String hotelName);
    Hotel findByHotelNameIgnoreCase(String hotelName);

    @Query(value = "select h from Hotel h order by h.hotelId desc")
    List<Hotel> getLastHotelId();
}
