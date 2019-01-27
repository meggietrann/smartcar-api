import static spark.Spark.*;
import com.google.gson.Gson;
import com.smartcar.sdk.*;
import com.smartcar.sdk.data.*;

//  export JAVA_HOME='/c/Program Files/Java/jdk-10'
public class Main {
  // global variable to save our accessToken
  private static String access;
  private static Gson gson = new Gson();

  public static void main(String[] args) {

      port(8000);
   
       // TODO: Authorization Step 1a: Launch Smartcar authentication dialog
      String clientId = System.getenv("CLIENT_ID");
      String clientSecret = System.getenv("CLIENT_SECRET");
      String redirectUri = System.getenv("REDIRECT_URI");
      String[] scope = {"read_vehicle_info", "read_location", "read_odometer", "control_security"};
      boolean testMode = true;
   
       AuthClient client = new AuthClient(
       clientId,
       clientSecret,
       redirectUri,
       scope,
       testMode
   );

    get("/login", (req, res) -> {
      // TODO: Authorization Step 1b: Launch Smartcar authentication dialog
      String link = client.getAuthUrl();
       res.redirect(link);
       return null;
    });

    get("/exchange", (req, res) -> {
      // TODO: Authorization Step 3: Handle Smartcar response
          String code = req.queryMap("code").value();

        // TODO: Request Step 1: Obtain an access token
        Auth auth = client.exchangeCode(code);
        
        // in a production app you'll want to store this in some kind of persistent storage
        access = auth.getAccessToken();
        
        return access;
    });

    get("/vehicle", (req, res) -> {
      // TODO: Request Step 2: Get vehicle ids

      // TODO: Request Step 3: Create a vehicle

      // TODO: Request Step 4: Make a request to Smartcar API
          SmartcarResponse<VehicleIds> vehicleIdResponse = AuthClient.getVehicleIds(access);
       // the list of vehicle ids
       String[] vehicleIds = vehicleIdResponse.getData().getVehicleIds();
       
       // instantiate the first vehicle in the vehicle id list
       Vehicle vehicle = new Vehicle(vehicleIds[0], access);
   
       // TODO: Request Step 4: Make a request to Smartcar API
       VehicleInfo info = vehicle.info();
       System.out.println(gson.toJson(info));
       
       // Get the odometer
       SmartcarResponse response = vehicle.odometer();
       System.out.println(gson.toJson(response.getData()));
       
       // {
       //     "id": "36ab27d0-fd9d-4455-823a-ce30af709ffc",
       //     "make": "TESLA",
       //     "model": "Model S",
       //     "year": 2014
       // }
       
       res.type("application/json");
       
       // Browser
       // return gson.toJson(info);
       return gson.toJson(response.getData());

    });
  }
}
