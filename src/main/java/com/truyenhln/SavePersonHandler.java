package com.truyenhln;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.truyenhln.bean.PersonRequest;
import com.truyenhln.bean.PersonResponse;
import java.util.Date;

public class SavePersonHandler implements RequestHandler<PersonRequest, PersonResponse> {
    private DynamoDB dynamoDb;

    private String DYNAMODB_TABLE_NAME = "HelloWorldDatabase";

    private Regions REGION = Regions.AP_SOUTHEAST_1;

    public PersonResponse handleRequest(PersonRequest personRequest, Context context) {
        String fullName = "Welcome " + personRequest.getFirstName() + " " + personRequest.getLastName();
        initDynamoDbClient();
        persistData(personRequest);
        PersonResponse personResponse = new PersonResponse();
        personResponse.setMessage(fullName);
        return personResponse;
    }

    private PutItemOutcome persistData(PersonRequest personRequest) throws ConditionalCheckFailedException {
        String name = personRequest.getFirstName() + " " + personRequest.getLastName();
        Date date = new Date();
        return this.dynamoDb.getTable(this.DYNAMODB_TABLE_NAME)
                            .putItem((new PutItemSpec())
                                             .withItem((new Item())
                                                               .withString("ID", name)
                                                               .withString("LatestGreetingTime", date.toString())));
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(this.REGION));
        this.dynamoDb = new DynamoDB((AmazonDynamoDB) client);
    }
}
