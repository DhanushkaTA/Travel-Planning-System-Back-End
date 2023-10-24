package lk.dhanushkaTa.hotelservice.service.impl;

import lk.dhanushkaTa.hotelservice.dto.HotelDTO;
import lk.dhanushkaTa.hotelservice.entity.Hotel;
import lk.dhanushkaTa.hotelservice.exception.DuplicateException;
import lk.dhanushkaTa.hotelservice.exception.NotFoundException;
import lk.dhanushkaTa.hotelservice.repository.HotelRepository;
import lk.dhanushkaTa.hotelservice.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    @Autowired
    private final HotelRepository hotelRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public void saveHotel(HotelDTO hotelDTO, MultipartFile pic) throws DuplicateException {
        if (hotelRepository.existsById(hotelDTO.getHotelId())){
            throw new DuplicateException("Hotel Id already exits!");
        }
//        Hotel hotel = this.handleFile(pic, modelMapper.map(hotelDTO, Hotel.class));
        hotelRepository.save(
                this.handleFile(pic, modelMapper.map(hotelDTO, Hotel.class))
        );
    }

    @Override
    public HotelDTO findHotelById(String hotelId) {
//        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        return hotelRepository.
                findById(hotelId).map(this::convertPathToByte).orElse(null);
//        return hotel.map(this::convertPathToByte).orElse(null);
//        return hotel.map(value -> modelMapper.map(value, HotelDTO.class)).orElse(null);
    }

    private HotelDTO convertPathToByte(Hotel hotel) {

        HotelDTO hotelDTO = modelMapper.map(hotel, HotelDTO.class);
        try {
            hotelDTO.setHotelImage(
                    Files.readAllBytes(Paths.get(hotel.getHotelImage()).toFile().toPath())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return hotelDTO;
    }

    @Override
    public List<HotelDTO> getHotelList() {
        return hotelRepository.findAll().stream().
                map(this::convertPathToByte).collect(Collectors.toList());
//        return modelMapper.map(hotelRepository.findAll(),new TypeToken<List<HotelDTO>>(){}.getType());
    }

    @Override
    public List<HotelDTO> findHotelByNameLike(String hotelName) {
        List<Hotel> hotelList = hotelRepository.
                findByHotelNameIsLikeIgnoreCaseOrderByHotelCategoryAsc(hotelName);
        if (hotelList.isEmpty()){
            return null;
        }else {
            return hotelList.stream().
                    map(this::convertPathToByte).collect(Collectors.toList());//return hotelDto list
//            return modelMapper.map(hotelList, new TypeToken<List<HotelDTO>>(){}.getType());
        }
    }

    public HotelDTO findHotelByName(String hotelName){
        Hotel hotel = hotelRepository.findByHotelNameIgnoreCase(hotelName);
        return hotel!=null ? this.convertPathToByte(hotel) : null;
//        return hotel!=null ? modelMapper.map(hotel,HotelDTO.class) : null;
//        return hotel.map(value -> modelMapper.map(value,HotelDTO.class)).orElse(null);
    }

    @Override
    public void updateHotelDetails(HotelDTO hotelDTO,MultipartFile pic) throws NotFoundException {
        if (!hotelRepository.existsById(hotelDTO.getHotelId())){
            throw new NotFoundException("Hotel couldn't find");
        }
        Hotel hotel = this.handleFile(pic, modelMapper.map(hotelDTO, Hotel.class));
//        hotelRepository.save(modelMapper.map(hotelDTO, Hotel.class));
        hotelRepository.save(hotel);
    }

    @Override
    public void deleteHotel(String hotelId) throws NotFoundException {
        if (!hotelRepository.existsById(hotelId)){
            throw new NotFoundException("Hotel couldn't find");
        }
        hotelRepository.deleteById(hotelId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private Hotel handleFile(MultipartFile pic,Hotel hotel){

        try {
            String uploadPathDer="E:\\IJSE\\AAD\\image\\hotel\\"+hotel.getHotelId();
            Path uploadPath = Paths.get(uploadPathDer);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }else {
                FileUtils.deleteDirectory(new File(uploadPathDer));
                Files.createDirectories(uploadPath);
            }

            for (int i=0;i<3;i++){
                byte[] bytes = pic.getBytes();
                Path path = Paths.get(uploadPath +"\\"+ pic.getOriginalFilename());
                Files.write(path,bytes);
                hotel.setHotelImage(path.toString());
            }

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return hotel;
    }

    public String getNextId(){

        List<Hotel> hotelList = hotelRepository.getLastHotelId();
        String lastHotelId="";
        if (!hotelList.isEmpty()){
            System.out.println(hotelList.get(0).getHotelId());
            lastHotelId=hotelList.get(0).getHotelId();
        }

        return this.generateNextHotelId(lastHotelId);
    }


    private String generateNextHotelId(String lastHotelId) {
        String date="";
        String newDate="";
        date=new SimpleDateFormat("yyyy/MM").format(new Date());
        newDate="H/"+date;//G/2020/10@0001

        if(!(lastHotelId.equals(""))) {
            String[] ids = lastHotelId.split("@");
            int id = Integer.parseInt(ids[1]);
            id += 1;


            boolean isEquals=isDateEquals(ids[0],newDate);
            if(!isEquals){
                ids[0]=newDate;
                id=1;
            }

            String newLoginId=String.format("@%03d",id);
            return ids[0] + newLoginId;
        }

        return newDate+"@0001";
    }

    private boolean isDateEquals(String id, String date) {
        return id.equals(date);
//        if(id.equals(date)){
//            return true;
//        }
//        return false;
    }



}
