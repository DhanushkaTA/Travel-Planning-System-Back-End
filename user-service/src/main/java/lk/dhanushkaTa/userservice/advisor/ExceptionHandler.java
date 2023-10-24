package lk.dhanushkaTa.userservice.advisor;

import lk.dhanushkaTa.userservice.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@CrossOrigin
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public ResponseUtil sendResponse(Exception e){
        return new ResponseUtil(500,e.getMessage(),null);
    }
}
