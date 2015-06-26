package com.bucket.thebucket;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by marcel on 29/04/15.
 */


public class basicConfigurationAndData {

    final int ERR_CURRENT_STATE_DISCOVERED = 1;
    final int ERR_CURRENT_STATE_CONNECTED= 2;
    final int ERR_CURRENT_STATE_DISCONNECTED = 3;

    final int ERR_DEVICE_NOT_NETWORKED = 4;
    final int ERR_DEVICE_ALREADY_NETWORKED = 5;

    final int MAX_NETWORK_SIZE_REACHED = 6;

    final int ERR_STATE_NOT_RECOGNIZED = 7;

    final int MAX_LOCALLY_NETWORKED_SIZE = 50;

    final int ERR_DEVICE_NEVER_CONNECTED = 8;

    final int ERR_DEVICE_REVISION_UNCHANGED = 9;

    public class AccountInformation{

        //TO INCLUDE PROFILE INFO
        ProfileInfo thisProfile;
    }

    DeviceInfoObject thisDevice;

    public class ThisDeviceInfo{
        //TO INCLUDE THE DEVICE INFO OBJECT FOR THIS DEVICE

        DeviceInfoObject thisDevice;
    }

    public class ThisDeviceSettings{
        //INCLUDE FILTERS, SERVICE TYPES, LAYOUT INFO
    }

    public class SavedNetworkInfo{
        //INCLUDES LIST OF PREVIOUSLY CONNECTED DEVICES, PREVIOUSLY RECEIVED FILES
        //CAN BE RETRIEVED FROM SIGN IN
        //INCLUDES LIST OF PREVIOUSLY CONNECTED DEVICES, PREVIOUSLY RECEIVED FILES
        //CAN BE RETRIEVED FROM SIGN IN

        //hash table groups contain the device info
        //save important device info objec revision

        //HashTable of 10000 elements *(32 bit revision +(64 bit DeviceTAG) //the device tag is random unit assigned
        //point to list of point3ers
        //pointers incmlude:
        //references to device current or active
        //->(could be current local network, is network lead

        //<Tag, Info>
        //Assign Tags as sequential Random Values for registered devices
        Hashtable<String, SavedDeviceInfo> savedDeviceInfoTable;

        ArrayList<Integer> errorQueue;

        public SavedNetworkInfo() {
            savedDeviceInfoTable = new Hashtable<>();
            errorQueue = new ArrayList<>();
        }

        public class SavedDeviceInfo {

            boolean isOnDisk;
            public DeviceInfoObject deviceInfo;

            int revision;

            //tables are used to store the info
            //can read a large block from disk that is likely to be grouped
            int tableLocation;
        }

        public SavedDeviceInfo lookupDevice(String TAG) {

            if(TAG == null) return null;

            if(savedDeviceInfoTable.contains(TAG)) {
                return savedDeviceInfoTable.get(TAG);
            }

            else {
                errorQueue.clear();
                errorQueue.add(ERR_DEVICE_NEVER_CONNECTED);
                return null;
            }
        }

        public SavedDeviceInfo updateDevice(DeviceInfoObject update) {
            if(!savedDeviceInfoTable.contains(update.TAG)) {
                if(savedDeviceInfoTable.get(update.TAG).revision < update.mRevision) {
                    savedDeviceInfoTable.get(update.TAG).deviceInfo = update;
                    savedDeviceInfoTable.get(update.TAG).revision = update.mRevision;
                    return savedDeviceInfoTable.get(update.TAG);
                }
                else {
                    errorQueue.clear();
                    errorQueue.add(ERR_DEVICE_REVISION_UNCHANGED);
                    return null;
                }
            }
            errorQueue.clear();
            errorQueue.add(ERR_DEVICE_NEVER_CONNECTED);
            return null;
        }
    }

    public class CurrentNetworkInfo{


        ArrayList<Integer> errorQueue;

        public CurrentNetworkInfo(){

            errorQueue = new ArrayList<>();

            locallyNetworkedDeviceLookupTable = new Hashtable<>();

            allLocallyNetworkedDevices = new LinkedList<>();
            currentlyDiscoveredDevices = new LinkedList<>();
            currentlyConnectedDevices = new LinkedList<>();
            currentlyDisconnectedDevices = new LinkedList<>();
            currentlyExchangingDevices = new LinkedList<>();
        }

        public DeviceInfoObject lookupTAG(String TAG){

            //is actually the container for DeviceInfo Object, change Later, duh
            //return the object containing the particular TAG, can also
            Iterator<LocallyNetworkedDevice> deviceIterator = allLocallyNetworkedDevices.iterator();

            DeviceInfoObject current;
            while(deviceIterator.hasNext()){
                current = deviceIterator.next();
                if(current.TAG.equals(TAG)) {
                    return current;
                }
            }
            return null;
        }
        //TO INCLUDE DEVICES THAT ARE DISCOVERED AND CONNECTED


        //TO POINT TO THE OBJECT IN THE LINKED LISTS, DOES NOT GIVE LOCATION IN LINKED LIST
        Hashtable<String, LocallyNetworkedDevice> locallyNetworkedDeviceLookupTable;


        //CONSIDER SELF BALANCING TREES FOR O(lg(n))
        //ALL DEVICE, POINTS TO THE OTHER LINKED LISTS
        LinkedList<LocallyNetworkedDevice> allLocallyNetworkedDevices;


        //called by the P2P layer
        LocallyNetworkedDevice addNewDiscoveredDevice(DeviceInfoObject newDevice){
            //check for improper call
            if(newDevice == null){
                errorQueue.clear();
                return null;
            }

            //already a discovered device
            else if(locallyNetworkedDeviceLookupTable.contains(newDevice.TAG)){

                errorQueue.clear();
                errorQueue.add(ERR_DEVICE_ALREADY_NETWORKED);
                return null;

            }

            //not already discovered, add to list, set state discovered
            else if (allLocallyNetworkedDevices.size() < MAX_LOCALLY_NETWORKED_SIZE) {
                LocallyNetworkedDevice device = new LocallyNetworkedDevice(newDevice);
                device.state = "Discovered";

                allLocallyNetworkedDevices.add(device);
                currentlyDiscoveredDevices.add(device);

                return device;
            }

            else {
                errorQueue.clear();
                errorQueue.add(MAX_NETWORK_SIZE_REACHED);
                return null;
            }
        }

//MOVE THE DEVICE FROM DISCOVERED TO EITHER CONNECTED OR DISCONNECTED
        //is Modified may be useless as he DeviceInfoObject is not Immutable

        LocallyNetworkedDevice moveDiscoveredDevice(String newLocation, Boolean isModified, DeviceInfoObject device) {

            LocallyNetworkedDevice theDeviceInList;
            String currentState;

            if (newLocation == null || isModified == null || device == null){
                errorQueue.clear();
                return null;
            }

            //not discovered
            else if( (theDeviceInList = locallyNetworkedDeviceLookupTable.get(device.TAG))==null ){

                errorQueue.clear();
                errorQueue.add(ERR_DEVICE_NOT_NETWORKED);
                return null;
            }

            //device in list but not discovered
            else if( !(currentState = theDeviceInList.state ).equals("Discovered")) {

                errorQueue.clear();
                switch (currentState){
                    case "Connected":
                        errorQueue.add(ERR_CURRENT_STATE_CONNECTED);
                    case "Disconnected":
                        errorQueue.add(ERR_CURRENT_STATE_DISCONNECTED);
                }
                return null;
            }

            else if(newLocation.equals("ConnectedDevices")){

                currentlyDiscoveredDevices.remove(theDeviceInList);
                currentlyConnectedDevices.add(theDeviceInList);

                theDeviceInList.state = "Connected";
                currentlyConnectedDevices.add(theDeviceInList);
                if(isModified == true){

                    theDeviceInList.deviceInfo = device;
                }

                return theDeviceInList;
            }

            else if(newLocation.equals("DisconnectedDevices")) {

                currentlyDiscoveredDevices.remove(theDeviceInList);
                currentlyDisconnectedDevices.add(theDeviceInList);

                theDeviceInList.state = "Disconnected";

                if(isModified == true){
                    theDeviceInList.deviceInfo = device;
                }
                return theDeviceInList;
            }

            else {
                errorQueue.clear();
                errorQueue.add(ERR_STATE_NOT_RECOGNIZED);
                return null;
            }
        }

//MOVE THE DEVICE FROM CONNECTED TO EITHER DISCOVERED OR DISCONNECTED
        LocallyNetworkedDevice moveConnectedDevice(String newLocation, Boolean isModified, DeviceInfoObject device) {

            LocallyNetworkedDevice theDeviceInList;
            String currentState;

            if (newLocation == null || isModified == null || device == null){
                errorQueue.clear();
                return null;
            }

            else if( (theDeviceInList = locallyNetworkedDeviceLookupTable.get(device.TAG))==null ){

                errorQueue.clear();
                errorQueue.add(ERR_DEVICE_NOT_NETWORKED);
                return null;
            }


            else if( !(currentState = theDeviceInList.state ).equals("Connected")) {

                errorQueue.clear();
                switch (currentState){
                    case "Discovered":
                        errorQueue.add(ERR_CURRENT_STATE_DISCOVERED);
                    case "Disconnected":
                        errorQueue.add(ERR_CURRENT_STATE_DISCONNECTED);
                }
                return null;
            }

            else if(newLocation.equals("DiscoveredDevices")){

                currentlyConnectedDevices.remove(theDeviceInList);
                currentlyDiscoveredDevices.add(theDeviceInList);

                theDeviceInList.state = "Discovered";
                currentlyDiscoveredDevices.add(theDeviceInList);
                if(isModified == true){

                    theDeviceInList.deviceInfo = device;
                }

                return theDeviceInList;
            }

            else if(newLocation.equals("DisconnectedDevices")) {

                currentlyConnectedDevices.remove(theDeviceInList);
                currentlyDisconnectedDevices.add(theDeviceInList);

                theDeviceInList.state = "Disconnected";

                if(isModified == true){
                    theDeviceInList.deviceInfo = device;
                }
                return theDeviceInList;
            }

            else {
                errorQueue.clear();
                errorQueue.add(ERR_STATE_NOT_RECOGNIZED);
                return null;
            }
        }

        public class QueueUpdate{

            ArrayList<FileNodeObject> incomingFilesToAdd;
            ArrayList<FileNodeObject> incomingFilesToRemove;

            ArrayList<FileNodeObject> outgoingFilesToAdd;
            ArrayList<FileNodeObject> outgoingFilesToRemove;

        }

        public class FileListUpdate{

        }

        public class OtherUpdates{

        }

        LocallyNetworkedDevice updateConnectedDevice(Boolean isModified, DeviceInfoObject device, ArrayList<Object> updateValues) {

            //UPDATES TO INCLUDE
                //FILE LIST UPDATE
                //OTHER DEVICE INFO UPDATEs
                //INCOMMING AND OUTGOING QUEUE UPDATES

            LocallyNetworkedDevice theDeviceInList;
            String currentState;

            if(isModified == null || device == null || updateValues == null){
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

            //If a device moves between one of these 4 categories it must be made known to the view adapter

        //discovered are P2P only, has not gone through the connection and establishment of all info
        LinkedList<LocallyNetworkedDevice> currentlyDiscoveredDevices;

        //All devices currently passed the P2P stages
        LinkedList<LocallyNetworkedDevice> currentlyConnectedDevices;

        //keep in this list for X time then remove entirely
        LinkedList<LocallyNetworkedDevice> currentlyDisconnectedDevices;

        //Includes list of devices currently involved with exchanges
        //THIS IS LESS IMPORTANT, CAN BE MERGED WITH CONNECTED?
                LinkedList<LocallyNetworkedDevice> currentlyExchangingDevices;

    }

    public class ThisDeviceFiles{

        public class FileViewAdapter {

            Boolean isViewingFile;


            Directory currentDirectory;

            FileNodeObject currentFile;

            public int changeDirectory(Directory newDirectory){

                //SET CURRENT DIRECTORY TO NEW DIRECTORY
                //isViewingFile = false;
                //VIEW SHOULD BE CHANGED TO DISPLAY ITS INFO
                return 0;
            }

            public int selectFile(FileNodeObject openFile){
                //set current file to open file
                //isViewingFile = true;
                //change view to reflect this
                return 0;
            }

            public int closeFile(){
                if(isViewingFile == false){
                    return 0;
                }
                //ELSE CLOSE FILE SET TO TRUE
                return 0;
            }
        }

        public ThisDeviceFiles(){
            fileList = new Hashtable<>();
            directoryList = new Hashtable<>();
            receivedFiles = new ReceivedFiles();
            thisDeviceBucketFiles = new ThisDeviceBucketFiles();
        }

        //this hash table points to the parent directory of each file
        Hashtable<String, Directory> fileList;

        //this hash table points to the parent directory of each directory
        Hashtable<String, Directory> directoryList;

        //INCLUDE THE DIRECTORIES OF PREVIOUSLY RECEIVED DOWNLOADABLES
            //ALSO OWN FILES IN BUCKET, W/ POINTERS FROM THIS DEVICE INFO

        ReceivedFiles receivedFiles;

        ThisDeviceBucketFiles thisDeviceBucketFiles;

        public class ThisDeviceBucketFiles{

            public ThisDeviceBucketFiles(){
                currentSetDownloadables = new ArrayList<>();
                previousSetDownloadables = new ArrayList<>();
            }

            ArrayList<BucketFiles> currentSetDownloadables;

            ArrayList<BucketFiles> previousSetDownloadables;

            public class BucketFiles{
                FileNodeObject fileNode;
                Object otherInfo;
            }

            Map<String, String> downloadablesRecord;



            public String createDownloadablesRecord(){
     //USE RECORD PARSER AS BEFORE
                return null;
            }
        }

        public class ReceivedFiles{

            //should be sorted in order will be removed
            LinkedList<FileNodeObject> pendingFiles;

            //files are removed from this list when complete
            LinkedList<FileNodeObject> currentTransfers;

            Directory rootReceivedDirectory;

            public void addPendingFile(){
                /////////////////////
            }

            public void pendingToTransfer(){
                /////////////////////////////
            }

            public void transferToComplete(){
                ///////////////////////////
                //MUST USE DEVICE SETTINGS TO DETERMINE PROPER DIRECTORY
            }

            public void pendingToDisco(){

            }

            public void transferToDisco(){

            }

            public void removeDisco(){

            }

            public void discoToPending(){

            }

            public void discoToTransfer() {
                //SHOULD PROBABLY JUST PUT IN TRANSFER
            }
            public ReceivedFiles(){
                pendingFiles = new LinkedList<>();
                currentTransfers = new LinkedList<>();

                rootReceivedDirectory = new Directory(null, "rootReceivedDirectory");
            }
        }
    }

    public class Directory {

        int totalSymbolicLinks;

        String directoryName;

        Directory parentDirectory;

        int size;

        //MAY WANT TO MAKE LINKED LIST OR BINARY SEARCH FOR QUICK CHECK OF FILE NAMES
        ArrayList<Directory> childDirectories;
        ArrayList<FileNodeObject> childFiles;

        //add new child directory
        public Directory addDirectory(String name) {

            Iterator<Directory> childDirectoryIterator = this.childDirectories.iterator();
            while(childDirectoryIterator.hasNext()) {
                if(childDirectoryIterator.next().directoryName.equals(name)) {
                    return null;
                }
            }
            Directory newDirectory = new Directory(this, name);
            childDirectories.add(newDirectory);
            return newDirectory;
        }

        //add a new child file info node
        public FileNodeObject addFile(FileNodeObject file) {
            Iterator<FileNodeObject> childFileIterator = this.childFiles.iterator();
            while(childFileIterator.hasNext()) {
                if(childFileIterator.next().fileTag.equals(file.fileTag)) {
                    return null;
                }
            }
            this.childFiles.add(file);
            file.totalSymbolicLinks ++;
            return file;
        }

        public FileNodeObject copyFile(){
            return null;

        }

        public Directory moveFile() {
            return null;
        }

        public int deleteFile(){
            return 0;
        }

        public int deleteDirectory(Boolean deleteAllChildren){

            if(deleteAllChildren == true) {
                //only delete symbolic links unless there is only 1, then delete whole file
                Iterator<Directory> directoryIterator = this.childDirectories.iterator();

                while(directoryIterator.hasNext()){
                    directoryIterator.next().deleteDirectory(deleteAllChildren);

                }

                Iterator<FileNodeObject> fileIterator = this.childFiles.iterator();
                FileNodeObject fileToDelete;
                while(fileIterator.hasNext()){
                    fileToDelete = fileIterator.next();

                    if( (fileToDelete.totalSymbolicLinks--) == 0) {

       //MAKE SURE TO REMOVE THIS FILE FROM THE DISK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        fileToDelete = null;
                    }

                }
                return 0;
            }

            else if(deleteAllChildren == false){

        //STILL NEED TO MAKE ADJUSTMENTS TO THE HASH TABLE

                Iterator<FileNodeObject> parentFileIterator = this.parentDirectory.childFiles.iterator();
                Iterator<Directory> parentDirectoryIterator = this.parentDirectory.childDirectories.iterator();

                Iterator<FileNodeObject> fileIterator = this.childFiles.iterator();
                Iterator<Directory> directoryIterator = this.childDirectories.iterator();

                Directory directoryToRename, directoryToCompare;

                FileNodeObject fileToRename, fileToCompare;

                while(directoryIterator.hasNext()){
                    directoryToRename = directoryIterator.next();

                    while(parentDirectoryIterator.hasNext()) {
                        if(directoryToRename.directoryName.equals((directoryToCompare = parentDirectoryIterator.next()).directoryName)){
                            directoryToRename.directoryName.concat("(1)");
                        }
                    }
                    parentDirectory.childDirectories.add(directoryToRename);
                    parentDirectoryIterator = this.parentDirectory.childDirectories.iterator();
                }

                while(directoryIterator.hasNext()){
                    fileToRename = fileIterator.next();

                    while(parentDirectoryIterator.hasNext()) {
                        if(fileToRename.fileName.equals((fileToCompare = parentFileIterator.next()).fileName)){
                            fileToRename.fileName.concat("(1)");
                        }
                    }
                    parentDirectory.childFiles.add(fileToRename);
                    parentFileIterator = this.parentDirectory.childFiles.iterator();
                }
                return 0;
            }
            return 0;
        }

        public int copyDirectory(){
            return 0;
        }

        public int moveDirectory(){
            return 0;
        }


        public Directory(Directory parent, String name) {

            totalSymbolicLinks =1;
            directoryName = new String(name);
            parentDirectory = parent;
            childDirectories = new ArrayList<>();
            childFiles = new ArrayList<>();
        }
    }


    public class ProfileInfo{


        String accountName;
        String name;
        String location;

    }

    public class LocallyNetworkedDevice {

        public LocallyNetworkedDevice(DeviceInfoObject newDevice){

            deviceInfo = newDevice;

        }
        private DeviceInfoObject deviceInfo;

        //include discovered, connected, disconnected
        private String state;

        //says if the device in question current has outgoing or incoming requests
        //and also the queues
        private DeviceDownloadState downloadState;

        ArrayList<FileNodeObject> recommendedIncomingFiles;
        ArrayList<FileNodeObject> recommendedOutgoingFiles;

        //this will be the network schedule for this device
        Object schedule;

        class DeviceDownloadState{
            String incommingState;

            String outgoingState;

            ArrayList<FileNodeObject> incomingQueue;

            ArrayList<FileNodeObject> outgoingQueue;

        }


    }

}


