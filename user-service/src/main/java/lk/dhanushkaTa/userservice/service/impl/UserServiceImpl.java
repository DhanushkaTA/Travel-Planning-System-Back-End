package lk.dhanushkaTa.userservice.service.impl;

import lk.dhanushkaTa.userservice.dto.UserDTO;
import lk.dhanushkaTa.userservice.entity.User;
import lk.dhanushkaTa.userservice.exception.DuplicateException;
import lk.dhanushkaTa.userservice.exception.NotFoundException;
import lk.dhanushkaTa.userservice.repository.UserRepository;
import lk.dhanushkaTa.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
public class UserServiceImpl implements UserService {

    @Autowired
    public final UserRepository userRepository;

    @Autowired
    public final ModelMapper modelMapper;

    @Override
    public boolean saveUser(UserDTO userDTO,
                            MultipartFile nic1,
                            MultipartFile nic2, MultipartFile pic) throws DuplicateException {
        if(userDTO==null){
            throw new RuntimeException("UserDto null From UserService");
        }

        if (userRepository.findById(userDTO.getUserId()).isPresent()) {//return Optional<User>
            throw new DuplicateException("Customer AllReady Exists From UserService");
        }

        User user= this.handleFile(nic1,nic2,pic,modelMapper.map(userDTO, User.class));

        userRepository.save(user);
        return true;
    }

    @Override
    public UserDTO findUserById(String userId) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()){
            throw new NotFoundException("Customer Couldn't find");
        }

        return this.convertPathToByte(user.get());
    }

    @Override
    public UserDTO findUserByIdOrNic(String detail) {
        User user= userRepository.findUserByUserIdOrUserIdNum(detail, detail);
        return this.convertPathToByte(user);
//        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> findUserByIdOrNicLike(String detail) {
        return modelMapper.map(userRepository.findUserByUserIdLikeOrUserIdNumLike(detail,detail),
                new TypeToken<List<UserDTO>>(){}.getType());
    }

    @Override
    public void updateUser(UserDTO userDTO,
                           MultipartFile nic1,MultipartFile nic2,MultipartFile pic) throws NotFoundException {
        if (!userRepository.existsById(userDTO.getUserId())){
            throw new NotFoundException("User couldn't found");
        }

//        User user = this.handleFile(nic1, nic2, pic, modelMapper.map(userDTO, User.class));
        userRepository.save(
                this.handleFile(nic1, nic2, pic, modelMapper.map(userDTO, User.class)
                ));
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> collect =
                userRepository.findAll().stream().map(this::convertPathToByte).collect(Collectors.toList());
        return userRepository.findAll().stream().map(this::convertPathToByte).collect(Collectors.toList());

//        return modelMapper.map(userRepository.findAll(),new TypeToken<List<UserDTO>>(){}.getType());
    }

    @Override
    public void deleteUserById(String userId) throws NotFoundException {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        }else {
            throw new NotFoundException("User not found");
        }
    }

    private User handleFile(MultipartFile nic1, MultipartFile nic2, MultipartFile pic, User user) {

        List<String>paths=new ArrayList<>();
        List<MultipartFile>files=new ArrayList<>();
        files.add(nic1);
        files.add(nic2);
        files.add(pic);

        try {
            String uploadPathDer="E:\\IJSE\\AAD\\image\\user\\"+user.getUserId();
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

            user.setNicImage1(paths.get(0));
            user.setNicImage2(paths.get(1));
            user.setProfileImage(paths.get(2));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private UserDTO convertPathToByte(User user) {

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        try {
            userDTO.setProfileImage(Files.readAllBytes(Paths.get(user.getProfileImage()).toFile().toPath()));
            userDTO.setNicImage1(Files.readAllBytes(Paths.get(user.getNicImage1()).toFile().toPath()));
            userDTO.setNicImage2(Files.readAllBytes(Paths.get(user.getNicImage2()).toFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userDTO;
    }
}
