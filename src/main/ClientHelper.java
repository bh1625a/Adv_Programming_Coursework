package main;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientHelper extends Thread {
    private Client client;
    private Scanner in;
    private ArrayList<String> memberList;
    private boolean isConnected = false;
    private Coordinator coordinator;


    public ClientHelper(){
        super.start();
    }

    public void run(){
        try {
            client = new Client(this);
            coordinator = new Coordinator(this, this.client);
            while (true){
                if(getConnected()) {
                    while (in.hasNextLine()) {
                        String inputStream = in.nextLine();
                        System.out.println(inputStream);
                        System.out.println("-----------------------------------------------------------");
                        if (inputStream.equals("/COORDINATOR")){
                            String coordinatorInfo = in.nextLine();
                            String[] coordArray = coordinatorInfo.split(":");
                            String coordValue = in.nextLine();
                            if (coordValue.equals("/COORDINATORTRUE")){
                                client.setCoordinator(true);
                                client.chatWindowFirstMember();
                                client.sendClientPing();
                                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                            }
                        } else if (inputStream.equals("/ALLUSERS")) {
                            String incomingID = in.nextLine();
                            System.out.println("IncomingID: " + incomingID);
                            memberList = createIDListFromInput(incomingID);
//                            coordinator.buildHashMap();
                            client.UpdateOnlineUsers(memberList);
                            if (incomingID.contains("/END")){
                                break;
                            }
                        } else if (inputStream.equals("/RECEIVEMESSAGE")){
                            String message = in.nextLine();
                            System.out.println("Message received by client: " + message);
                            client.UpdateChatWindow(message);

                        } else if (inputStream.equals("/SENDCOORDINATORDETAILS")){
                            String coordDetails = in.nextLine();
                            String[] parts = coordDetails.split(":");

                            coordinator.setId(parts[0]);
                            coordinator.setPort(Integer.valueOf(parts[1]));
                            coordinator.setIp(parts[2]);
                            coordinator.buildHashMap(); // Set all counters to 0 for each user

                            client.updateCoordinatorDetails(coordinator.getId(), coordinator.getPort(), coordinator.getIp());
                        } else if (inputStream.equals("/PING")){
                            System.out.println("RECEIVED THE PING. ATTEMPTING TO SEND PONG");
                            client.sendClientPong();
                        }
                        else if (inputStream.contains("/PONG")){
                            System.out.println("RECEIVED PONG");
                            coordinator.checkPong(inputStream);
                        }


                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> createIDListFromInput(String receivedIDList){
        ArrayList<String> updatedList = new ArrayList<>();
        String[] parts = receivedIDList.split(",");
        for(String id : parts){
            if(!(id.equals("/END"))){
                updatedList.add(id);
            }
        }
        return updatedList;
    }



    public ArrayList<String> getMemberList(){
        return this.memberList;
    }

    public synchronized boolean getConnected(){
        return this.isConnected;
    }

    public synchronized void setConnected(boolean value){
        this.isConnected = value;
    }

    public synchronized Scanner getInputStream(){
        return this.in;
    }

    public synchronized void setInputStream(Scanner inputStream){
        this.in = inputStream;
    }

    public Coordinator getCoordinator(){
        return this.coordinator;
    }


}
