package com.bucket.thebucket;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


public class Schedule {


    static int ERR_BAD_INPUT = -1;

    final int ERR_CURRENT_STATE_DISCOVERED = 1;
    final int ERR_CURRENT_STATE_CONNECTED= 2;
    final int ERR_CURRENT_STATE_DISCONNECTED = 3;
    final int ERR_DEVICE_NOT_NETWORKED = 4;
    final int ERR_DEVICE_ALREADY_NETWORKED = 5;
    final int MAX_NETWORK_SIZE_REACHED = 6;
    final int ERR_STATE_NOT_RECOGNIZED = 7;
    final int ERR_DEVICE_NEVER_CONNECTED = 8;
    final int ERR_DEVICE_REVISION_UNCHANGED = 9;
    final int ERR_RECORD_MISSING_INFORMATION = 10;


    final int MAX_LOCALLY_NETWORKED_SIZE = 50;

    static int ERR_SERVICE_ALREADY_REGISTERED = 20;
    static int ERR_NO_SERVICE_FOUND = 21;
    static int ERR_SERVICE_NOT_REGISTERED = 22;
    static int ERR_SERVICE_ALREADY_ON = 23;
    static int ERR_SERVICE_NOT_ON = 24;

    NetworkCallsAndData localNetwork = new NetworkCallsAndData();


    public class P2pDiscovery {

        private void discoverService(){

            final LocallyNetworkedDevice localDevice = new LocallyNetworkedDevice;
            WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
                @Override
                public void onDnsSdTxtRecordAvailable(String fullDomain, Map<String, String> record, WifiP2pDevice wifiP2pDevice) {

                    if(!record.containsKey("DEVICE_TAG")) { return; }

                    else if(!record.containsKey("OVERALL_REVISION")) { return; }

                    if(localNetwork.locallyNetworkedDeviceLookupTable.contains(record.get("DEVICE_TAG"))) {
                        localDevice[0] = localNetwork.locallyNetworkedDeviceLookupTable.get(record.get("DEVICE_TAG"));

                        if(localDevice[0].overallRevision < Integer.parseInt(record.get("OVERALL_REVISION")) ) {
                            ////////////////////////////////
                        }
                    }

                }
            };
        }

    }


    public class LocallyNetworkedDevice {

        public LocallyNetworkedDevice(){

        }

        //include discovered, connected, disconnected
        public String state;

        //says if the device in question current has outgoing or incoming requests
        //and also the queues
        private DeviceDownloadState downloadState;

        Map<String,String> services;

        LinkedList<Object> receivedRequests;
        LinkedList<Object> receivedData;

        Map<String, String> p2pRecord;


        int overallRevision;

        Map<String, Integer> serviceRevisions;

        WifiP2pInfo wifiP2pInfo;
        WifiP2pDevice wifiP2pDevice;

        //ArrayList<FileNodeObject> recommendedIncomingFiles;
        //ArrayList<FileNodeObject> recommendedOutgoingFiles;

        //this will be the network schedule for this device
        Object schedule;

    }

        class DeviceDownloadState{
            String incommingState;

            String outgoingState;

            ArrayList<FileNodeObject> incomingQueue;

            ArrayList<FileNodeObject> outgoingQueue;

        }
    public class NetworkCallsAndData {


        LocallyNetworkedDevice localDevice;

        public void discoverOn(){


            WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
                @Override

                //record availab
                public void onDnsSdTxtRecordAvailable(String fullDomain, Map<String, String> record, WifiP2pDevice wifiP2pDevice) {

                    if(!record.containsKey("DEVICE_TAG")) { return; }

                    else if(!record.containsKey("OVERALL_REVISION")) { return; }

                    if(localNetwork.locallyNetworkedDeviceLookupTable.contains(record.get("DEVICE_TAG"))) {
                        //the device is currently networked so the record information must be checked
                        localDevice = localNetwork.locallyNetworkedDeviceLookupTable.get(record.get("DEVICE_TAG"));

                        if(localDevice.overallRevision < Integer.parseInt(record.get("OVERALL_REVISION")) ) {
                            //an increased revision means some service info must be changed, iterate through
                            Map<String, String> newServiceMap = new HashMap<>();

                            for(Map.Entry<String,String> o : record.entrySet()) {
                                //iterate through each record entry
                                if( o.getKey().contains("SERVICE:")) {
                                    //only concerned with the services
                                    //populate the service map
                                    newServiceMap.put(o.getKey().split(":")[1] , o.getValue());

                                    //if the local device is already associated with the service, check the revision for updates
                                    if(localDevice.services.containsKey(o.getKey().split(":")[1])) {
                                        if(Integer.parseInt(localDevice.services.get(o.getKey().split(":")[1])) < Integer.parseInt(o.getValue()) ) {


                                        }
                                    }
                                }
                            }

                        }
                    }

                }
            };
        }

        public class QueueUpdate {

            ArrayList<FileNodeObject> incomingFilesToAdd;
            ArrayList<FileNodeObject> incomingFilesToRemove;

            ArrayList<FileNodeObject> outgoingFilesToAdd;
            ArrayList<FileNodeObject> outgoingFilesToRemove;
        }


            //TO POINT TO THE OBJECT IN THE LINKED LISTS, DOES NOT GIVE LOCATION IN LINKED LIST
        Hashtable<String, LocallyNetworkedDevice> locallyNetworkedDeviceLookupTable;

        //CONSIDER SELF BALANCING TREES FOR O(lg(n)) lookup
        //ALL DEVICE, POINTS TO THE OTHER LINKED LISTS
        LinkedList<LocallyNetworkedDevice> allLocallyNetworkedDevices;

        //discovered are P2P only, has not gone through the connection and establishment of all info
        LinkedList<LocallyNetworkedDevice> currentlyDiscoveredDevices;

        //All devices currently passed the P2P stages
        LinkedList<LocallyNetworkedDevice> currentlyConnectedDevices;

        //keep in this list for X time then remove entirely
        LinkedList<LocallyNetworkedDevice> currentlyDisconnectedDevices;

        //Includes list of devices currently involved with exchanges
        //THIS IS LESS IMPORTANT, CAN BE MERGED WITH CONNECTED?
        LinkedList<LocallyNetworkedDevice> currentlyExchangingDevices;

        ArrayList<Integer> networkErrorQueue;

        //each linked list is a collection of device tags
        Map<String, LinkedList<String>> allCurrentNetworkServices;

        Map<String, ArrayList<Object>> activeServices;
        Map<String, Integer> services;

        //a connected device will send it's record on each call
        //Record :  1. Device Tag , TAG
        //          2. Overall Revision , Revision
        //          3. Service 1 , Revision
        //          4. Service 2 , Revision
        //

        public LocallyNetworkedDevice updateConnectedDevice(String deviceTAG, Map record) {

            //an update comes when the connect device revision changes
            //must update overall revision and check each active service for notification

            if(record == null || deviceTAG == null) {
                networkErrorQueue.clear();
                networkErrorQueue.add(ERR_BAD_INPUT);
                return null;
            }

            else if(!locallyNetworkedDeviceLookupTable.contains(deviceTAG)) {
                //device is not in any list
                networkErrorQueue.clear();
                networkErrorQueue.add(ERR_DEVICE_NOT_NETWORKED);
                return null;
            }

            else{
                switch (locallyNetworkedDeviceLookupTable.get(deviceTAG).state){
                    //only the connected state can be service
                    //the tcp thread will be required to request connect otherwise

                    case "Connected":
                        break;

                    case "Disconnected":
                        networkErrorQueue.clear();
                        networkErrorQueue.add(ERR_CURRENT_STATE_DISCONNECTED);
                        return null;

                    case "Discovered":
                        networkErrorQueue.clear();
                        networkErrorQueue.add(ERR_CURRENT_STATE_DISCOVERED);
                        return null;

                    default:
                        networkErrorQueue.clear();
                        networkErrorQueue.add(ERR_STATE_NOT_RECOGNIZED);
                        return null;
                }

                String mDeviceTAG = null;
                LinkedList<Map.Entry> serviceReivisions = new LinkedList<>();
                int mRevision = 0;

                for (Object o : record.entrySet()) {
                    //must gather all information from the connected device's record
                    Map.Entry thisEntry = (Map.Entry) o;
                    String key = (String) thisEntry.getKey();
                    String value = (String) thisEntry.getValue();

                    if (key.equals("DEVICE_TAG")) {
                        if (!locallyNetworkedDeviceLookupTable.contains(value)) {
                            networkErrorQueue.clear();
                            networkErrorQueue.add(ERR_DEVICE_NOT_NETWORKED);
                            return null;
                        } else {
                            mDeviceTAG = value;
                        }
                    } else if (key.contains("OVERALL_REVISION")) {
                        mRevision = Integer.parseInt(value);
                    } else if (key.contains("SERVICE")) {
                        serviceReivisions.add(thisEntry);
                    } else {

                    }
                }
                if(mDeviceTAG == null || mRevision == 0 || serviceReivisions.isEmpty() ) {
                    //important information was left of of the record, report back to the device
                    networkErrorQueue.clear();
                    networkErrorQueue.add(ERR_RECORD_MISSING_INFORMATION);
                    return null;
                }

                else {
                    if(locallyNetworkedDeviceLookupTable.get(mDeviceTAG).overallRevision == mRevision) {
                        //no change in the revision so there is nothing to do
                        networkErrorQueue.clear();
                        networkErrorQueue.add(ERR_DEVICE_REVISION_UNCHANGED);
                        return null;
                    }
                    else {
                        //1 or more services is updated, check
                        Iterator<Map.Entry> serviceEntries = serviceReivisions.iterator();
                        Map.Entry<String,String> entry;
                        int newRevision;
                        while(serviceEntries.hasNext()) {
                            entry= (Map.Entry<String, String>) serviceEntries;
                            if(locallyNetworkedDeviceLookupTable.get(mDeviceTAG).services.containsKey(entry.getKey()) ){
                                //if the service is in the know list of services associated with this device, check the known revision
                                if(Integer.parseInt(locallyNetworkedDeviceLookupTable.get(mDeviceTAG).services.get(entry.getKey())) < Integer.parseInt(entry.getValue()) ){
                                    //if the revision has increased, then update and notify the service if it is running
                                    if(activeServices.containsKey(entry.getKey())) {
                                        locallyNetworkedDeviceLookupTable.get(mDeviceTAG).services.put(entry.getKey(), entry.getValue());
                                        activeServices.get(entry.getKey()).add("REVISION UPDATED TO: ".concat(entry.getValue()));
                                    }
                                    else{
                                        //no update to service as service is not active
                                        locallyNetworkedDeviceLookupTable.get(mDeviceTAG).services.put(entry.getKey(), entry.getValue());
                                    }
                                }
                                else{
                                    //service was never broadcast
                                }
                            }
                        }


                    }
                }

            }

            LocallyNetworkedDevice theDeviceInList;
            String currentState;

            if(device == null || updateValues == null){
                return null;
            }

            else {

                if( (theDeviceInList = locallyNetworkedDeviceLookupTable.get(device.TAG))==null ){

                    errorQueue.clear();
                    errorQueue.add(ERR_DEVICE_NOT_NETWORKED);
                    return null;
                }

                if( !(currentState = theDeviceInList.state).equals("Connected")) {
                    errorQueue.clear();

                    switch(currentState){
                        //THIS IS ASSUMING THAT THE FILES ARE ONLY KNOWN IF CONNECTED
                        //MAY BE PASSABLE THROUGH THE P2P LAYER THOUGH, AND MAY PERSIST THROUGH DISCO
                        case "Discovered":
                            errorQueue.add(ERR_CURRENT_STATE_DISCOVERED);
                        case "Disconnected":
                            errorQueue.add(ERR_CURRENT_STATE_DISCONNECTED);

                    }
                    return null;
                }
                else{
                    if(updateValues.size() == 0){
                        return theDeviceInList;
                    }

                    int i=0;
                    while(i < updateValues.size()){
                        if(updateValues.get(i).getClass().toString().equals("QueueUpdate")){
                            QueueUpdate queueUpdate = (QueueUpdate) updateValues.get(i);

                            //MODIFY THE INCOMING QUEUE LIST (ADD)
                            if(queueUpdate.incomingFilesToAdd!=null){
                                for(int j=0; j < queueUpdate.incomingFilesToAdd.size(); j++){
                                    if( theDeviceInList.deviceInfo.mFileList.contains(queueUpdate.incomingFilesToAdd.get(j))){

                                        if(!theDeviceInList.downloadState.incomingQueue.contains(queueUpdate.incomingFilesToAdd.get(j))){
                                            theDeviceInList.downloadState.incomingQueue.add(queueUpdate.incomingFilesToAdd.get(j));
                                        }
                                        else{
                                            //IS ALREADY IN THE INCOMING QUEUE
                                        }
                                    }
                                    else{
                                        //THIS FILE IS NOT INCLUDED IN THE DEVICE INFO OBJECT
                                    }
                                }
                            }

                            //MODIFY THE INCOMING QUEUE LIST (REMOVE)
                            if(queueUpdate.incomingFilesToRemove!=null) {

                                for (int k = 0; k < queueUpdate.incomingFilesToRemove.size(); k++) {
                                    if (theDeviceInList.downloadState.incomingQueue.contains(queueUpdate.incomingFilesToRemove.get(k))) {
                                        theDeviceInList.downloadState.incomingQueue.remove(queueUpdate.incomingFilesToRemove.get(k));
                                    } else {
                                        //THE FILE WAS NOT IN THE QUEUE
                                    }
                                }
                            }

                            //MODIFY THE OUTGOING QUEUE LIST (ADD)
                            if(queueUpdate.outgoingFilesToAdd!=null){
                                for(int j=0; j < queueUpdate.outgoingFilesToAdd.size(); j++){
                                    if( thisDevice.mFileList.contains(queueUpdate.outgoingFilesToAdd.get(j))){

                                        if(!theDeviceInList.downloadState.outgoingQueue.contains(queueUpdate.outgoingFilesToAdd.get(j))){
                                            theDeviceInList.downloadState.outgoingQueue.add(queueUpdate.outgoingFilesToAdd.get(j));
                                        }
                                        else{
                                            //IS ALREADY IN THE OUTGOING QUEUE
                                        }
                                    }
                                    else{
                                        //THIS FILE IS NOT INCLUDED IN THE DEVICE INFO OBJECT FOR THIS DEVICE
                                    }
                                }
                            }

                            //MODIFY THE OUTGOING QUEUE LIST (REMOVE)
                            if(queueUpdate.outgoingFilesToRemove!=null) {

                                for (int k = 0; k < queueUpdate.outgoingFilesToRemove.size(); k++) {
                                    if (theDeviceInList.downloadState.outgoingQueue.contains(queueUpdate.outgoingFilesToRemove.get(k))) {
                                        theDeviceInList.downloadState.outgoingQueue.remove(queueUpdate.outgoingFilesToRemove.get(k));
                                    } else {
                                        //THE FILE WAS NOT IN THE QUEUE
                                    }
                                }
                            }

                            // END OF FILE QUEUE MODS

                            return theDeviceInList;
                        }

                        if(updateValues.get(i).getClass().toString().equals("FileListUpdate")){

                        }

                        if(updateValues.get(i).getClass().toString().equals("OtherUpdates")){

                        }
                    }
                }
            }


            return null;
        }


    }


    public class ServiceRequestInterface {

        private ArrayList<Object> requestsFrom;

        private ArrayList<Object> dataFrom;

        private ArrayList<Object> errorsFrom;

        private String serviceTAG;

        public ServiceRequestInterface(String mServiceTAG){

            serviceTAG = new String(mServiceTAG);
            requestsFrom = new ArrayList<>();
            dataFrom = new ArrayList<>();
            errorsFrom = new ArrayList<>();

        }

        public Object parseInputCommands(String inputCommands) {

            return null;
        }

        public Object requestDiscoveredDevices(){

            return  null;
        }

        public Object requestConnectedDevices(){

            return null;
        }

        public Object requestDisconnectedDevices(){

            return null;
        }

        public Object requestAllNetworkedDevices(){

            return null;
        }

        public Object requestCurrentNetworkSchedule(){

            return null;
        }

        public Object connectTo(){

            return null;
        }

        public Object requestFrom(){

            return null;
        }

        public Object sentDataTo(){

            return null;
        }

        public Object sendErrorTo(){

            return null;
        }
    }


    public class ServiceRegistration {

        //the Object can be a string command, ex: 196A4B2 BUCKET CONNECT_TO JOE'S_DEVICE
        //when the service makes the request, it uses its given interface which assigns the ID and SERVICE TAG
        //a service must only present: CONNECT_TO JOE'S_DEVICE


        //Object contains: 1. Request Id
        //                 2. ServiceTAG
        //                 3. Request type
        //                 4. addition data(DeviceTAGs, data, schedule requests)

        LinkedList<Object> incommingMessageQueue;

        //when onService is successful, the interface with message queues is returned
        Hashtable<String, ServiceRequestInterface> onServices;

        //<serviceTAG, RequestInterface>
        //the automatic updates allow for services to register
        //will also be a method of registering services without any permissions
        //the service request interface is just the template used, not the allocated object
        Hashtable<String, ServiceRequestInterface> registeredServices;

        //each service must register with their serviceTAG
        //upon registration the service has a requestInterfaceObjectCreated and placed into the registeredServices table
        //this interface can be requested by the service
        public boolean registerService(String serviceTAG, ArrayList<Integer> errorQueue){

            ArrayList<Integer> tempErrorQueue = new ArrayList<>();
            Iterator<Integer> errorIterator;

            if(registeredServices.contains(serviceTAG)){
                errorQueue.clear();
                errorQueue.add(ERR_SERVICE_ALREADY_REGISTERED);
                return false;
            }
            else{
                ServiceRequestInterface newRequestInterface = requestNetworkServiceRegistration(serviceTAG, tempErrorQueue);
                if(newRequestInterface == null) {
                    if(!tempErrorQueue.isEmpty()) {
                        errorQueue.clear();
                        errorIterator = tempErrorQueue.iterator();
                        while(errorIterator.hasNext()) {
                            if(errorIterator.next().equals(ERR_NO_SERVICE_FOUND)) {
                                errorQueue.add(ERR_NO_SERVICE_FOUND);
                            }
                        }
                        return false;
                    }
                    else {
                        //error queue was not empty but returned null, bad input
                        errorQueue.clear();
                        errorQueue.add(ERR_BAD_INPUT);
                        return false;
                    }
                }
                //returned a non null interface definition
                else{
                    registeredServices.put(serviceTAG, newRequestInterface);
                    return true;
                }
            }
        }

        //each service must inform the scheduler if it is on or off
        public ServiceRequestInterface onService(String serviceTAG, ArrayList<Integer> errorQueue){
            if(!registeredServices.contains(serviceTAG)){
                errorQueue.clear();
                errorQueue.add(ERR_SERVICE_NOT_REGISTERED);
                return null;
            }
            else {
                if(onServices.contains(serviceTAG)){
                    errorQueue.clear();
                    errorQueue.add(ERR_SERVICE_ALREADY_ON);
                    return null;
                }

                else{
                    onServices.put(serviceTAG, registeredServices.get(serviceTAG));
                    return onServices.get(serviceTAG);
                }
            }
        }

        public ServiceRequestInterface requestRequestInterface(String serviceTAG, ArrayList<Integer> errorQueue) {
            if (!registeredServices.contains(serviceTAG)) {
                errorQueue.clear();
                errorQueue.add(ERR_SERVICE_NOT_REGISTERED);
                return null;
            } else {
                if (!onServices.contains(serviceTAG)) {
                    errorQueue.clear();
                    errorQueue.add(ERR_SERVICE_NOT_ON);
                    return null;
                } else {
                    return onServices.get(serviceTAG);
                }
            }
        }

        public boolean offService(String serviceTAG, ArrayList<Integer> errorQueue) {

            if(!registeredServices.contains(serviceTAG)) {
                errorQueue.clear();
                errorQueue.add(ERR_SERVICE_NOT_REGISTERED);
                return false;
            }
            else{
                if(!onServices.contains(serviceTAG)) {
                    errorQueue.clear();
                    errorQueue.add(ERR_SERVICE_NOT_ON);
                    return false;
                }
                else{
                    onServices.remove(serviceTAG);
                    return true;
                }
            }
        }

    }



    public class ServiceRequests {


        public Object requestConnect(String deviceTAG, String serviceTAG, ArrayList<Object> returnMessageQueue){
            if()
        }
    }



    //schedule initiallize

    //service registration
    //services are identified by their TAG, challenge-response (schedule encrypts challenge with public TAG, must get response)

    //service on/off (active/innactive)

    //on discover check all service revisions
    //call on connect if needed, notify service

    //have message queue from services
    //access when the service has schedule time

    //if the service is only discovered, must give it a block, keep a certain block open to process requests in p2p discover

    //if the service is connected, the request queue is processed on its next scheduled block

    //have response queue for the received data
    //a service can either get response messages, or entire files
    //the service must provide the response queue/repository

    public class P2pCalls {


        //the scheduler main thread runs separately to the tcp call threads
        //tcp requests can only be made by the theads requiring them
        //if a tcp thread is running, it has the lock for as long as it is scheduled

        ArrayList<Integer> errorQueue;

        public class DeiceInfoObject {

            //all device and account info can be located using the TAG
            String TAG;

            String currentConnectionState;

            boolean TCPThreadActive;
            ArrayList<String> tcpRequestQueue;

            WifiP2pDevice wifiP2pDevice;
            WifiP2pInfo wifiP2pInfo;

            Socket currentActiveSocket;

            Thread currentTCPThread;

            int lastConnectedRevision;
            int currentRevision;

            ArrayList<FileNodeObject> receivedFiles;
        }

        public DeviceInfoObject discoverP2p(Map record, WifiP2pInfo p2pDevice) {

            if(record == null) {
                errorQueue.clear();
                errorQueue.add(ERR_BAD_INPUT);
                return null;
            }

            DeviceInfoObject currentNetworkedDevice;
            DeviceInfoObject previousNetworkedDevice;

            currentNetworkedDevice = basicConfigurationAndData.currentlyNetworkedDevices.lookup(record.TAG);
            previousNetworkedDevice = basicConfigurationAndData.previouslyNetworkedDevices.lookup(record.TAG);

            if(currentNetworkedDevice == null) {


                if(previousNetworkedDevice == null) {
                    currentNetworkedDevice = new DeviceInfoObject(record);
                    //can add this device to the previously connected hash table
                    currentNetworkedDevice.p2pDevice = p2pDevice;
                    basicConfigurationAndData.currentNetworkedDevices.addDiscoveredDevice(currentNetworkedDevice);
                    currentNetworkedDevice.tcpRequestQueue.add("Request All");
                    connectP2p(currentNetworkedDevice);

                }
                else if(previousNetworkedDevice.previousRevision < record.revision) {
                    previousNetworkedDevice.currentRevision = record.revision;
                    basicConfigurationAndData.currentNetworkedDevices.addDiscoveredDevice(previousNetworkedDevice);
                    previousNetworkedDevice.tcpRequestQueue.add("Request Since Last Connect");
                    connectP2p(currentNetworkedDevice);
                }
            }

            else if (currentNetworkedDevice.state.equals("Disconnected") ) {
                if(previousNetworkedDevice == null) {
                    if(currentNetworkedDevice.currentRevision < record.revision) {
                        currentNetworkeDevice.currentRevision = record.revision;
                        basicConfigurationAndData.currentNetworkedDevices.addDiscoveredDevice(currentNetworkedDevice);
                        currentNetworkedDevice.tcpRequestQueue.add("Request Since Last Connected");
                        connectP2p(currentNetworkedDevice);
                    }
                    else{
                        //nothing to request
                        basicConfigurationAndData.currentNetworkedDevices.addDiscoveredDevice(currentNetworkedDevice);
                    }
                }
                else if (previousNetworkedDevice.previousRevision < record.revision) {
                    basicConfigurationAndData.moveDisconnectedDevice(currentNetworkedDevice, "Discovered");
                    currentNetworkedDevice.tcpRequestQueue.add("Request Since Last Connected");
                    connectP2p(currentNetworkedDevice);
                }
                else {
                    basicConfigurationAndData.moveDisconnectedDevice(currentNetworkedDevice, "Discovered");
                    if(currentNetworkedDevice.tcpRequestQueue.size > 0) {
                        connectP2p(currentNetworkedDevice);
                    }
                }
            }
            else if (currentNetworkedDevice.state.equals("Discovered")) {
                if(currentNetworkedDevice.currentRevision < record.revision) {
                    currentNetworkedDevice.currentRevision = record.revision;
                    currentNetworkedDevice.tcpRequestQueue.add("Request Since Last Connected");
                    connectP2p(currentNetworkedDevice);
                }
                else {
                    //no update
                }
            }
            else if (currentNetworkedDevice.state.equals("Connected") ){
                if(currentNetworkedDevice.currentRevision < record.revision) {
                    currentNetworkedDevice.currentRevision = record.revision);
                    currentNetworkedDevice.tcpRequestQueue.add("Revision Update Request");
                }
                else{
                    //no update
                }
            }
        }

        public DeviceInfoObject connectP2p(DeviceInfoObject device) {
            //when connect is called must call connect over a certain block of time repeatedly

            InetAddress groupOwnerAddress = info.groupOwnerAddress;
            if (device.wifiP2pInfo.groupFormed && device.wifiP2pInfo.isGroupOwner) {
            }
            else if (device.wifiP2pInfo.groupFormed) {
            }

        }



        public DeviceInfoObject P2pDiscovered (Map record) {

            DeviceInfoObject localDevice;

            DeviceInfoObject previousDevice;

            localDevice = basicConfigurationAndData.localNetworkInfo.lookupTag(record.TAG);

            //actually should check the database on device i f the ddevice in question has connected


            //check if the device is connected
            //if it is , then check revision, if the current revision is less then the one discovered/connected,
            //must re discover, and change revision then check against the previously connected info,

            //NULL means not yet locall ynetworked
            if(localDevice == null) {

                //create the new loca object
                localDevice = new DeviceInfoObject(record);

                //add to discovered list
                basicConfigurationAndData.addDiscoveredDevice(localDevice);

                //check if previously connected
                previousDevice = previousNetworkInfo(record.TAG);

                //if null has never connected
                if(previousDevice == null){

                    requestConnect(localDevice);
                }
                else {
                    if(previousDevice.revision < localDevice.revision) {
                        //this will send the message to the device TCP thread
                        //once accept, the device object changes the queue
                        requestConnect(localDevice);
                    }
                    else{
                        //no need to connect
                    }
                }
            }

            else if(localDevice.state.equals("Disconnected") {
                localDevice.update(record);
                basicConfigurationAndData.moveDisconnectedDevice("Discovered", true, localDevice);
                return localDevice;
            }


        }

        public DeviceInfoObject P2pConnect (DeviceInfoObject) {

        }
    }

    public class ScheduleMessageObject {

        //String can be "request_blocks"
        public String requestType;

        //if the algorithm cannot return enough blocks
        int minimumBlocks;

        //each bit of each byte is associated with the priority of the requested time
        //ex: 0001011 refers to this time block have priority association of group 1, 2, and 4
        byte[] scheduleRequest;
    }

    static int MAX_TIME_SLOTS = 1000;
    static int TOTAL_SCHEDULE_PERIOD_MILIS = 1000;

    public class RunningScheduleAlgorithm extends Runnable {


        public RunningScheduleAlgorithm(Handler osMessageHandler){

            scheduleThreadHandler = new Handler() {

                //each message received needs a return addr
                //this will send the message to the running tcp thread
                public void HandleMessage(Message msg){
                    if(msg.obj.toString().equals("ScheduleMessageObject")){
                        if(msg.obj.requestType.equals("block_request"){
                            //THE REQUEST FUNCTION MUST REQUIRE A LOCK
                            //mask each of the bits and check the largest schedule block available
                            byte[][] proposedSchedule = new byte[8][MAX_TIME_SLOTS];
                            int maskBits;
                            int longestChainLength = 0;
                            int currentChainLength = 0;
                            int currentChainStart = 0;

                            int[8] longestChainStartIndex;


                            //8 to indicate which priority array it was from
                            //the 20 is to indicate the start index

                            int[][] longestChainStarts = new byte[8][20];
                            int currentStartIndex = 0;

                            for(int i = 0; i < 8; i++) {
                                maskBits = 2^(i);

                                for(int j = 0; j < MAX_TIME_SLOTS; j++){
                                    if( (msg.obj.scheduleRequest[j] & maskBits) !=0){
                                        longestChainStarts[i][currentStartIndex] = j;
                                        currentStartIndex++;
                                        currentChainLength++;

                                        proposedSchedule[i][j] = 1;
                                        j++;
                                        while( (msg.obj.scheduleRequest[j] & maskBits) !=0){
                                            proposedSchedule[i][j] =1;
                                            currentChainLength++;
                                            j++;
                                        }
                                        if(longestChainLength < currentChainLength){
                                            longestChainLegnth = currentChainLength;
                                            longestChainStartIndex[i] = longestChainStarts[i][currentStartIndex-1];
                                        }
                                    }
                                    else{
                                        currentStartIndex++;
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }

        Handler scheduleThreadHandler;

        public Handler getHandler(){
            return this.scheduleThreadHandler;
        }

        scheduleThreadHandler.


        public byte[] modifyScheduleRequest(byte[] scheduleMod){


        }

        public run(){

        }
    }

    private byte[] currentSchedule = new Byte[MAX_TIME_SLOTS];


    //function to make request
    //must decide if schedule is to be retreived as another thread
    //or if request is just from memory w/o lock
    byte[] requestCurrentSchedule(){
        return currentSchedule;
    }

    byte[] modifyScheduleRequest(){


    }


    //functio
}
