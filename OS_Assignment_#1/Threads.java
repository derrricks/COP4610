import java.io.*;
import java.util.*;

public class Main{
        public static void main(String args[]){

              // create a new buffer to share with producer & consumer
              ArrayList<Integer> sharedBuffer = new ArrayList<Integer>();
              int maxSize = 5; // select max size of buffer

              // create producer/consumer object
              Producer producer = new Producer(sharedBuffer,maxSize);
              Consumer consumer = new Consumer(sharedBuffer,maxSize);
                
              // create threads
              Thread t1 = new Thread(producer);
              Thread t2 = new Thread(consumer);
                
              // start threads
              t1.start();
              t2.start();      
        }
}

class Producer implements Runnable  {


        int maxSizeOfBuffer = 0;
        ArrayList<Integer> pbuffer;

        // create producer object
        public Producer(ArrayList<Integer> buffer, int max){
                pbuffer = buffer;
                maxSizeOfBuffer = max;
                }

        public void run(){ // produce data and add it to buffer that we initizlized above
                int i = 0;
                while(true)
                {
                        synchronized (pbuffer) {
                                while(pbuffer.size() == maxSizeOfBuffer)
                                {
                                        try
                                        {
                                                System.out.println("Buffer is Full... Producer is Waiting...");
                                                pbuffer.wait();
                                                i = 0;
                                        }
                                        catch(InterruptedException e)
                                        {
                                                e.printStackTrace();
                                        }
                                }

                                  if(pbuffer.size() > maxSizeOfBuffer){
                                    System.out.println("Error... Producing Past Max Size of Buffer");
                                  }
                                
                                ++i;
                                System.out.println("Producing Value: " + i + " of " + maxSizeOfBuffer);
                                pbuffer.add(i);
                                pbuffer.notify();

                                if(pbuffer.size() == maxSizeOfBuffer){
                                  System.out.println("Buffer is now Full...");
                                }
                        }
                }

              
        }
}

class Consumer implements Runnable {
    
      int maxSizeOfBuffer = 0;
      ArrayList<Integer> cbuffer;
      // create consumer constructor
      public Consumer(ArrayList<Integer> buffer, int max) {
        cbuffer = buffer;
        maxSizeOfBuffer = max;
      }

      public void run() {

        while(true) {
            synchronized (cbuffer) {
               while(cbuffer.isEmpty()) {
                 
                try {
                    System.out.println("Buffer is Empty... Consumer is Waiting...");
                    cbuffer.wait();
                } catch (InterruptedException e) {
                    	e.printStackTrace();
                      }
                }  

                  if(cbuffer.isEmpty()){
                   System.out.println("Error... Consuming Past Beggining of Buffer");
                  }

                if(!(cbuffer.isEmpty())) {
                System.out.println("Consuming Value: " + cbuffer.remove(0) + " of " + maxSizeOfBuffer);
                cbuffer.notify();
                }
            }
        }
      }
}
