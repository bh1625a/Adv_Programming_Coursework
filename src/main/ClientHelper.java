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
                        if (inputStream.equals("/COORDINATOR")){
                            String coordinatorInfo = in.nextLine();
                            String[] coordArray = coordinatorInfo.split(":");
                            String coordValue = in.nextLine();
                            if (coordValue.equals("/COORDINATORTRUE")){
                                client.setCoordinator(true);
                                client.chatWindowFirstMember();
                                client.sendClientPing();

                            }
                        } else if (inputStream.equals("/ALLUSERS")) {
                            String incomingID = in.nextLine();
                            memberList = createIDListFromInput(incomingID);
                            if (memberList.size() >= 1) {
                                coordinator.buildHashMap();
                            }
                            client.UpdateOnlineUsers(memberList);
                            if (incomingID.contains("/END")){
                                break;
                            }
                        } else if (inputStream.equals("/RECEIVEMESSAGE")){
                            String message = in.nextLine();
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
                            client.sendClientPong();
                        }
                        else if (inputStream.contains("/PONG")){
                            coordinator.checkPong(inputStream);
                        } else if (inputStream.contains("/REMOVEHASH")){
                            String[] parts = inputStream.split(":");
                            String id = parts[1];
                            coordinator.remove(id);
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> createIDListFromInput(String receivedIDList){
        /**
         * Returns an arraylist of currently connected id's
         * Builds the list from information provided on the input stream
         * Adds all id's that do not equal "/END"
         * @param receivedIDList The input stream of id's separated by commas.
         *
         */
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
        /**
         * Returns an ArrayList containing all currently connected id's.
         * @return this.memberlist The list of currently connected ID's
         */
        return this.memberList;
    }

    public synchronized boolean getConnected(){
        /**
         * Returns the current connection state of the client.
         * Synchonized to work across multiple threads
         */
        return this.isConnected;
    }

    public synchronized void setConnected(boolean value){
        /**
         * Sets the connection status for the current client
         * @param value A boolean depending on the current connection status of the client
         */
        this.isConnected = value;
    }

    public synchronized Scanner getInputStream(){
        return this.in;
    }

    public synchronized void setInputStream(Scanner inputStream){
        /**
         * Allows the inputStream to be set by the client
         * @param inputStream The inputStream being set
          */
        this.in = inputStream;
    }

    public Coordinator getCoordinator(){
        /**
         * Returns a coordinator object that contains information about the current coordinator
         * @return this.coordinator The current coordinator
         */
        return this.coordinator;
    }

}
