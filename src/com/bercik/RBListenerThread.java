package com.bercik;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class RBListenerThread extends Thread
{  
   private ReliableBroadcastProcess rbp;
   
   private int socketNum;
   private InetAddress address;

   public RBListenerThread(ReliableBroadcastProcess rbp, InetAddress address, int socketNum)
   {
      this.rbp = rbp;
      this.address = address;
      this.socketNum = socketNum;
   }

   public void run()
   {
      MulticastSocket socket;
      try
      {
         socket = new MulticastSocket(socketNum);
         socket.joinGroup(address);
   
         DatagramPacket packet;
         while (true)
         {
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(packet.getData());
            
            Message m = Message.parseTransmissionString(received.trim());
            
            //wiadomosc wysylam do procesu RB .
            rbp.receive(m);
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

}