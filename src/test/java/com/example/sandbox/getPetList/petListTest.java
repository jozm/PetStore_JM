package com.example.sandbox.getPetList;

import com.example.sandbox.Common;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.report.TestListener;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.example.sandbox.util.constans.Tags.SMOKE;
import static com.example.sandbox.util.constans.TestData.responseTimeLimit;


@Listeners(TestListener.class)
public class petListTest extends Common {

    @Test(enabled = true,groups = {SMOKE},description ="Find pet by status, status value is valid")
    public void testPositiveFindByStatus() {
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("status","available");
        //queryParams.put("asd","asd");
        //queryParams.put("maki","kakadu");

        Response  response = getUrl(findByStatus, queryParams);

        Assert.assertEquals(response.getStatusCode(),200,"Invalid response code");
        Assert.assertTrue(response.getTime() < responseTimeLimit, "Response time limit exceeded! Current value: " + response.getTime());

        // All value of "status" should be 'available'
        List<String> statuses = response.jsonPath().getList("status");
        Assert.assertNotNull(statuses, "The response is not a List of pets!");
        for(String s : statuses) {
            Assert.assertEquals(s, "available", "Element has found with wrong status value!");
        }


    }

    @Test(enabled = true,groups = {SMOKE},description ="Find pet by status, status value is invalid")
    public void testNegativeFindByStatus(){
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("status","invalid_status_asd");
    
        Response  response = getUrl(findByStatus,queryParams);
        Assert.assertEquals(response.getStatusCode(),200,"Invalid response code");
        Assert.assertTrue(response.getTime() < responseTimeLimit, "Response time limit exceeded! Current value: " + response.getTime());
    }
}
