package com.avijit.projects.elasticsearch.repository;

import com.avijit.projects.elasticsearch.document.Ticket;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends ElasticsearchRepository<Ticket, Integer> {

    List<Ticket> findTicketByStatus(String status);

}