package com.eventmanagement.event_management_system;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.ws.Endpoint;

@WebService
public class SoapTest {

    @WebMethod
    public double convertJodToUsd(@WebParam(name = "amount") double jod) {
        return jod * 1.41;
    }

    public static void main(String[] args) {
         String url = "http://localhost:8085/ws/currency";
        System.out.println("جاري تشغيل سيرفر الـ SOAP...");
        Endpoint.publish(url, new SoapTest());

        System.out.println("الرابط صار جاهز! انسخه وحطه في SoapUI:");
        System.out.println(url + "?wsdl");
    }



}
