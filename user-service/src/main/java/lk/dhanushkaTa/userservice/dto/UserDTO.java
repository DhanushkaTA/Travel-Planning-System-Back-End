package lk.dhanushkaTa.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserDTO {
    private String userId;
    private String userFullName;
    private String username;
    private String userPassword;
    private String userIdNum;
    private String userIdType;
    private String userEmail;
    private String userAddress;
    private String userDob;
    private String userGender;
    private String userContactNum;
    private byte[] nicImage1;
    private byte[] nicImage2;
    private byte[] profileImage;
}
