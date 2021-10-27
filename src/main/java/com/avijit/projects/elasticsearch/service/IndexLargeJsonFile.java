package com.avijit.projects.elasticsearch.service;


import com.avijit.projects.elasticsearch.document.Ticket;
import com.avijit.projects.elasticsearch.helper.Constants;
import com.avijit.projects.elasticsearch.helper.Indices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexLargeJsonFile {

    public static final Map<String, String> FILE_DETAILS = new HashMap<>();


    @PostConstruct
    public void configureFileIdStartUp(){
        FILE_DETAILS.put(Constants.FILE_ID_KEY,UUID.randomUUID().toString());
        log.info("File id - " + FILE_DETAILS.get(Constants.FILE_ID_KEY) );
    }

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
    String[] description = {"Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris ex exercitation amet et proident. Ipsum fugiat aute dolore tempor nostrud velit ipsum.",
            "Aliquip excepteur fugiat ex minim ea aute eu labore. Sunt eiusmod esse eu non commodo est veniam consequat.",
            "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit"};
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

        jsonReader.beginArray();
        int numberOfRecords = 1;
        var bulkRequest = new BulkRequest();
        while (jsonReader.hasNext()){
            Ticket ticket = gson.fromJson(jsonReader, Ticket.class);
            try {
                XContentBuilder xContentBuilder = jsonBuilder()
                        .startObject()
                        .field(TICKET_ID, ticket.getId())
                        .field(TICKET_TYPE, ticket.getType())
                        .field(TICKET_SUBJECT, ticket.getSubject())
                        .field(TICKET_DESCRIPTION, ticket.getDescription())
                        .field(TICKET_PRIORITY, ticket.getPriority())
                        .field(TICKET_STATUS, ticket.getStatus())
                        .endObject();

                String json = Strings.toString(xContentBuilder);
                System.out.println(json);
                    IndexRequest indexRequest = new IndexRequest(Indices.TICKET_INDEX)
                            .source(xContentBuilder);
                    bulkRequest.add(indexRequest);


                if (count==50000) {


                    addDocumentToESCluser(bulkRequest, noOfBatch, count);
                    bulkRequest = new BulkRequest();
                    noOfBatch++;
                    count = 0;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            numberOfRecords++;
            count++;
        }
        jsonReader.endArray();
        if(count!=0){

            addDocumentToESCluser(bulkRequest,noOfBatch,count);
        }
        log.info("Total Document Indexed : "+numberOfRecords);
        FILE_DETAILS.put(Constants.FILE_ID_KEY,UUID.randomUUID().toString());
        FILE_DETAILS.put(Constants.FILE_SIZE_KEY,jsonFilePath.length()+"");
        FILE_DETAILS.put(Constants.FILE_ELEMENTS_KEY,numberOfRecords+"");

    }


    public void addDocumentToESCluser(BulkRequest bulkRequest, int noOfBatch, int count) throws IOException {
        if(count==0){
            return;
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (bulkResponse.hasFailures()) {
            log.info("Bulk Indexing failed for Batch : "+noOfBatch);
            int numberOfDocFailed = 0;
            Iterator<BulkItemResponse> iterator = bulkResponse.iterator();
            while (iterator.hasNext()){
                BulkItemResponse response = iterator.next();
                if(response.isFailed()){
                    numberOfDocFailed++;
                }
            }
            log.info("Out of "+count+" documents, "+numberOfDocFailed+" documents failed");

           log.info(bulkResponse.buildFailureMessage());
        }else{
            log.info("Bulk Indexing Completed for batch : "+noOfBatch);

        }
    }


    public void generateFakeJsonFile(int count){
        List<Ticket> tickets = getRandomTickets(count);

        FileWriter file=null;
        try {

            file = new FileWriter("src/main/resources/data/data2.json");
            file.write(toJson(tickets));


        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    String toJson(Object object) throws IOException {
        return object == null ? null : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }


    public  void fakeDataFileCreation(int count) {
        generateFakeJsonFile(count);

    }


    public  List<Ticket>  getRandomTickets(int count) {
        List<Ticket> tickets = new ArrayList<>();


        for(int i=0;i < count;i++) {
            String description = faker.educator().course() + " " + faker.educator().campus() +" "+faker.educator().secondarySchool()+ " " + faker.educator().campus() +" "+faker.educator().secondarySchool();
            Ticket ticket= new Ticket();
            ticket.setId(i);
            ticket.setDescription(description);
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
