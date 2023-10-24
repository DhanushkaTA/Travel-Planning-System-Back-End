package lk.dhanushkaTa.guideservice.service;

import lk.dhanushkaTa.guideservice.dto.GuideDTO;
import lk.dhanushkaTa.guideservice.exception.DuplicateException;
import lk.dhanushkaTa.guideservice.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GuideService {

    GuideDTO findGuideById(String guideId);

    List<GuideDTO> getAllGuides();

    void saveGuide(GuideDTO guideDTO, MultipartFile nic,
                   MultipartFile guideId,MultipartFile pic) throws DuplicateException;
//    void saveGuide(GuideDTO guideDTO) throws DuplicateException;

    void updateGuide(GuideDTO guideDTO, MultipartFile nic,
                     MultipartFile guideId,MultipartFile pic) throws NotFoundException;

    void deleteGuide(String guideId) throws NotFoundException;

    void updateGuideStatus(String guideId) throws NotFoundException;
}
