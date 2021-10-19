package com.avijit.projects.elasticsearch.repository;

import com.avijit.projects.elasticsearch.document.Ticket;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends ReactiveCrudRepository<Ticket, String> {



}