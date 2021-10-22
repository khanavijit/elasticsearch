package com.avijit.projects.elasticsearch.api;

import com.avijit.projects.elasticsearch.document.Ticket;
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

    @GetMapping("/info")
    public List<IndexInformation> getClusterInfo() {
        return elasticsearchRestTemplate.indexOps(Ticket.class).getInformation();
    }

    @DeleteMapping("/clear-indices")
    public boolean clearIndices() {
        return elasticsearchRestTemplate.indexOps(Ticket.class).delete();
    }

    @PostMapping("/create-indices")
    public boolean createIndices() {
        return elasticsearchRestTemplate.indexOps(Ticket.class).create();
    }


}
