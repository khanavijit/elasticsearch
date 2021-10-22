package com.avijit.projects.elasticsearch.api;

import com.avijit.projects.elasticsearch.service.IndexLargeJsonFile;
import com.avijit.projects.elasticsearch.service.TicketService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
@Slf4j
public class DataApiController {
    private final IndexLargeJsonFile indexLargeJsonFile;

    @Autowired
    private final TicketService ticketService;


    @ApiOperation(value = "Data Refresh", nickname = "dataRefresh", notes = "", tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Operation") })
    @GetMapping(value = "/insert")
    ResponseEntity<String> insert() throws IOException, ExecutionException, InterruptedException {

        indexLargeJsonFile.JsonBulkImport();
        return ResponseEntity.ok("{\"Result\" : \"Success\"}");

    }
    @ApiOperation(value = "Fake Data Creation", nickname = "fakeDataCreation", notes = "", tags={  })
    @PostMapping("/fakeDataFileCreation/{count}")
    public ResponseEntity<String> fakeDataFileCreation(@PathVariable("count") int count){

        indexLargeJsonFile.fakeDataFileCreation(count);
        return ResponseEntity.ok("{\"Result\" : \"File Created\"}");
    }



}
