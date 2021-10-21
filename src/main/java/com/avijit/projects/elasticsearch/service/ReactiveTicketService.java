package com.avijit.projects.elasticsearch.service;

import com.avijit.projects.elasticsearch.document.Ticket;
import com.avijit.projects.elasticsearch.repository.ReactiveTicketRepository;
import com.avijit.projects.elasticsearch.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
@Slf4j
@Profile("reactive")
public class ReactiveTicketService implements TicketService {

    private final ReactiveTicketRepository reactiveTicketRepository;
    private final IndexLargeJsonFile indexLargeJsonFile;
    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;
    private final ModelMapper modelMapper;







    public void bulkInsert()  {


        try {
            List<Ticket> tickets= indexLargeJsonFile.getTickets();
            reactiveTicketRepository.saveAll(tickets).doOnNext(System.out::println).subscribe();
            log.info("Total Size " + tickets.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public List<org.avijit.projects.generated.model.Ticket> getTickets(String attribute, String value) {
        NativeSearchQuery searchQuery ;
        if(attribute.equalsIgnoreCase("id")){
            searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery(attribute,value)
                            )
                    .build();
        }
        else{
            searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery(attribute,value)
                            .operator(Operator.AND)
                            .lenient(true)
                            .fuzziness(Fuzziness.ONE)
                            .prefixLength(3))
                    .build();
        }

        //log.info("Reactive - Query " + searchQuery.getQuery());
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder() .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
        .build();


        try {
            Flux<SearchHit<Ticket>> allTickets = reactiveElasticsearchTemplate.search(searchQuery, Ticket.class);


            /* StreamSupport.stream(allTickets.toIterable().spliterator(), true)
                    .map(ticketSearchHit ->  modelMapper.map(ticketSearchHit.getContent(), org.avijit.projects.generated.model.Ticket.class))
                    .collect(Collectors.toList());*/

             allTickets.toStream().forEach(ticketSearchHit -> ticketSearchHit.getContent());


        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;


    }


}
