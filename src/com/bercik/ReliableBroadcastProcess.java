package com.bercik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class ReliableBroadcastProcess
{  
  
   private List<Message> messagesReceived;
   
   protected String procID;
   
   protected int messageSentCount;
   
   
   private InetAddress address;
   private MulticastSocket socket;
   private static int SOCKET = 4446;
   

   public ReliableBroadcastProcess(String ID, String groupAddress)
   {
      procID = ID;
      messageSentCount = 0;
      messagesReceived = new ArrayList<Message>();
      try
      {
         socket = new MulticastSocket(SOCKET);
         address = InetAddress.getByName(groupAddress);
         socket.joinGroup(address);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public ReliableBroadcastProcess(String ID)
   {
      this(ID, "230.0.0.1");
   }
   

   public void sendToAll(Message m)
   {
      byte[] buf = m.transmissionString().getBytes();
      try
      {
         DatagramPacket packet = new DatagramPacket(buf, buf.length, address, SOCKET);
         socket.send(packet);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   

   public void receive(Message m)
   {
      if (!messagesReceived.contains(m))
      {
         messagesReceived.add(m);
         sendToAll(m);
         deliver(m);
      }
   }

   public void broadcast(String messageText)
   {
      Message m = new Message(messageText, procID, messageSentCount++);
      sendToAll(m);
   }
   

   public void deliver(Message m)
   {
      System.out.println("  Delivered: " + m);
   }
   
   
  
   public void start()
   {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      startListenerThread();
      while (true)
      {
         try
         {
            String messageText = reader.readLine();
            broadcast(messageText);
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }
   }
   

   protected void startListenerThread()
   {
      RBListenerThread thread = new RBListenerThread(this, address, SOCKET);
      thread.start();
   }
   

   public static void main(String [] args)
   {
      if (args.length < 0)
      {
         System.out.println("U¿ycie w cmd: java ReliableBroadcastProcess <Proces>");
         System.exit(0);
      }
      ReliableBroadcastProcess rbp = new ReliableBroadcastProcess("Pierwszy");
      rbp.start();
   }
}