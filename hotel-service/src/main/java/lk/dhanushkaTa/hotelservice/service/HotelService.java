package lk.dhanushkaTa.hotelservice.service;

import lk.dhanushkaTa.hotelservice.dto.HotelDTO;
import lk.dhanushkaTa.hotelservice.exception.DuplicateException;
import lk.dhanushkaTa.hotelservice.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {
    void saveHotel(HotelDTO hotelDTO, MultipartFile pic) throws DuplicateException;

    HotelDTO findHotelById(String hotelId);

    List<HotelDTO> getHotelList();

    List<HotelDTO> findHotelByNameLike(String hotelName);

   HotelDTO findHotelByName(String hotelName);

   void updateHotelDetails(HotelDTO hotelDTO,MultipartFile pic) throws NotFoundException;

   void deleteHotel(String hotelId) throws NotFoundException;

    String getNextId();
}
