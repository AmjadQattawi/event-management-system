package com.eventmanagement.event_management_system.sopa;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;

@WebService(serviceName = "CurrencyService")
public class CurrencySoapService {

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    @WebMethod(operationName = "convertJodToUsd")
    public double convert(@WebParam(name = "amount") double jod) {
        double rate = 1.41;
        return jod * rate;
    }


}
