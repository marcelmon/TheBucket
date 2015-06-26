package ca
import java.util.*;
import java.io.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.util.*;
import android.content.*;
import android.content.res.*;334;

import android.app.*;
import android.os.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}


public class Space extends Fragment{
	
	
	DisplayMetrics metrics;
	
	Activity activity;
	
	Float xPixelDensity;
	Float yPixelDensity;
	
	Float xWidthInches;
	Float yHeightInches;
	
	GridViewArrays gridArrays;
	
	int orientation;
	
	@Override
	public View onCreate(){
		
		//inflate an stuff
		
		activity = getActivity();
		
		metrics = new DisplayMetrics();
		
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		orientation = activity.getResources().getConfiguration().orientation;
		
		
		xPixelDensity = metrics.xdpi;
		yPixelDensity = metrics.ydpi;
		
		xWidthInches = xPixelDensity * metrics.widthPixels;
		yHeightInches = yPixelDensity * metrics.heightPixels;
		
		gridArrays = new GridViewArrays(xWidthInches, yHeightInches, orientation);
		
		return null;
	}
	
	
	public class GridViewArrays{
		
		//MESURE WITH PORTRAIT AS DEFAULT
		Float heightInches;
		float widthInches;
		int zoom; //default is 10. go to 29 and 1
		
		
		float baseArrayHorizontalFillers;
		//BASE ARRAY
		
		//directly fills the first layer
		//width size is always 3 indicies
		//height size is also 3
		
		
		//then find the number of horizontal Fillers needed 
			//must find out if [height - (widthInches)] > 2 * widthInches/3
				//if 2* greater than need 2 filler arrays, if 3 then 2, 4 than 4, 5 than 4...
		
		
		//Perimiter arrays , the smallest is the perimiter around the bucket
		//defined as 3 across the width, and with equal spacing for the height
		
		//the horizontal fillers are above and below the bucket to fill the 
		//additional space, after zooming out, the horizontal fillers
		
		//horizontal filler arrays 
		 
		public GridViewArrays(Float xWidthInches, Float yHeightInches, int orientation){
			
			if(orientation == Configuration.ORIENTATION_PORTRAIT){
				
				widthInches = xWidthInches;
				heightInches = yHeightInches;
			}
			
			else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
				
				widthInches = yHeightInches;
				heightInches = xWidthInches;
				
			}
			
			else if(orientation == Configuration.ORIENTATION_SQUARE){
				
				widthInches = xWidthInches;
				heightInches = yHeightInches;
			}
			else if(orientation == Configuration.ORIENTATION_UNDEFINED){
				return;
			}
			
			
			
			zoom = 10;
			
			baseArrayHorizontalFillers = Math.floor( ((heightInches - widthInches)/(widthInches/3)/2));
			
			
			
			return;
			
			
		}		
	}
	
	GridView devices;
	
	GridAdapter<DeviceInfoObjectArray> currentlyConnectedDevicesAdapter;
	
	
	public void onDataChanged(){
		
		
	}
	
	currentlyConnectedDeviceAdapte.onClick(){
		
		//get id #
		//match to proper in array
	}
	
}



public class ConfigData{
	//USE AS REFERENCE MONITOR
	
	
	public class ThisAccount implements Data{

		
		public ThisAccount(){
			
			thisAccount = new ProfileInfo();
			readPermissions = new HashMap<>();
			writePermissions = new HashMap<>();
		}
		
		ProfileInfo thisAccount;
		
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
					
					case 1: dataIdentifier[i].equals("ENTIREPROFILE");
						switch(serviceToken){
							case 1: serviceToken.equals("root");
								return thisAccount;
								
							default: return null;
						}
				
					case 2: dataIdentifier[i].equals("accountName");
							switch(serviceToken){
								case 1: serviceToken.equals("root");
									return thisAccount.accountName;
								default: return null;
							}
					
					default: return null;
				}
				
			}
			
			return null;
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
	
	public class CurrentConnectedDevices implements Data{

		
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
	
	public interface Data{
		
		
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
		
	}
	

	
	
	
	public class ThisDeviceSettings{
		
		String deviceTAG;
		String name;
		
		long revision;
		
		LinkedList<FileObject> downloadables;
		
		
	}
	
	Hashtable<String, Filter> filterHash;
	//organize filters as hierarchy or hashed by value? if by val then no isEndFilter
	public class Filter {
		
		String filterName;
		String filterType;
		String filterAction;
		
		boolean filterStatus;
		
		boolean isEndFilter;
		//ANOTHER FILTER CAN BE a value, end filter have string values
		String filterValue;
		
		Filter filterBranchValue;
		
	}
	
	//Use an array to store a fixed set n of hashed file name values 0 -> n-1
	//use H(fileName || fileType || other file stuff) to reduce each set 
	//and the amount of required memory for hashing all possible files
	
	//can also sort files by type
	ArrayList<Hashtable<String, FileObject>> fileList;
	
	
	//each device info object will contain only files that have been downloaded
	//keep devices organized by type, or LOCATION <-- will reduce calls to not needing to check
	ArrayList<Hashtable<String, DeviceInfoObject>> previouslyConnectedDevices;
	
	
}

class ProfileInfo{
	
	String hashedPassword;
	String accountName;
	
	String name;
	
}

class DeviceInfoOject{

	//consider array for file list (pointing to the file name strings> for array list adapter,
	//list adapter could include other file info, also isDownload3ed/isDownloading
	LinkedList<FileObject> downloadedFiles;
	

	boolean currentlyConnected;
	boolean currentlyDownloading;
	
	String deviceName;
	String TAG;
	long revision;
	long AccountProfile;
	boolean isAnoymous;
	
	//all are type Bucket by default, can have alternate types
	List<String> deviceTypes;

	
	
	
	
}	

class FileObject{
	
	boolean currentlyDownloading;
	
	String fileName;
	
	Date dateCreated;
	
	String fileType;
	
	boolean isDownloaded;
	
	File filePointer;
	
	DeviceInfoObject owner;
	
}
