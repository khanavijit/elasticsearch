package com.avijit.projects.elasticsearch.api;

import com.avijit.projects.elasticsearch.document.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.IndexInformation;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RequiredArgsConstructor
@RestController
@RequestMapping("/elastic-cluster")
public class ElasticResource {

    private final  ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;
    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;


    @GetMapping("/info")
    public Flux<IndexInformation> getClusterInfo() {
        return reactiveElasticsearchTemplate.indexOps(Ticket.class).getInformation();
    }

    @DeleteMapping("/clear-indices")
    public Mono<String> clearIndices() {
        return reactiveElasticsearchOperations.indexOps(Ticket.class).delete().flatMap(this::getDeleted);
    }

    private Mono<String> getDeleted(Boolean isDeleted) {
        if (isDeleted) {
            return Mono.just("Deleted");
        } else {
            return Mono.just("Unable to delete");
        }
    }
}
