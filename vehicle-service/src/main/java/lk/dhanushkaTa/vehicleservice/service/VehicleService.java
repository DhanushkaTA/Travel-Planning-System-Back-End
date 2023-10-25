package lk.dhanushkaTa.vehicleservice.service;

import lk.dhanushkaTa.vehicleservice.dto.VehicleDTO;
import lk.dhanushkaTa.vehicleservice.exception.DuplicateException;
import lk.dhanushkaTa.vehicleservice.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleService {

    VehicleDTO findVehicleById(String vehicleId);

    List<VehicleDTO> getAllVehicle();

    List<VehicleDTO> getAllVehicleBySorting(String direction, String properties,String type);

    void saveVehicle(VehicleDTO vehicleDTO, MultipartFile[] images) throws DuplicateException;
//    void saveVehicle(VehicleDTO vehicleDTO) throws DuplicateException;

    void updateVehicle(VehicleDTO vehicleDTO,MultipartFile[] images) throws NotFoundException;

    void deleteVehicle(String vehicleId) throws NotFoundException;

    void updateVehicleStatus(String vehicleId) throws NotFoundException;


    //////////////////////////////////////////////////////////////////////////////////////////

    List<VehicleDTO> getFilteredVehicleList(String direction, String properties,String key,int pageNum);

    public List<VehicleDTO> getVehicleListBySearchType(String key,String direction,int pageNum );

    public long getVehicleCount();
}
