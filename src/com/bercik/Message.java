package com.bercik;

public class Message
{

private String text;

private String senderID;
private int sequenceNum;

//Wzorzec wymyslonego "protocolu".
private final static String pattern = "(.*?)\\|\\|sender:(.*?),seq:(\\d+)";

public Message(String text, String senderID, int sequenceNum)
{
   this.text = text;
   this.senderID = senderID;
   this.sequenceNum = sequenceNum;
}

public static Message parseTransmissionString(String receivedText)
{
   assert(receivedText.matches(pattern));
   String text = receivedText.replaceFirst(pattern, "$1");
   String ID = receivedText.replaceFirst(pattern, "$2");
   int seq = Integer.parseInt(receivedText.replaceFirst(pattern, "$3"));
   return new Message(text, ID, seq);
}

public String getSenderID()
{
   return senderID;
}

public int getSequenceNumber()
{
   return sequenceNum;
}

public boolean equals(Object o)
{
   if (!(o instanceof Message)) return false;
   Message other = (Message)o;
   return this.senderID.equals(other.senderID) && this.sequenceNum==other.sequenceNum;
}

public String toString()
{
   return text;
}

public String transmissionString()
{
   return text + "||sender:" + senderID + ",seq:" + sequenceNum;
}


public static void main(String [] args)
{
   Message m1 = new Message("Witaj", "Pierwszy", 1);
   System.out.println(m1.transmissionString());
   Message m2 = Message.parseTransmissionString(m1.transmissionString());
  // Message m2 = new Message("Siema", "Drugi", 2);
   System.out.println("m1 równe? m2: " + m1.equals(m2));
}
}