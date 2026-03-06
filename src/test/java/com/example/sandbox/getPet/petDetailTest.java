package com.example.sandbox.getPet;

import com.example.sandbox.Common;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.report.TestListener;

import java.util.Map;
import java.util.TreeMap;

import static com.example.sandbox.util.constans.Tags.SMOKE;
import static com.example.sandbox.util.constans.TestData.responseTimeLimit;

@Listeners(TestListener.class)
public class petDetailTest extends Common {

    @Test(enabled = true,groups = {SMOKE},description ="Find pet by ID, ID exists")
    public void testPositiveGetPetById(){
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("status","available");
        Response  response0= getUrl(findByStatus, queryParams);
        Assert.assertEquals(response0.getStatusCode(),200,"Invalid response code");
        String id = response0.jsonPath().get("find{it.status.equals('available')}.id").toString();

        Response  response2 = getUrl(petById.replace("{petId}",id));
        Assert.assertEquals(response2.getStatusCode(),200,"Invalid response code");
        Assert.assertTrue(response2.getTime() < responseTimeLimit, "Response time limit exceeded! Current value: " + response2.getTime());

        // Mandatory keys
        Assert.assertNotNull(response2.jsonPath().get("id"), "Missing 'id' key in response!");
        Assert.assertNotNull(response2.jsonPath().get("name"), "Missing 'name' key in response!");
        Assert.assertNotNull(response2.jsonPath().get("photoUrls"), "Missing 'photoUrls' key in response!");

        // Value types
        Object nameValue = response2.jsonPath().get("name");
        Assert.assertTrue(nameValue instanceof String, "Value of 'name' is not String!");

        Object photoUrlsValue = response2.jsonPath().get("photoUrls");
        Assert.assertTrue(photoUrlsValue instanceof java.util.List, "Value of 'photoUrls' is not List!");

        // Status enum
        String status = response2.jsonPath().getString("status");
        if (status != null) {
            boolean isValidStatus = status.equals("available") || status.equals("pending") || status.equals("sold");
            Assert.assertTrue(isValidStatus, "Ivalid 'status' value: " + status + "!");
        }
    }

    @Test(enabled = true, groups = {SMOKE}, description = "Find pet by ID, ID does not exist")
    public void testNegativeGetPetById() {
        // ID is negative
        String nonExistentId = "-999888";
        Response response = getUrl(petById.replace("{petId}", nonExistentId));
        Assert.assertEquals(response.getStatusCode(), 404, "Invalid response code!");
        Assert.assertTrue(response.getTime() < responseTimeLimit, "Response time limit exceeded! Current value: " + response.getTime());
    }
}
