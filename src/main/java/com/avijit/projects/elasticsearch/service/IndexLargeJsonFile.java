package com.avijit.projects.elasticsearch.service;


import com.avijit.projects.elasticsearch.document.Ticket;
import com.avijit.projects.elasticsearch.helper.Indices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexLargeJsonFile {


    public static final String TICKET_ID = "id";
        public static final String TICKET_TYPE = "type";
        public static final String TICKET_SUBJECT = "subject";
        public static final String TICKET_DESCRIPTION = "description";
        public static final String TICKET_PRIORITY = "priority";
        public static final String TICKET_STATUS = "status";
    private final RestHighLevelClient restHighLevelClient;

    private Faker faker =new Faker(new Locale("en-IND"));
    String[] priority = {"High", "medium", "Low"};
    String[] status = {"Pending", "Open", "Close","hold"};
    String[] subject = {"Cargo Missing", "Booking Error", "Payment Sent Error"};
    Random random = new Random();
    @Autowired
    private ObjectMapper objectMapper;

    public void JsonBulkImport() throws IOException, ExecutionException, InterruptedException {

        File jsonFilePath = new File("src/main/resources/data/data2.json");
        int count = 0;
        int noOfBatch = 1;



        JsonReader jsonReader = new JsonReader(
                new InputStreamReader(
                        new FileInputStream(jsonFilePath), StandardCharsets.UTF_8));
        Gson gson = new GsonBuilder().create();

        jsonReader.beginArray(); //start of json array
        int numberOfRecords = 1;
        final var bulkRequest = new BulkRequest();
        //List<XContentBuilder> array= new ArrayList<XContentBuilder>();
        while (jsonReader.hasNext()){ //next json array element
            Ticket ticket = gson.fromJson(jsonReader, Ticket.class);
            //do something real
            try {
                XContentBuilder xContentBuilder = jsonBuilder()
                        .startObject()
                        .field(TICKET_ID, ticket.getId())
                        .field(TICKET_TYPE, ticket.getType())
                        .field(TICKET_SUBJECT, ticket.getSubject())
                        .field(TICKET_DESCRIPTION, ticket.getDescription())
                        .field(TICKET_PRIORITY, ticket.getPriority())
                        .field(TICKET_STATUS, ticket.getPriority())
                        .endObject();

                //array.add(xContentBuilder);


                    IndexRequest indexRequest = new IndexRequest(Indices.TICKET_INDEX)
                            .source(xContentBuilder);
                    bulkRequest.add(indexRequest);


                if (count==50_000) {


                    addDocumentToESCluser(bulkRequest, noOfBatch, count);
                    noOfBatch++;
                    count = 0;
                }
            }catch (Exception e) {
                e.printStackTrace();
                //skip records if wrong date in input file
            }
            numberOfRecords++;
            count++;
        }
        jsonReader.endArray();
        if(count!=0){ //add remaining documents to ES

            addDocumentToESCluser(bulkRequest,noOfBatch,count);
        }
        //restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        log.info("Total Document Indexed : "+numberOfRecords);

    }


    public void addDocumentToESCluser(BulkRequest bulkRequest, int noOfBatch, int count) throws IOException {
        if(count==0){
            //org.elasticsearch.action.ActionRequestValidationException: Validation Failed: 1: no requests added;
            return;
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (bulkResponse.hasFailures()) {
            log.info("Bulk Indexing failed for Batch : "+noOfBatch);
            // process failures by iterating through each bulk response item
            int numberOfDocFailed = 0;
            Iterator<BulkItemResponse> iterator = bulkResponse.iterator();
            while (iterator.hasNext()){
                BulkItemResponse response = iterator.next();
                if(response.isFailed()){
                    //System.out.println("Failed Id : "+response.getId());
                    numberOfDocFailed++;
                }
            }
            log.info("Out of "+count+" documents, "+numberOfDocFailed+" documents failed");
           log.info(bulkResponse.buildFailureMessage());
        }else{
            log.info("Bulk Indexing Completed for batch : "+noOfBatch);
        }
    }


     public List<Ticket> getTickets() throws IOException {

        Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/data.json"));

        return new Gson().fromJson(reader, new TypeToken<List<Ticket>>() {}.getType());


    }

    //method to be developed
    public  void  bulkInsertFromFile() throws IOException {

        BulkRequest bulkRequest = new BulkRequest();
        List<Ticket> tickets = getTickets();

        tickets.forEach(ticket -> {
            IndexRequest indexRequest = new IndexRequest(Indices.TICKET_INDEX)
                    .source(Ticket.getAsMap(ticket));
            bulkRequest.add(indexRequest);
        });
        log.info("Total inputs  " + tickets.size());

        try {
            BulkResponse response= restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void generateFakeJsonFile(int count){
        List<Ticket> tickets = getRandomTickets(count);
        FileWriter file=null;
        try {

            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter("src/main/resources/data/data2.json");
            file.write(toJson(tickets));


        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    String toJson(Object object) throws IOException {
        return object == null ? null : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    //for sample
    public  void  fakeBulkInsert(int count) {
        generateFakeJsonFile(count);

        BulkRequest bulkRequest = new BulkRequest();
        List<Ticket> tickets = getRandomTickets(count);

        tickets.forEach(ticket -> {
            IndexRequest indexRequest = new IndexRequest(Indices.TICKET_INDEX)
                    .source(Ticket.getAsMap(ticket));
            bulkRequest.add(indexRequest);
        });
        log.info("Total inputs  " + tickets.size());

        try {
            BulkResponse response= restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public  List<Ticket>  getRandomTickets(int count) {
        List<Ticket> tickets = new ArrayList<>();

        for(int i=0;i < count;i++) {

            Ticket ticket= new Ticket();
            ticket.setId(i);
            ticket.setDescription(faker.book().title());
            ticket.setPriority(getRandom(priority));
            ticket.setStatus(getRandom(status));
            ticket.setSubject(getRandom(subject));
            ticket.setType("incident");
            tickets.add(ticket);
        }
        return tickets;
    }
    private String getRandom(String[] array){
        return array[random.nextInt(array.length)];
    }
}
