package lk.dhanushkaTa.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.dhanushkaTa.userservice.dto.UserDTO;
import lk.dhanushkaTa.userservice.exception.DuplicateException;
import lk.dhanushkaTa.userservice.exception.NotFoundException;
import lk.dhanushkaTa.userservice.service.UserService;
import lk.dhanushkaTa.userservice.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    @Autowired
    public final UserService userService;

    @Autowired
    public final ObjectMapper objectMapper;

    @GetMapping()
    public String getTest(){
        return "user controller Ok...";
    }

    @GetMapping(path = "find/{userId}")
    public ResponseUtil findUserById(@PathVariable("userId") String userId) throws NotFoundException {
        UserDTO userById = userService.findUserById(userId);
        return new ResponseUtil(200,"User Find",userById);
    }

    @GetMapping(path = "find/all")
    public ResponseUtil getAll(){
        List<UserDTO> all = userService.findAll();
        for (UserDTO userDTO : all){
            System.out.println(userDTO.getUserId()+" : "+userDTO.getUserIdNum());
        }

        return new ResponseUtil(200,"User List",userService.findAll());
    }

    @GetMapping(params = {"detail"})//api/v1/user?detail=xxxxx
    private ResponseUtil findUserByIdOrNic(@RequestParam("detail") String detail){
        return new ResponseUtil(200,"User found",userService.findUserByIdOrNic(detail));
    }

    @PostMapping(path = "save",
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseUtil saveUser(@RequestParam String user,
                                 @RequestParam MultipartFile nic1,
                                 @RequestParam MultipartFile nic2,
                                 @RequestParam MultipartFile pic) throws DuplicateException, JsonProcessingException {
        System.out.println("userDto : "+user);

        UserDTO userDTO = objectMapper.readValue(user, UserDTO.class);
        System.out.println(userDTO);
        userService.saveUser(userDTO,nic1,nic2,pic);
        return new ResponseUtil(200,"User Saved",null);
    }

    @DeleteMapping(path = "delete/{userId}")
    public ResponseUtil deleteUser(@PathVariable("userId")String userId) throws NotFoundException {
        userService.deleteUserById(userId);
        return new ResponseUtil(200,"User Deleted",null);
    }


    @PutMapping(path = "update",
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseUtil updateUser(@RequestParam String user,
                                   @RequestParam MultipartFile nic1,
                                   @RequestParam MultipartFile nic2,
                                   @RequestParam MultipartFile pic) throws NotFoundException, JsonProcessingException {
        System.out.println("userDto : "+user);

        UserDTO userDTO = objectMapper.readValue(user, UserDTO.class);
        System.out.println(userDTO);
        userService.updateUser(userDTO,nic1,nic2,pic);
        return new ResponseUtil(200,"User updated",null);
    }

}
