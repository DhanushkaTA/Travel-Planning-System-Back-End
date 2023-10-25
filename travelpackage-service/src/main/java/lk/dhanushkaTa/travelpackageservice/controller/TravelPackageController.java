package lk.dhanushkaTa.travelpackageservice.controller;

import lk.dhanushkaTa.travelpackageservice.dto.TravelPackageDTO;
import lk.dhanushkaTa.travelpackageservice.exception.DuplicateException;
import lk.dhanushkaTa.travelpackageservice.exception.NotFoundException;
import lk.dhanushkaTa.travelpackageservice.service.TravelPackageService;
import lk.dhanushkaTa.travelpackageservice.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/travelPackage")
@RequiredArgsConstructor
public class TravelPackageController {

    @Autowired
    private final TravelPackageService travelPackageService;

    @GetMapping
    public String ping(){
        return "TravelPackage Controller Ok..";
    }

    @GetMapping(path = "find",params = {"packageId"})//find?packageId=T/2023/10@001
    public ResponseUtil findPackageById(String packageId){
        return new ResponseUtil(
                200, "Package found",travelPackageService.findPackageById(packageId));
    }

    @GetMapping(path = "find/all")
    public ResponseUtil getAllPackages(){
        return new ResponseUtil(
                200, "Package found",travelPackageService.getAllPackages());
    }

    @PostMapping(path = "save")
    public ResponseUtil savePackage(@RequestBody TravelPackageDTO packageDTO) throws DuplicateException {
        travelPackageService.saveTravelPackage(packageDTO);
        return new ResponseUtil(200,"Package saved",null);
    }

    @PutMapping(path = "update")
    public ResponseUtil updatePackage(@RequestBody TravelPackageDTO packageDTO) throws NotFoundException {
        travelPackageService.updateTravelPackage(packageDTO);
        return new ResponseUtil(200,"Package updated",null);
    }

    @DeleteMapping(path = "delete", params = {"packageId"})
    public ResponseUtil deletePackage(String packageId) throws NotFoundException {
        travelPackageService.deletePackage(packageId);
        return new ResponseUtil(200,"Package Deleted",null);
    }

    @GetMapping(path = "get/id")
    public ResponseUtil getNextPackageId(){
        return new ResponseUtil(200,"Next Package Id",travelPackageService.getNextId());
    }

    @GetMapping(path = "get/details",params = {"date"})
    public ResponseUtil getDetails(@RequestParam String date){
        return new ResponseUtil(
                200,"Details",travelPackageService.getDetails(date));
    }
}
