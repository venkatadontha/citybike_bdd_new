package stepDef;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Location;
import resources.NetworkUtility;

public class MyStepDefs extends NetworkUtility {
    RequestSpecification request;
    Response response;
    int responseStatusCode;

    @When("user triggers GET API request call")
    public void GET_APIRequest() throws IOException 
    {
    	System.out.println("\n**********Step-1 : Get Request for the API Endpoint");
    	response = given()
        		 .when()
        		 .get(getURI("baseUrl"))
        		 .then()
                 .extract()
                 .response();
        System.out.println("\n");
    }


    @Then("Status code should be {int}")
    public void fetchStatusCode(int statusCode) 
    {
    	int responseStatusCode = response.getStatusCode();
        System.out.println("\n**********Step-2 : Status Code for the GET API request is: " + responseStatusCode);
        System.out.println("\n");
        assertEquals("\n Test Failed because " + responseStatusCode + " does not match with Expected", statusCode,responseStatusCode);
    }

    @Then("user should get the total Networks Count {int} in Response body")
    public void fetchNetworkCount(int count) 
    {
        System.out.println("\n**********Step-3 : Network Count Validation Test");
        System.out.println("\n");
        int NetworksCount = (getNetWorksLength(response));
        System.out.println("---------->The total number of Networks Count is : " + NetworksCount );
        System.out.println("\n");
        assertEquals("\n Test Failed because " + responseStatusCode + " does not match with Expected", count, NetworksCount);

    }

    @Then("^user should get the city \"([^\"]*)\" and \"([^\"]*)\"$")
    public void fetchCityAndCountry(String city, String country) 
    {
        System.out.println("\n**********Step-4 : Test to validate that the city and country");
        System.out.println("\n");
        Location location =  getLocation(response, city);
        assertNotNull("City "+ city+" not found", location);
        assertEquals("City name is not matching", location.getCity(), city);
        assertEquals("Country name is not matching", location.getCountry(), country);
    }

    @Then("^user should get (.+) and (.+) corresponding to city \"([^\"]*)\"$")
    public void fetchLatitudeAndLongitude(String latitude, String longitude,  String city) 
    {
        System.out.println("\n**********Step-5 :Longitude and Latitude for " + city + " is : ");
        System.out.println("\n");
        Location location =  getLocation(response, city);
        assertNotNull("City "+ city+" not found", location);
        assertEquals("Latitude is not matching", latitude, location.getLatitude());
        assertEquals("Longitude is not matching",  longitude, location.getLongitude());
    }

}
