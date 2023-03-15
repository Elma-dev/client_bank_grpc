package dev.elma.clients;

import dev.elma.stubs.BankServices;
import dev.elma.stubs.bankServicesGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ClientGrpcCS {
    public static void main(String[] args) throws IOException {
        ManagedChannel localhost =ManagedChannelBuilder.forAddress("localhost",2001).usePlaintext().build();
        bankServicesGrpc.bankServicesStub bankServicesStub = bankServicesGrpc.newStub(localhost);

        StreamObserver<BankServices.messageReq> messageReqStreamObserver = bankServicesStub.perfomCurrencyStream(new StreamObserver<BankServices.messageResp>() {
            @Override
            public void onNext(BankServices.messageResp messageResp) {
                System.out.println("Response of server is: "+messageResp.getResult());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                //System.out.println("The last One...");
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter=0;
            @Override
            public void run() {
                BankServices.messageReq messageReq = BankServices.messageReq.newBuilder().
                        setMessageFrom("MAD").setMessageTo("EURO").setAmount(Math.random() * 100).build();
                messageReqStreamObserver.onNext(messageReq);
                if(counter++==20){
                    messageReqStreamObserver.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);

        System.out.println("-----------Wait Resp----------");
        System.in.read();


    }
}
