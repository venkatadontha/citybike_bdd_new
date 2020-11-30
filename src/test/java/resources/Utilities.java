package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class Utilities {

	// This Method is to get the base URL from the config.properties file
	public static String getURI(String key) throws IOException 
	{
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream("src/test/java/resources/config.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}

	// This method is to find the total count of Networks in the Response body
	public int getCount(Response response, String message) 
	{
		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		int count = js.getInt(message);
		return count;
	}

	// This method check the city name = Frankfurt und return its index value response body and return its index
	public int getIndex(Response response, String message, String city) {
		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		int index;
		int count = getCount(response, message);
		
		for (int i = 0; i < count; i++) {
			String c = js.getString("networks[" + i + "].location.city");
			if (city.equalsIgnoreCase(c)) 
			{
				System.out.println("---------->The Index value of Networks for " + city + " is:" + i);
				index = i;
				return index;
			}
		}
		return 0;
	}

	// getting the Longitude and Latitude for Frankfurt and asserting their corresponding values against the parameters passed under Feature file
	public boolean getCity(Response response, String message, String city, String country) {

		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		int index = getIndex(response,message,city);

		String cityVal = js.getString("networks[" + index + "].location.city");
		String countryVal = js.getString("networks[" + index + "].location.country");
		
		System.out.println("----------> City listed in Networks data is: " + cityVal);
		System.out.println("----------> Country listed in Networks data is: " +countryVal);

		if (city.equalsIgnoreCase(city) && country.equalsIgnoreCase(country)) {
			System.out.println("----------> " + city + " is listed correctly against " +country + " in the Networks data.");
			System.out.println("\n");
			return true;
		} else {
			System.out.println("----------> " + city + " is not listed against " +country + " in the Networks data.");
			System.out.println("\n");
			return false;
		}

	}
	
	// this function return type is Hash Map, where it stores city, country, Longitude and Latitude value against the parameter which passed in feature file
	public HashMap<String, String> getCityData(Response response, String message, String city){

		HashMap<String, String> frankfurtData = new HashMap<>();
		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		int index = getIndex(response,message,city);

		String varCity = js.getString("networks[" + index + "].location.city");
		String varLatitude = js.getString("networks[" + index + "].location.latitude");
		String varLongitude = js.getString("networks[" + index + "].location.longitude");
		String varCountry = js.getString("networks[" + index + "].location.country");

		frankfurtData.put("country",varCountry );
		frankfurtData.put("city",varCity );
		frankfurtData.put("latitude",varLatitude );
		frankfurtData.put("longitude",varLongitude );

		return frankfurtData;

	}

}