package com.avijit.projects.elasticsearch.api;

import com.avijit.projects.elasticsearch.document.Ticket;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexInformation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by nasir on 14/11/17.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("v1")
public class ElasticApiController {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @ApiOperation(value = "Get Cluster Information", nickname = "getClusterInfo",  tags={  })
    @GetMapping("/info")
    public List<IndexInformation> getClusterInfo() {
        return elasticsearchRestTemplate.indexOps(Ticket.class).getInformation();
    }

    @ApiOperation(value = "Clear Indices", nickname = "clearIndices",  tags={  })
    @DeleteMapping("/clear-indices")
    public boolean clearIndices() {
        return elasticsearchRestTemplate.indexOps(Ticket.class).delete();
    }

    @ApiOperation(value = "Create Indices", nickname = "createIndices",  tags={  })
    @PostMapping("/create-indices")
    public boolean createIndices() {
        return elasticsearchRestTemplate.indexOps(Ticket.class).create();
    }


}
