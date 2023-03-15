package dev.elma.clients;

import dev.elma.stubs.BankServices;
import dev.elma.stubs.bankServicesGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class ClientGrpcSStream {
    public static void main(String[] args) throws IOException {
        ManagedChannel connection = ManagedChannelBuilder.forAddress("localhost", 2001).usePlaintext().build();
        bankServicesGrpc.bankServicesStub bankServicesStub = bankServicesGrpc.newStub(connection);
        
        //create Request:
        BankServices.messageReq mesgReq  = BankServices.messageReq.newBuilder().
                setMessageTo("MAD").
                setMessageTo("EURO").
                setAmount(200).build();

        bankServicesStub.getCurrencyStream(mesgReq, new StreamObserver<BankServices.messageResp>() {
            @Override
            public void onNext(BankServices.messageResp messageResp) {
                System.out.println(messageResp.toString());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("The Last One...");
            }
        });

        System.out.println("Waiting the Response...");
        System.in.read();

    }
}
