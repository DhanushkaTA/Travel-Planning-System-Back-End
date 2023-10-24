package lk.dhanushkaTa.guideservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.dhanushkaTa.guideservice.dto.GuideDTO;
import lk.dhanushkaTa.guideservice.exception.DuplicateException;
import lk.dhanushkaTa.guideservice.exception.NotFoundException;
import lk.dhanushkaTa.guideservice.service.GuideService;
import lk.dhanushkaTa.guideservice.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/guide")
@CrossOrigin
@RequiredArgsConstructor
public class GuideController {

    @Autowired
    private final GuideService guideService;

    @Autowired
    public final ObjectMapper objectMapper;

    @GetMapping
    public String ping(){
        return "Guide Controller Ok...";
    }

    @GetMapping(path = "find/{guideId}")
    public ResponseUtil findGuideById(@PathVariable String guideId){
        return new ResponseUtil(200,"Guide Found",guideService.findGuideById(guideId));
    }

    @GetMapping(path = "find/all")
    public ResponseUtil getAllGuides(){
        return new ResponseUtil(200,"Guide list Found",guideService.getAllGuides());
    }

    @PostMapping(path = "save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil saveGuide(@RequestParam String guide,
                                  @RequestParam MultipartFile nic,
                                  @RequestParam MultipartFile guideId,
                                  @RequestParam MultipartFile pic) throws DuplicateException, JsonProcessingException {
        GuideDTO guideDTO = objectMapper.readValue(guide, GuideDTO.class);
        guideService.saveGuide(guideDTO,nic,guideId,pic);
        return new ResponseUtil(200,"Guide saved",null);
    }

    @PutMapping(path = "update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil updateGuide(@RequestParam String guide,
                                    @RequestParam MultipartFile nic,
                                    @RequestParam MultipartFile guideId,
                                    @RequestParam MultipartFile pic) throws NotFoundException, JsonProcessingException {
        GuideDTO guideDTO = objectMapper.readValue(guide, GuideDTO.class);
        guideService.updateGuide(guideDTO,nic,guideId,pic);
        return new ResponseUtil(200,"Guide updated",null);
    }

    @PutMapping(path = "update/status/{guideId}")
    public ResponseUtil updateGuideStatus(@PathVariable String guideId) throws NotFoundException {
        guideService.updateGuideStatus(guideId);
        return new ResponseUtil(200,"Update Status",null);
    }

    @DeleteMapping(path = "delete/{guideID}")
    public ResponseUtil deleteGuide(@PathVariable String guideID) throws NotFoundException {
        guideService.deleteGuide(guideID);
        return new ResponseUtil(200,"Guide deleted",null);
    }


}
