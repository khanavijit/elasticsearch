package com.avijit.projects.elasticsearch.service;

import com.avijit.projects.elasticsearch.document.Ticket;
import com.avijit.projects.elasticsearch.helper.Indices;
import com.avijit.projects.elasticsearch.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Profile("elastic")
public class ElasticTicketService implements TicketService{

    private final TicketRepository ticketRepository;
    private final IndexLargeJsonFile indexLargeJsonFile;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;



    public List<Ticket> getTickets(String attribute, String value, String fileId) {
        log.info("invoking service not from cache - fileId " + fileId);


        NativeSearchQuery searchQuery=Indices.getNativeQuery(attribute, value);
        //searchQuery.setPageable(PageRequest.of(49, 200));

        SearchHits<Ticket> searchHits = elasticsearchRestTemplate.search(searchQuery, Ticket.class);
        if (searchHits.hasSearchHits()) {
            return searchHits.getSearchHits().stream()
                    .map( SearchHit ::getContent)
                    .collect(Collectors.toList());
        }
        else{
            return null;
        }
    }


    public List<Ticket> getTicketsInd(String attribute, String value, String fileId) {
        log.info("invoking Ind " + fileId);
        return ticketRepository.findTicketByStatus(value);


    }

}
