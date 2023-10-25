package lk.dhanushkaTa.vehicleservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.dhanushkaTa.vehicleservice.dto.VehicleDTO;
import lk.dhanushkaTa.vehicleservice.exception.DuplicateException;
import lk.dhanushkaTa.vehicleservice.exception.NotFoundException;
import lk.dhanushkaTa.vehicleservice.service.VehicleService;
import lk.dhanushkaTa.vehicleservice.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    @Autowired
    private final VehicleService vehicleService;

    @Autowired
    private final ObjectMapper objectMapper;

    @GetMapping
    public String ping(){
        return "Vehicle Controller ok...";
    }

    @GetMapping(path = "find/{vehicleId}")
    public ResponseUtil findVehicleById(@PathVariable String vehicleId){
        return new ResponseUtil(200,"Vehicle Found",vehicleService.findVehicleById(vehicleId));
    }

    @GetMapping(path = "find/all")
    public ResponseUtil getAllVehicles(){
        return new ResponseUtil(200,"Vehicle list",vehicleService.getAllVehicle());
    }

    //  find/filter?direction=asc&properties=seat&type=all
    @GetMapping(path = "find/filter",params = {"direction","properties","type"})
    public ResponseUtil getVehicleListByOrder(String direction,String properties,String type){
        return new ResponseUtil(
                200,
                "Vehicle list",
                vehicleService
                        .getAllVehicleBySorting(direction,properties,type));
    }

    @PostMapping(path = "save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil saveVehicle(@RequestParam String vehicle,
                                    @RequestParam MultipartFile[] images) throws DuplicateException, JsonProcessingException {
//        vehicleService.saveVehicle(vehicleDTO);
        System.out.println(vehicle);
//        VehicleDTO vehicleDTO = objectMapper.readValue(vehicle, VehicleDTO.class);
        vehicleService.saveVehicle(
                objectMapper.readValue(vehicle, VehicleDTO.class),
                images);
        return new ResponseUtil(200,"Vehicle saved",null);
    }

    @PutMapping(path = "update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil updateVehicle(@RequestParam String vehicle,
                                      @RequestParam MultipartFile[] images) throws NotFoundException, JsonProcessingException {
//        vehicleService.updateVehicle(vehicleDTO);
        VehicleDTO vehicleDTO = objectMapper.readValue(vehicle, VehicleDTO.class);
        vehicleService.updateVehicle(vehicleDTO,images);
        return new ResponseUtil(200,"Vehicle updated",null);
    }

    @PutMapping(path = "update/status/{vehicleId}")
    public ResponseUtil updateVehicleStatus(@PathVariable String vehicleId) throws NotFoundException {
        vehicleService.updateVehicleStatus(vehicleId);
        return new ResponseUtil(200,"Vehicle status updated",null);
    }

    @DeleteMapping("delete/{vehicleId}")
    public ResponseUtil deleteVehicle(@PathVariable String vehicleId) throws NotFoundException {
        vehicleService.deleteVehicle(vehicleId);
        return new ResponseUtil(200,"Vehicle deleted",null);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = "sort/filter/{direction}/{properties}/{key}/{pageNum}")
    public ResponseUtil getVehicleByProperties(@PathVariable String direction,
                                               @PathVariable String properties,
                                               @PathVariable String key,
                                               @PathVariable int pageNum){
        return new ResponseUtil(200,"list",
                vehicleService.getFilteredVehicleList(direction,properties,key,pageNum));
    }

    @GetMapping(path = "sort/filter/searchType/{direction}/{key}/{pageNum}")
    public ResponseUtil getVehicleListBySearchType(@PathVariable String direction,
                                                   @PathVariable String key,
                                                   @PathVariable int pageNum){
        return new ResponseUtil(200,"list",
                vehicleService.getVehicleListBySearchType(key,direction,pageNum));
    }

    @GetMapping(path = "get/count")
    public ResponseUtil getVehicleCount(){
        return new ResponseUtil(200,"list",
                vehicleService.getVehicleCount());
    }
}
