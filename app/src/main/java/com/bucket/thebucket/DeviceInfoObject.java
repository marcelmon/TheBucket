package com.bucket.thebucket;

import java.util.ArrayList;

/**
 * Created by marcel on 31/05/15.
 */
public class DeviceInfoObject {

    String TAG;
    public String mDeviceName;
    public int mRevision;
    public ArrayList<FileNodeObject> mFileList;
    public String mDeviceType;
    public int mNumberOfDownloadables;

    public DeviceInfoObject(String name, int revision, String type) {
        mDeviceName = new String(name);
        mRevision = revision;
        mDeviceType = type;
        mFileList = new ArrayList<FileNodeObject>();
    }

    public DeviceInfoObject() {

    }

    public void addFile(FileNodeObject file) {
        mFileList.add(file);
    }


};

