package dev.elma.clients;

import dev.elma.stubs.BankServices;
import dev.elma.stubs.bankServicesGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Timer;
import java.util.TimerTask;

public class ClientGrpcCS {
    public static void main(String[] args) {
        ManagedChannel connection = ManagedChannelBuilder.forAddress("localhost", 2001).usePlaintext().build();
        //create Asynchronous Stub
        bankServicesGrpc.bankServicesStub bankServicesStubAsyn = bankServicesGrpc.newStub(connection);

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                bankServicesStubAsyn.perfomCurrencyStream(new StreamObserver<BankServices.messageResp>() {
                    int counter=0;
                    @Override
                    public void onNext(BankServices.messageResp messageResp) {
                        BankServices.messageReq message = BankServices.messageReq.newBuilder().setMessageFrom("MAD").setMessageTo("$").setAmount(Math.random() * 1000).build();
                        onNext(messageResp);
                        if(counter==20){
                            onCompleted();
                            timer.cancel();
                        }
                        System.out.println(messageResp);

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("End");
                    }
                });
            }
        },100,100);

    }
}
