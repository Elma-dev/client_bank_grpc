package dev.elma.clients;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import dev.elma.stubs.BankServices;
import dev.elma.stubs.bankServicesGrpc;

public class ClientGrpcUnary {
    public static void main(String[] args) {
        //create connection with server
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("localhost", 2001).usePlaintext().build();
        //create stub between client and srv
        bankServicesGrpc.bankServicesBlockingStub bankServicesBlockingStub = bankServicesGrpc.newBlockingStub(localhost);
        //create req msg
        BankServices.messageReq messageReq=BankServices.messageReq.newBuilder().
                setMessageFrom("MAD").setMessageTo("$").setAmount(200).build();
        //create resp msg
        BankServices.messageResp messageResp=bankServicesBlockingStub.convert(messageReq);


        //show the response
        System.out.println(messageResp);

    }
}
