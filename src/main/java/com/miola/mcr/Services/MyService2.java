package com.miola.mcr.Services;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MyService2 {

    @ServiceActivator(inputChannel="mqttInputChannel2")
    public void handleHere2(@Payload Object mess) {
        System.out.println("payload 2 : "+mess);
        //return mess;
    }
}