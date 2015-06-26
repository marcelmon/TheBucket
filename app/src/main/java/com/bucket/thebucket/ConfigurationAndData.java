package com.bucket.thebucket;

import com.bucket.thebucket.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class ConfigurationAndData {



    public class AccountData implements Data{

        public Object customFunction(Object object){
            return null;
        }

        public AccountData(){

            thisAccount = new ProfileInfo();
            readPermissions = new HashMap<>();
            writePermissions = new HashMap<>();
        }


        public class ProfileInfo implements Cloneable{

            String hashedPassword;
            String accountName;
            String name;

            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }


        ProfileInfo thisAccount;


        //in form <H(userToken, serviceToken), permissions>
        HashMap<String, String> readPermissions;
        HashMap<String, String> writePermissions;


        public Object serviceReadData(String[] dataIdentifier, String serviceToken) throws CloneNotSupportedException {

            //must be implemented as String[]??
            LinkedList<String> readValues = new LinkedList<>();


            if(dataIdentifier==null || serviceToken ==null){
                return null;
            }
            else if(dataIdentifier.length == 0){
                return null;
            }

            //build a LinkedList of the Strings to return or return an entire ProfileInfo object
            else for(int i=0; i<dataIdentifier.length; i++){

                    switch(dataIdentifier[i]){

                        case "ENTIREPROFILE":
                            switch(serviceToken){
                                case "root":
                                    return thisAccount.clone();

                                default: return null;
                            }

                        case "accountName":

                            switch(serviceToken){
                                case "root":

                                    readValues.add(thisAccount.accountName);

                                default: return null;
                            }
                        case "name":
                            switch(serviceToken){
                                case "root":

                                    readValues.add(thisAccount.name);

                                default: return null;
                            }

                        default: return null;
                    }

            }

            if(readValues.size()!=0){
                return readValues;
            }
            else return null;
        }


        public Object serviceWriteData(String[] dataIdentifier, String[] dataValues, String serviceToken){

            //return which data members were modified successfully, null for none

            LinkedList<String> successfulWrites = new LinkedList<>();

            if(dataIdentifier==null || dataValues == null || serviceToken ==null){
                return null;
            }
            else if(dataIdentifier.length == 0 || dataValues.length != dataIdentifier.length){
                return null;
            }

            else for(int i=0; i<dataIdentifier.length; i++){

                    switch(dataIdentifier[i]){

                        case "accountName":

                            switch(serviceToken){
                                case "root":

                                    successfulWrites
                                    return true;

                                default: return null;
                            }

                        default: return null;
                    }

                }

            return null;
        }

        public Object bothReadData(String[] dataIdentifier, String serviceToken, String UserToken){
            return null;
        }
        public Object bothWriteData(String[] dataIdentifier, String serviceToken, String userToken){
            return null;

        }
        public Object userReadData(String[] dataIdentifier, String serviceToken, String UserToken){
            return null;
        }
        public Object userWriteData(String[] dataIdentifier, String serviceToken, String userToken){
            return null;
        }



    }

    public class CurrentConnectedDevices implements Data{

        public Object customFunction(Object object){
            return null;
        }
        
        LinkedList<DeviceInfoObject> connectedDevices;

        public CurrentConnectedDevices(){

            connectedDevices = new LinkedList<>();
            readPermissions = new HashMap<>();
            writePermissions = new HashMap<>();

        }

        //in form <H(userToken, serviceToken), permissions>
        HashMap<String, String> readPermissions;
        HashMap<String, String> writePermissions;




        public Object serviceReadData(String[] dataIdentifier, String serviceToken, String userToken){

            if(dataIdentifier==null || serviceToken ==null || userToken == null){
                return null;
            }
            else if(dataIdentifier.length == 0){
                return null;
            }

            else for(int i=0; i<dataIdentifier.length; i++){

                    switch(dataIdentifier[i]){

                        case 1: dataIdentifier[i].equals("ALLDEVICES");
                            switch(serviceToken){
                                case 1: serviceToken.equals("root");
                                    return static connectedDevices;

                                default: return null;
                            }

                        default: return null;


                    }
                }
        }


        public Object serviceWriteData(String[] dataIdentifier, String serviceToken, String userToken){

            if(dataIdentifier==null || serviceToken ==null || userToken == null){
                return null;
            }
            else if(dataIdentifier.length == 0){
                return null;
            }

            else for(int i=0; i<dataIdentifier.length; i++){

                    switch(dataIdentifier[i]){

                        case 1: dataIdentifier[i].equals("ENTIREPROFILE");
                            switch(serviceToken){
                                case 1: serviceToken.equals("root");

                                    //do the writing here
                                    //call NOTIFIY_DATA_SET_CHANGED()
                                    return true;

                                default: return null;
                            }
                        default: return null;
                    }

                }

            return null;
        }

        public Object bothReadData(String[] dataIdentifier, String serviceToken, String UserToken){
            return null;
        }
        public Object bothWriteData(String[] dataIdentifier, String serviceToken, String userToken){
            return null;

        }
        public Object userReadData(String[] dataIdentifier, String serviceToken, String UserToken){
            return null;
        }
        public Object userWriteData(String[] dataIdentifier, String serviceToken, String userToken){
            return null;
        }
    }
}


