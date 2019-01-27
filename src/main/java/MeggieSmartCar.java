import com.google.gson.Gson;
import com.smartcar.sdk.*;
import com.smartcar.sdk.data.*;

public class MeggieSmartCar {
   public static void main(String[] args) throws Exception {
      VehicleLocation location = MeggieSmartCar.getVehicleLocation("baeac1d1-b27e-4039-9dda-1355376ece5a");
      System.out.println(location);
   }
   
   public static VehicleLocation getVehicleLocation(String access) throws Exception {
   
      
      SmartcarResponse<VehicleIds> vehicleIdResponse = AuthClient.getVehicleIds(access);

       // the list of vehicle ids
       String[] vehicleIds = vehicleIdResponse.getData().getVehicleIds();
       
       // instantiate the first vehicle in the vehicle id list
       Vehicle vehicle = new Vehicle(vehicleIds[0], access);
   
       // TODO: Request Step 4: Make a request to Smartcar API
       SmartcarResponse<VehicleLocation> location = vehicle.location();

       // Get the vehicle's latitude and longtitude
       return location.getData();
   }
   
}