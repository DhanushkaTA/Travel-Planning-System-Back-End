package lk.dhanushkaTa.userservice.service.impl;

import lk.dhanushkaTa.userservice.dto.FinancialDTO;
import lk.dhanushkaTa.userservice.dto.TravelPackageDTO;
import lk.dhanushkaTa.userservice.feign.TravelPackage;
import lk.dhanushkaTa.userservice.service.FinancialService;
import lk.dhanushkaTa.userservice.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FinancialServiceImpl implements FinancialService {

    @Autowired
    private final TravelPackage travelPackage;

    @Override
    public FinancialDTO getIncomeDetails(String date){
        double hotel = (10 / 100);

        double totalValue=0;

        System.out.println("hotel : "+ hotel);
////////////////////////////////////////////////////////////////////////////////

        ResponseUtil details = travelPackage.getAllPackages();
        List<TravelPackageDTO> packageDTO = (List<TravelPackageDTO>) details.getData();

        for (TravelPackageDTO dto : packageDTO) {
            totalValue=totalValue+dto.getTravelPackage_Value();
        }

//        ResponseUtil details = travelPackage.getDetails("2023");
        System.out.println(details);

        return new FinancialDTO(totalValue,totalValue,null);

    }
}
