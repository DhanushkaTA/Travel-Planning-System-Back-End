package lk.dhanushkaTa.hotelservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.dhanushkaTa.hotelservice.dto.HotelDTO;
import lk.dhanushkaTa.hotelservice.exception.DuplicateException;
import lk.dhanushkaTa.hotelservice.exception.NotFoundException;
import lk.dhanushkaTa.hotelservice.exception.ProcessingException;
import lk.dhanushkaTa.hotelservice.service.HotelService;
import lk.dhanushkaTa.hotelservice.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/hotel")
@RequiredArgsConstructor
@CrossOrigin
public class HotelController {

    @Autowired
    private final HotelService hotelService;

    @Autowired
    private final ObjectMapper objectMapper;

    @GetMapping
    public String ping(){
        return "Hotel Controller Ok";
    }

    @GetMapping(path = "find/all")
    public ResponseUtil getAllHotels(){
        return new ResponseUtil(200,"Hotel List",hotelService.getHotelList());
    }

    @GetMapping(path = "find",params = {"hotelId"})//find?hotelId=H/2023/10@001
    public ResponseUtil findHotelById(String hotelId){
        System.out.println(hotelService.findHotelById(hotelId));
        return new ResponseUtil(200,"Hotel Found",hotelService.findHotelById(hotelId));
    }

    @GetMapping(path = "find/name/{name}")
    public ResponseUtil findHotelByName(@PathVariable String name){
        return new ResponseUtil(200,"Hotel found",hotelService.findHotelByName(name));
    }

    @GetMapping(path = "find/like/{name}")
    public ResponseUtil findHotelByNameLike(@PathVariable String name){
        return new ResponseUtil(200,"Hotel found",hotelService.findHotelByNameLike(name));
    }

    @PostMapping(path = "save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseUtil saveHotel(@RequestParam String hotel,
                                  MultipartFile pic) throws DuplicateException, ProcessingException {
        HotelDTO hotelDTO = null;
        try {
            hotelDTO = objectMapper.readValue(hotel, HotelDTO.class);
        } catch (JsonProcessingException e) {
            throw new ProcessingException(e.getMessage(),e);
        }
        hotelService.saveHotel(hotelDTO,pic);
        return new ResponseUtil(200,"Hotel Saved",null);
    }

    @PutMapping(path = "update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseUtil updateHotel(@RequestParam String hotel,
                                    @RequestParam MultipartFile pic) throws NotFoundException, ProcessingException {
        HotelDTO hotelDTO = null;
        try {
            hotelDTO = objectMapper.readValue(hotel, HotelDTO.class);
        } catch (JsonProcessingException e) {
            throw new ProcessingException(e.getMessage(),e);
        }
        hotelService.updateHotelDetails(hotelDTO,pic);
        return new ResponseUtil(200,"Hotel updated",null);
    }

    @DeleteMapping(path = "delete",params = {"hotelId"})////delete?hotelId=H/2023/10@001
    public ResponseUtil deleteHotel(String hotelId) throws NotFoundException {
        hotelService.deleteHotel(hotelId);
        return new ResponseUtil(200,"Hotel Deleted",null);
    }

    @GetMapping(path = "get/id")
    public ResponseUtil getNextHotelId(){
        return new ResponseUtil(200,"Next Hotel Id",hotelService.getNextId());
    }

}
