package lk.dhanushkaTa.userservice.controller;

import lk.dhanushkaTa.userservice.dto.FinancialDTO;
import lk.dhanushkaTa.userservice.service.FinancialService;
import lk.dhanushkaTa.userservice.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/financial")
@CrossOrigin
@RequiredArgsConstructor
public class FinancialController {

    @Autowired
    private final FinancialService financialService;

    @GetMapping(path = "find",params = "date")
    public ResponseUtil getDetails(@RequestParam String date){
        FinancialDTO incomeDetails = financialService.getIncomeDetails(date);
        return new ResponseUtil(200,"Details",null);
    }

    @GetMapping()
    public String ping(){
        return "Financial Controller Ok...";
    }
}
