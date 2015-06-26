package com.bucket.thebucket;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by marcel on 31/05/15.
 */
public class LocallyNetworkedDeviceInfo {

    //can also be considered as a session revision
    public int overallRevision;

    //each different service has a DeviceInfoObject
    public ArrayList<DeviceInfoObject> associatedDeviceObjects;
    public ArrayList<Map.Entry<String,Integer>> associatedServices;

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
