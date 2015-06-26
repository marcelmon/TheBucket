package com.bucket.thebucket;

import java.io.File;
import java.util.Map;

/**
 * Created by marcel on 31/05/15.
 */
public class FileNodeObject {
    int totalSymbolicLinks;

    String fileTag;
    String fileName;
    int size;
    String FileType;
    String orginialOwner;
    String dateCreated;
    String dateTransfered;
    Map<String, String> attributes;
    File fd;

    public FileNodeObject(String name, int Size, File mFd){
        fileName = name;
        size = Size;
        fd = mFd;
    }
    public FileNodeObject(){

    }

    public FileNodeObject(ServerTransferHandler.FileInfoNode mFileInfoNode, String path){
        fileName = mFileInfoNode.fileName;
        size = mFileInfoNode.size;
        FileType = mFileInfoNode.FileType;
        orginialOwner = mFileInfoNode.orginialOwner;
        dateCreated = mFileInfoNode.dateCreated;
        dateTransfered = mFileInfoNode.dateTransfered;
        attributes = mFileInfoNode.attributes;
        fd = new File(path);
    }
};

