import java.io.*;
import java.util.*;
import java.lang.*;

public class ConsumerProducer{
        public static void main(String args[]){
                // create a new producer object
                Producer producer = new Producer();
        }
}

class Producer extends Thread {

        // create message queue (buffer)
        int sizeOfBuffer = 5;
        ArrayList<Integer> sharedBuffer;
        // initialize Thread
        Thread producerThread;

        // create producer object
        Producer(){
                sharedBuffer = new ArrayList<Integer>(sizeOfBuffer);
                producerThread = new Thread(this); // create thread with current object
                producerThread.start(); // start thread
        }

        public void run(){ // produce data and add it to buffer that we initizlized above
                int i = 0;
                while(true)
                {
                        synchronized (sharedBuffer) {
                                while(sharedBuffer.size() == sizeOfBuffer)
                                {
                                        try
                                        {
                                                System.out.println("Queue is Full...");
                                                sharedBuffer.wait();
                                        }
                                        catch(InterruptedException e)
                                        {
                                                e.printStackTrace();
                                        }
                                }
                                System.out.println("Producing Value: " + i);
                                sharedBuffer.add(i++);
                                sharedBuffer.notify();
                        }
                }

                Consumer consumer = new Consumer(this);
                producerThread = new Thread(consumer);
                consumer.start();
        }
}

class Consumer extends Thread {
        Producer producer;

        // create consumer constructor
        Consumer(Producer temp) {
                producer = temp;
        }

        public void run() {

                try {
                        for (int i = 0; i < producer.sizeOfBuffer; i++) {
                                System.out.println(producer.sharedBuffer.charAt(i) + "")
                        }
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                System.out.println("Buffer is Empty...");
        }
}