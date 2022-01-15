
package com.miola.mcr.MqttCnxn;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttBeans {
        @Bean
        public MqttPahoClientFactory mqttClientFactory() {
            DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
            //  	We recommend configuring an MqttConnectOptions object and injecting it into the factory, instead of
            //  	setting the (deprecated) options on the factory itself.
            MqttConnectOptions options = new MqttConnectOptions();
            //The broker URL.
            options.setServerURIs(new String[] {"tcp://localhost:1883"});
            options.setUserName("admin");
            String pass = "1234";
            options.setPassword(pass.toCharArray());
            options.setCleanSession(true);

            factory.setConnectionOptions(options);
            return factory;
        }
        @Bean
        public MessageChannel mqttInputChannel() {
            return new DirectChannel();
        }

        @Bean
        public MessageChannel mqttInputChannel2() {
        return new DirectChannel();
    }

        @Bean
        public MessageProducerSupport inbound() {
            // ClientId : uniquely identifies our client.
            MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                    mqttClientFactory(), "DbEnergy");

            adapter.setCompletionTimeout(5000);
            adapter.setConverter(new DefaultPahoMessageConverter());
            //QoS values(0,1,2).
            // 0 : The client simply publishes the message, and there is no acknowledgement by the broker.
            // 1 : The broker sends an acknowledgement back to the sender, but in the event that that the acknowledgement
            // is lost the sender won't realise the message has got through, so will send the message again. The client
            // will re-send until it gets the broker's acknowledgement.
            // 2 : This is the highest level of service, in which there is a sequence of four messages between the sender and
            // the receiver, a kind of handshake
            // to confirm that the main message has been sent and that the acknowledgement has been received.
            adapter.setQos(2);
            adapter.setOutputChannel(mqttInputChannel());
            return adapter;
        }

    @Bean
    public MessageProducerSupport inbound2() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn2",
                mqttClientFactory(), "DbAir");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel2());
        return adapter;
    }


        /*@Bean
        @ServiceActivator(inputChannel = "mqttInputChannel")
        public MessageHandler handler() {
            return new MessageHandler() {

                @Override
                public void handleMessage(Message<?> message) throws MessagingException {
                    String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                    if(topic.equals("myTopic")) {
                        System.out.println("This is the topic");
                    }
                    *//*if(topic.equals("myTopic2")) {
                        System.out.println("This is the topic2");
                    }*//*
                    System.out.println(message.getPayload());
                }
            };
        }*/



    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlows.from(inbound())
                .transform(p -> p)
                .handle("DBEnergyService","handleHere")
                .get();
    }

    @Bean
    public IntegrationFlow mqttInFlow2() {
        return IntegrationFlows.from(inbound2())
                .transform(p -> p)
                .handle("DBAirService","handleHere2")
                .get();
    }


        @Bean
        public MessageChannel mqttOutboundChannel() {
            return new DirectChannel();
        }

        @Bean
        @ServiceActivator(inputChannel = "mqttOutboundChannel")
        public MessageHandler mqttOutbound() {
            //clientId is generated using a random number
            MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());
            messageHandler.setAsync(true);
            messageHandler.setDefaultTopic("#");
            messageHandler.setDefaultRetained(false);
            return messageHandler;
        }



}
