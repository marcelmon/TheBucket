package com.bucket.thebucket;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by marcel on 31/05/15.
 */
public class TheBucket {

    static int ERR_WRONG_USER_OR_PASS = 10;
    static int ERR_ACCOUNT_NOT_LOADED = 11;
    static int ERR_ACCOUNT_SIGNED_IN = 12;

    public TheBucket() {

    }

    boolean accountSignedIn;
    //can decide to keep a list of accounts that can be signed into
    //list removes accounts not accessed after X days or Y hours
    public AccountAndDeviceInfo currentlyLoadedAccount



    ArrayList<Integer> errorQueue;

    public AccountAndDeviceInfo updateNetworkAccountInfo(AccountAndDeviceInfo account, ArrayList<Integer> errorQueue){

    }

    public AccountAndDeviceInfo networkSignInRequest(String username, String password, ArrayList<Integer> errorQueue){

    }

    public AccountAndDeviceInfo logIntoCurrentlyLoadedAccount(String username, String password, ArrayList<Integer> errorQueue) {

    }

    //login will have the loaded account
    public AccountAndDeviceInfo signInToAccount(String username, String password){

        private AccountAndDeviceInfo tempAccount;

        private Iterator<Integer> errorIterator

        ArrayList<Integer> tempErrorQueue;

        //already an account signed in
        if(accountSignedIn) {
            errorQueue.clear();
            errorQueue.add(ERR_ACCOUNT_SIGNED_IN);
            return null;
        }
        else {
            //there is no loaded account, use network to sign in
            if(currentlyLoadedAccount == null) {
                tempAccount = networkSignInRequest(username, password, tempErrorQueue);
                //network returned null
                if(tempAccount == null) {
                    //check for errors
                    if(tempErrorQueue.size > 0) {
                        errorIterator = tempErrorQueue.iterator();
                        while (errorIterator.hasNext()) {
                            if(errorIterator.next().equals(ERR_WRONG_USER_OR_PASS)){
                                errorQueue.clear();
                                errorQueue.add(ERR_WRONG_USER_OR_PASS);
                                accountSignedIn = false;
                            }
                        }
                        return null;
                    }
                    //no errors in queue
                    else{

                    }

                }
                //network returned an account, will be signed in
                else{
                    curentlyLoadedAccount = tempAccount;
                    accountSignedIn = true;
                    return currentlyLoadedAccont;
                }
            }
            //there is a loaded account so see if the username matches
            else {
                if(currentlyLoadedAccount.accountName.equals(username)) {
                    if(currentlyLoadedAccount.accountPassword.equals(password)) {
                        tempAccount  = networkSignInRequest(username, password, tempErrorQueue);
                        if(tempAccount = null) {
                            if(tempErrorQueue.size > 0){
                                errorIterator = tempErrorQueue.iterator();
                                errorQueue.clear();

                                //there could be several errors each requiring network calls, group them together
                                //have an 2 X n array <call type, data> to send, and 2 x n array <
                                while(errorIterator.hasNext()){
                                    //occurs only if the server has not been updated previously with this account
                                    if(errorIterator.next().equals(ERR_WRONG_USER_OR_PASS){
                                        tempAccount = updateNetworkAccount(currentlyLoadedAccount, tempErrorQueue);
                                        //for some reason the server cannot be updated
                                        if(tempAccount == null) {
                                            if(temp
                                        }
                                    }
                                }

                            }
                        }
                        accountSignedIn = true;
                        return currentlyLoadedAccount;
                    }
                }
            }
        }

        //check if an account is already signed in
        if(!accountSignedIn) {
            //check if an account is loaded
            if(curentlyLoadedAccount!=null) {
                //if an account is not signed in and is loaded, compare username to input
                if(currentlyLoadedAccount.accountName.equals(username)) {

                    //password matches current loaded
                    if(currentlyLoadedAccount.accountPassword.equals(password)){
                        accountSignedIn = true;
                        return currentlyLoadedAccount;
                    }
                    //password does not match current loaded, check server for update, must have equal or greater revision
                    else {
                        AccountAndDeviceInfo tempAccount = networkSignInRequest(username, password, errorQueue);
                        if(tempAccount) {
                            //found a temp account that signed in, check the revision
                            if(tempAccount.revision >= currentlyLoadedAccount){
                                currentlyLoadedAccount = tempAccount;
                                networkSignInConfirm(username, password, errorQueue);
                                accountSignedIn = true;
                                return currentlyLoadedAccount;
                            }

                            //if the revision on the device is higher then the info was not previously loaded to the server
                            //the user will have to find the password by other means
                            else {
                                //must update the server with the account and return not signed in
                                updateServer(currentlyLoadedAccount);
                                accountSignedIn = false;
                                errorQueue.clear();
                                errorQueue.add(ERR_WRONG_USER_OR_PASS);
                                return null;
                            }
                        }
                        else{
                            if(errorQueue.size() > 0){
                                Iterator errorIterator = errorQueue.iterator();
                                while(errorIterator.hashNext()){
                                    if(errorIterator.next().equals(ERR_WRONG_USER_OR_PASS){
                                        accountSignedIn = false;
                                        errorQueue.clear();
                                        errorQueue.add(ERR_WRONG_USER_OR_PASS);
                                        return null;
                                    }
                                }
                            }
                        }
                    }

                }
                else{
                    //the username did not match the loaded account

                    errorQueue.clear();
                    errorQueue.add(ERR_WRONG_USER_OR_PASS);
                    return null;
                }
            }
            else {
                currentlyLoadedAccount = networkSignInRequest(username, password);

                if(currentlyLoadedAccount == null){
                    accountSignedIn = false;
                    errorQueue.clear();
                    errorQueue.add(ERR_ACCOUNT_NOT_LOADED);
                    return null;
                }
            }
        }
        else {
            errorQueue.clear();
            errorQueue.add(ERR_ACCOUNT_SIGNED_IN);
            return null;
        }
    }

    public boolean logOutOfAccount(boolean keepLoaded) {
        if(accountSignedIn){
            if(currentlyLoadedAccount == null) {
                errorQueue.clear();
                errorQueue.add(ERR_ACCOUNT_NOT_LOADED);
                return false;
            }
            else{
                if(keepLoaded) {
                    accountSignedIn = false;
                    return true;
                }
                else {
                    saveToDisk(currentlyLoadedAccount);
                    networkUpdateAccount(currentlyLoadedAccount);
                    currentlyLoadedAccount = null;
                    return true;
                }
            }
        }
    }

    AccountAndDeviceInfo accountAndDeviceInfo;

    public class AccountAndDeviceInfo{


        //need a way to only partially read from disk when capable
        basicConfigAndData configAndData;

        String publicTAG;
        String privateTAG;

        String accountName;
        String accountPassword;
    }
}