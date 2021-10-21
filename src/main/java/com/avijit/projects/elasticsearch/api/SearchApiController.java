package com.avijit.projects.elasticsearch.api;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
@Slf4j
public class SearchApiController implements SearchApi {

    @Autowired
    private final TicketService ticketService;

    /**
     * GET /search : seaerches the user support tickets
     *
     * @param attribute The name of the attribute that is been searched (required)
     * @param value The value to find (required)
     * @return successful operation (status code 200)
     */
    @ApiOperation(value = "seaerches the user support tickets", nickname = "search", notes = "", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation") })
    @GetMapping(value = "/search")
     public ResponseEntity<List<org.avijit.projects.generated.model.Ticket>> search(@NotNull
                                         @ApiParam(value = "The name of the attribute that is been searched", required = true)
                                         @Valid @RequestParam(value = "attribute", required = true, defaultValue = "subject")
                                                 String attribute,
                                               @NotNull @ApiParam(value = "The value to find", required = true,defaultValue = "missing")
                                         @Valid
                                         @RequestParam(value = "value", required = true)
                                                 String value) {

        log.info(">>> search {} as {}", attribute,value);
        return ResponseEntity.ok(ticketService.getTickets(attribute,value));

    }



}
