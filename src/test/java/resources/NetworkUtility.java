package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Location;

/**
 * Contains utility methods for the Network JSON
 */
public class NetworkUtility {

	private static final String ROOT_ELEMENT = "networks";

	/**
	 * Returns the URL for then endpoint
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static String getURI(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream("src/test/java/resources/config.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}

	/**
	 * Returns the size the Json array for the root element
	 * 
	 * @param response
	 * @return the array size
	 */
	public int getNetWorksLength(Response response) {
		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		return js.getList(ROOT_ELEMENT).size();
	}

	/**
	 * Returns the array index for the given city
	 * 
	 * @param response
	 * @param city
	 * @return the Index
	 */
	public int getIndex(Response response, String city) {

		int index = 0;
		int count = getNetWorksLength(response);
		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		for (int i = 0; i < count; i++) {
			String c = js.getString("networks[" + i + "].location.city");
			if (city.equalsIgnoreCase(c)) {
				System.out.println("---------->The Index value of City " + city + " is:" + i);
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * Returns the <code>Location</code> object for the given city
	 */
	public Location getLocation(Response response, String city) {

		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		int index = getIndex(response, city);

		String cityVal = js.getString("networks[" + index + "].location.city");
		String country = js.getString("networks[" + index + "].location.country");
		String latitude = js.getString("networks[" + index + "].location.latitude");
		String longitude = js.getString("networks[" + index + "].location.longitude");

		Location location = Location.builder().city(cityVal).country(country).latitude(latitude).longitude(longitude).build();
		System.out.println("Location of City"+ city+" is: " + location.toString());
		return location;
	}


}