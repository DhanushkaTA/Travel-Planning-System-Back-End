package lk.dhanushkaTa.vehicleservice.service.impl;

import lk.dhanushkaTa.vehicleservice.dto.VehicleDTO;
import lk.dhanushkaTa.vehicleservice.entity.Vehicle;
import lk.dhanushkaTa.vehicleservice.exception.DuplicateException;
import lk.dhanushkaTa.vehicleservice.exception.NotFoundException;
import lk.dhanushkaTa.vehicleservice.repository.VehicleRepository;
import lk.dhanushkaTa.vehicleservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private final VehicleRepository vehicleRepository;

    @Autowired
    private final ModelMapper modelMapper;

    private  Sort.Direction direction=Sort.Direction.ASC;

    private String properties="id";

    private String transmissionType="auto";


    @Override
    public VehicleDTO findVehicleById(String vehicleId) {
//        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        return vehicleRepository.findById(vehicleId)
                .map(this::convertPathToByte).orElse(null);
    }

    @Override
    public List<VehicleDTO> getAllVehicle() {
//        return modelMapper.map(vehicleRepository.findAll(),new TypeToken<List<VehicleDTO>>(){}.getType());
        return vehicleRepository.findAll().
                stream().map(this::convertPathToByte).collect(Collectors.toList());
    }

    @Override
    public List<VehicleDTO> getAllVehicleBySorting(String direction, String properties,String type) {
        this.properties = properties.equalsIgnoreCase("seat") ? "vehicleSeatCapacity" :
                properties.equalsIgnoreCase("fuelType") ? "vehicleFuelType" :
                        properties.equalsIgnoreCase("transmissionType") ?
                                "vehicleTransmissionType" : "id";
        this.direction = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        System.out.println(" \n"+this.properties+" \n");
        System.out.println(" \n"+this.direction+" \n");
        System.out.println(" \n"+this.transmissionType+" \n");

        if (properties.equalsIgnoreCase("transmissionType")){
            this.transmissionType=type;

            return vehicleRepository.
                    findByVehicleTransmissionTypeOrderByVehicleIdAsc(this.transmissionType).
                    stream().map(this::convertPathToByte).collect(Collectors.toList());
        }

        return vehicleRepository.findAll(Sort.by(this.direction,this.properties)).
                stream().map(this::convertPathToByte).collect(Collectors.toList());

    }

    ///////////////////////////////////////// NEW /////////////////////////////////////////////////////////////////
    public List<VehicleDTO> getFilteredVehicleList(String direction, String properties,String key,int pageNum){
        this.direction = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.
                of(pageNum,10,Sort.by(this.direction,"vehicleSeatCapacity"));
//        PageRequest pageRequest = PageRequest.of(0,10,Sort.by(Sort.Direction.ASC,properties));

        if (properties.equalsIgnoreCase("fuel")){
            Page<Vehicle> list = vehicleRepository.findByVehicleFuelTypeIsLike(key, pageRequest);
            List<VehicleDTO> collect = list.stream().map(this::convertPathToByte).collect(Collectors.toList());
            for (VehicleDTO v:
                    collect) {
                System.out.println(v.getVehicleId()+" : "+v.getVehicleSeatCapacity()+" : "+v.getVehicleSearchType());
            }

            return collect;
        }else {
            List<Vehicle> list = vehicleRepository.findByVehicleTransmissionTypeIsLike(key, pageRequest);
            List<VehicleDTO> collect = list.stream().map(this::convertPathToByte).collect(Collectors.toList());
            for (VehicleDTO v:
                    collect) {
                System.out.println(v.getVehicleId()+" : "+v.getVehicleSeatCapacity()+" : "+v.getVehicleSearchType());
            }

            return collect;
        }
    }


    public List<VehicleDTO> getVehicleListBySearchType(String key,String direction,int pageNum ){
        this.direction = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.
                of(pageNum,10,Sort.by(this.direction,"vehicleSeatCapacity"));

        List<Vehicle> list = vehicleRepository.findByVehicleSearchTypeIsLike(key, pageRequest);
        List<VehicleDTO> collect = list.stream().map(this::convertPathToByte).collect(Collectors.toList());
        for (VehicleDTO v:
                collect) {
            System.out.println(v.getVehicleId()+" : "+v.getVehicleSeatCapacity()+" : "+v.getVehicleSearchType());
        }

        return collect;
    }

    @Override
    public long getVehicleCount(){
        long count = vehicleRepository.count();
        long pageCont=count/10;
        if (count%10>0){
            pageCont++;
        }

        System.out.println(count+" : "+pageCont);
        return pageCont;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Images order ->
     * 1)DriverLicense
     * 2)frontImage
     * 3)rearImage
     * 4)sideImage
     * 5)frontInteriorImage
     * 6)rearInteriorImage
     * */

    @Override
    public void saveVehicle(VehicleDTO vehicleDTO, MultipartFile[] images) throws DuplicateException {
        if (vehicleRepository.existsById(vehicleDTO.getVehicleId())){
            throw new DuplicateException("Vehicle number already exits");
        }
//        vehicleRepository.save(modelMapper.map(vehicleDTO, Vehicle.class));
//        Vehicle vehicle = this.handleFile(images, modelMapper.map(vehicleDTO, Vehicle.class));
        vehicleRepository.save(this.handleFile(images, modelMapper.map(vehicleDTO, Vehicle.class)));
    }

    @Override
    public void updateVehicle(VehicleDTO vehicleDTO,MultipartFile[] images) throws NotFoundException {
        if (!vehicleRepository.existsById(vehicleDTO.getVehicleId())){
            throw new NotFoundException("Vehicle number not found");
        }
//        Vehicle vehicle = this.handleFile(images, modelMapper.map(vehicleDTO, Vehicle.class));
        vehicleRepository.save(this.handleFile(images, modelMapper.map(vehicleDTO, Vehicle.class)));
    }

    @Override
    public void deleteVehicle(String vehicleId) throws NotFoundException {
        if (!vehicleRepository.existsById(vehicleId)){
            throw new NotFoundException("Vehicle number not found");
        }
        vehicleRepository.deleteById(vehicleId);
    }

    @Override
    public void updateVehicleStatus(String vehicleId) throws NotFoundException {
        if (!vehicleRepository.existsById(vehicleId)){
            throw new NotFoundException("Vehicle number not found");
        }
        Optional<Vehicle> vehicleById = vehicleRepository.findById(vehicleId);
        if (vehicleById.isPresent()){
            Vehicle vehicle = vehicleById.get();
            if (vehicle.getVehicleStatus().equalsIgnoreCase("eligible")){
                vehicle.setVehicleStatus("NotEligible");
            }else {
                vehicle.setVehicleStatus("Eligible");
            }
            vehicleRepository.save(vehicle);
        }
    }

    private Vehicle handleFile(MultipartFile[] images, Vehicle vehicle) {

        List<String>paths=new ArrayList<>();
        System.out.println("image length : "+images.length);


        try {
            String uploadPathDer="E:\\IJSE\\AAD\\image\\vehicle\\"+vehicle.getVehicleId();
            Path uploadPath = Paths.get(uploadPathDer);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }else {
                FileUtils.deleteDirectory(new File(uploadPathDer));
                Files.createDirectories(uploadPath);
            }

            for (int i=0;i<images.length;i++){
                byte[] bytes = images[i].getBytes();
                Path path = Paths.get(uploadPath +"\\"+ images[i].getOriginalFilename());
                Files.write(path,bytes);
                paths.add(path.toString());
            }

            vehicle.setVehicleDriverLicense(paths.get(0));
            vehicle.setFrontImage(paths.get(1));
            vehicle.setRearImage(paths.get(2));
            vehicle.setSideImage(paths.get(3));
            vehicle.setFrontInteriorImage(paths.get(4));
            vehicle.setRearInteriorImage(paths.get(5));

            System.out.println(" \n Vehicle : "+vehicle);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return vehicle;
    }

    private VehicleDTO convertPathToByte(Vehicle vehicle) {

        VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);
        try {
            vehicleDTO.setVehicleDriverLicense(
                    Files.readAllBytes(Paths.get(vehicle.getVehicleDriverLicense()).toFile().toPath()));
            vehicleDTO.setFrontImage(Files.readAllBytes(Paths.get(vehicle.getFrontImage()).toFile().toPath()));
            vehicleDTO.setRearImage(Files.readAllBytes(Paths.get(vehicle.getRearImage()).toFile().toPath()));
            vehicleDTO.setSideImage(Files.readAllBytes(Paths.get(vehicle.getSideImage()).toFile().toPath()));
            vehicleDTO.setFrontInteriorImage(Files.readAllBytes(Paths.get(vehicle.getFrontInteriorImage()).toFile().toPath()));
            vehicleDTO.setRearInteriorImage(
                    Files.readAllBytes(Paths.get(vehicle.getRearInteriorImage()).toFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return vehicleDTO;
    }
}
