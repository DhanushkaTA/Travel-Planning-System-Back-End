package lk.dhanushkaTa.travelpackageservice.service;

import lk.dhanushkaTa.travelpackageservice.dto.TravelPackageDTO;
import lk.dhanushkaTa.travelpackageservice.exception.DuplicateException;
import lk.dhanushkaTa.travelpackageservice.exception.NotFoundException;

import java.util.List;

public interface TravelPackageService {

    void saveTravelPackage(TravelPackageDTO travelPackageDTO) throws DuplicateException;

    void updateTravelPackage(TravelPackageDTO travelPackageDTO) throws NotFoundException;

    TravelPackageDTO findPackageById(String packageId);

    List<TravelPackageDTO> getAllPackages();

    void deletePackage(String packageId) throws NotFoundException;

    String getNextId();

    List<TravelPackageDTO> getDetails(String date);



}
