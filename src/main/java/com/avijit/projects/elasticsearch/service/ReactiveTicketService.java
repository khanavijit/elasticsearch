package com.avijit.projects.elasticsearch.service;

import com.avijit.projects.elasticsearch.document.Ticket;
import com.avijit.projects.elasticsearch.helper.Indices;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;




    public List<Ticket> getTickets(String attribute, String value, String fileId) {
        log.info("invoking service not from cache - fileId " + fileId);
        List<Ticket> tickets = new ArrayList<>();

        NativeSearchQuery searchQuery=Indices.getNativeQuery(attribute, value);
        searchQuery.setPageable(PageRequest.of(49, 200));


        try {
            Flux<SearchHit<Ticket>> allTickets = reactiveElasticsearchTemplate.search(searchQuery, Ticket.class);
            allTickets.toStream().forEach(ticketSearchHit ->tickets.add( ticketSearchHit.getContent()));
            return tickets;

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;


    }



    @Override
    public List<Ticket> getTicketsInd(String attribute, String value, String s) {
        List<Ticket> tickets= new ArrayList<>();
        try {
            Flux<Object> allTickets =  reactiveTicketRepository.findTicketByStatus(value, PageRequest.of(49, 200)).collectList().flatMapMany(Flux::just);


            allTickets.toStream().forEach(ticketSearchHit ->tickets.addAll( ((ArrayList<Ticket>)ticketSearchHit)));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return tickets;
    }


}
