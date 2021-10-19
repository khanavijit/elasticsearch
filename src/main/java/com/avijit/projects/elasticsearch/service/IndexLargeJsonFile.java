package com.avijit.projects.elasticsearch.service;


import com.avijit.projects.elasticsearch.document.Ticket;
import com.avijit.projects.elasticsearch.repository.TicketRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
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


    /*public void JsonBulkImport() throws IOException, ExecutionException, InterruptedException {

        File jsonFilePath = new File("C:\\Codebase\\Maersk\\search-elasticcache\\codebase\\elasticsearch\\src\\main\\resources\\data\\databk.json");
        int count = 0;
        int noOfBatch = 1;

        JsonReader jsonReader = new JsonReader(
                new InputStreamReader(
                        new FileInputStream(jsonFilePath), StandardCharsets.UTF_8));
        Gson gson = new GsonBuilder().create();

        jsonReader.beginArray(); //start of json array
        int numberOfRecords = 1;
        final var bulkRequest = new BulkRequest();
        List<XContentBuilder> array= new ArrayList<XContentBuilder>();
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

                array.add(xContentBuilder);

                if (count==50_000) {


                    //addDocumentToESCluser(bulkRequest, noOfBatch, count);
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

            final var indexRequest = new IndexRequest("ticket-store");
            indexRequest.source(array);
            bulkRequest.add(indexRequest);
            //addDocumentToESCluser(bulkRequest,noOfBatch,count);
        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("Total Document Indexed : "+numberOfRecords);
        System.out.println();
    }
*/

     public List<Ticket> getTickets() throws IOException {

        Reader reader = Files.newBufferedReader(Paths.get("C:\\Codebase\\Maersk\\search-elasticcache\\codebase\\elasticsearch\\src\\main\\resources\\data\\databk.json"));

        return new Gson().fromJson(reader, new TypeToken<List<Ticket>>() {}.getType());

    }

}
