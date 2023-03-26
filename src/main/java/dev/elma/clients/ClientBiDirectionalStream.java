package dev.elma.clients;

import dev.elma.stubs.BankServices;
import dev.elma.stubs.bankServicesGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ClientBiDirectionalStream{
    public static void main(String[] args) throws IOException {
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("localhost", 2001).usePlaintext().build();//Channel with server
        bankServicesGrpc.bankServicesStub bankServicesStub = bankServicesGrpc.newStub(localhost);//Stubs

        StreamObserver<BankServices.messageReq> messageReqStreamObserver = bankServicesStub.fullCurrencyStream(new StreamObserver<BankServices.messageResp>() {
            @Override
            public void onNext(BankServices.messageResp messageResp) {
                System.out.println(messageResp.toString());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("That's The Last One...");
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter=0;
            @Override
            public void run() {
                BankServices.messageReq msg = BankServices.messageReq.newBuilder().setMessageFrom("MAD").setMessageTo("EURO").setAmount(Math.random() + 100).build();
                messageReqStreamObserver.onNext(msg);
                if(counter++==20){
                    messageReqStreamObserver.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);

        System.out.println("Waiting...");
        System.in.read();
    }
}
