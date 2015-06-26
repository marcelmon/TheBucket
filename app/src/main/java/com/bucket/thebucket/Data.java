package com.bucket.thebucket;

/**
 * Created by marcel on 29/04/15.
 */
public interface Data {

    //
//		int ServiceHashFunction(String serviceToken);
//		int UserHashFunction(String userToken);
//		int BothHashFunction(String serviceToken, String userToken);
//

    public Object userReadData(String[] dataIdentifier, String serviceToken, String UserToken);
    public Object userWriteData(String[] dataIdentifier, String serviceToken, String userToken);

    public Object serviceReadData(String[] dataIdentifier, String serviceToken, String UserToken);
    public Object serviceWriteData(String[] dataIdentifier, String serviceToken, String userToken);

    public Object bothReadData(String[] dataIdentifier, String serviceToken, String UserToken);
    public Object bothWriteData(String[] dataIdentifier, String serviceToken, String userToken);

    public Object customFunction(Object object);
}

