package lk.dhanushkaTa.guideservice.service.impl;

import lk.dhanushkaTa.guideservice.dto.GuideDTO;
import lk.dhanushkaTa.guideservice.entity.Guide;
import lk.dhanushkaTa.guideservice.exception.DuplicateException;
import lk.dhanushkaTa.guideservice.exception.NotFoundException;
import lk.dhanushkaTa.guideservice.repository.GuideRepository;
import lk.dhanushkaTa.guideservice.service.GuideService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {

    @Autowired
    private final GuideRepository guideRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public GuideDTO findGuideById(String guideId) {
//        Optional<Guide> guide = guideRepository.findById(guideId);
        return guideRepository.findById(guideId).map(this::convertPathToByte).orElse(null);
//        return guide.map(value -> modelMapper.map(value, GuideDTO.class)).orElse(null);
    }

    private GuideDTO convertPathToByte(Guide guide) {

        GuideDTO guideDTO = modelMapper.map(guide, GuideDTO.class);
        try {
            guideDTO.setGuideImage(Files.readAllBytes(Paths.get(guide.getGuideImage()).toFile().toPath()));
            guideDTO.setGuideIDImage(Files.readAllBytes(Paths.get(guide.getGuideIDImage()).toFile().toPath()));
            guideDTO.setGuideNICImage(Files.readAllBytes(Paths.get(guide.getGuideNICImage()).toFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return guideDTO;
    }

    @Override
    public List<GuideDTO> getAllGuides() {
//        List<GuideDTO> collect = guideRepository.findAll().stream().
//                map(this::convertPathToByte).collect(Collectors.toList());
        return guideRepository.findAll().stream().
                map(this::convertPathToByte).collect(Collectors.toList());
    }

    @Override
    public void saveGuide(GuideDTO guideDTO,
                          MultipartFile nic,
                          MultipartFile guideId,MultipartFile pic) throws DuplicateException {
        if (guideRepository.existsById(guideDTO.getGuideId())){
            throw new DuplicateException("Customer Already exits");
        }

//        guideRepository.save(modelMapper.map(guideDTO, Guide.class));
        guideRepository.save(this.handleFile(nic,guideId,pic,modelMapper.map(guideDTO, Guide.class)));
    }

    @Override
    public void updateGuide(GuideDTO guideDTO, MultipartFile nic,
                            MultipartFile guideId,MultipartFile pic) throws NotFoundException {
        if (!guideRepository.existsById(guideDTO.getGuideId())){
            throw new NotFoundException("Customer couldn't found");
        }

        guideRepository.save(
                this.handleFile(nic,guideId,pic,modelMapper.map(guideDTO, Guide.class)));
    }

    @Override
    public void deleteGuide(String guideId) throws NotFoundException {
        if (!guideRepository.existsById(guideId)){
            throw new NotFoundException("Customer couldn't found");
        }
        guideRepository.deleteById(guideId);
    }

    @Override
    public void updateGuideStatus(String guideId) throws NotFoundException {
        if (!guideRepository.existsById(guideId)){
            throw new NotFoundException("Customer couldn't found");
        }
        Optional<Guide> guideById = guideRepository.findById(guideId);
        Guide guide = guideById.get();
        if (guide.getGuideStatus().equalsIgnoreCase("eligible")){
            guide.setGuideStatus("NotEligible");
        }else {
            guide.setGuideStatus("Eligible");
        }
        guideRepository.save(guide);
    }

    private Guide handleFile(MultipartFile nic, MultipartFile guideId, MultipartFile pic, Guide guide) {

        List<String>paths=new ArrayList<>();
        List<MultipartFile>files=new ArrayList<>();
        files.add(nic);
        files.add(guideId);
        files.add(pic);

        try {
            String uploadPathDer="E:\\IJSE\\AAD\\image\\guide\\"+guide.getGuideId();
            Path uploadPath = Paths.get(uploadPathDer);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }else {
                FileUtils.deleteDirectory(new File(uploadPathDer));
                Files.createDirectories(uploadPath);
            }

            for (int i=0;i<3;i++){
                byte[] bytes = files.get(i).getBytes();
                Path path = Paths.get(uploadPath +"\\"+ files.get(i).getOriginalFilename());
                Files.write(path,bytes);
                paths.add(path.toString());
            }

            guide.setGuideNICImage(paths.get(0));
            guide.setGuideIDImage(paths.get(1));
            guide.setGuideImage(paths.get(2));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(guide);

        return guide;
    }
}
