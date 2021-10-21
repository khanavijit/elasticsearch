package com.avijit.projects.elasticsearch.api;

import com.avijit.projects.elasticsearch.service.IndexLargeJsonFile;
import com.avijit.projects.elasticsearch.service.TicketService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.avijit.projects.generated.api.SearchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
@Slf4j
public class DataApiController {
    private final IndexLargeJsonFile indexLargeJsonFile;

    @Autowired
    private final TicketService ticketService;


    @ApiOperation(value = "Data Preparation", nickname = "bulkInsert", notes = "", tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation") })
    @GetMapping(value = "/insert")
    ResponseEntity<String> insert() throws IOException, ExecutionException, InterruptedException {

        //ticketService.bulkInsert();
        indexLargeJsonFile.JsonBulkImport();
        return ResponseEntity.ok("{\"Result\" : \"Success\"}");

    }
    @ApiOperation(value = "Fake bulk insert", nickname = "fakeBulkInsert", notes = "", tags={  })
    @PostMapping("/faker/insert/{count}")
    public void bulkInsertWithFakeData(@PathVariable("count") int count){

        indexLargeJsonFile.fakeBulkInsert(count);
    }



}
