package com.avijit.projects.elasticsearch.repository;

import com.avijit.projects.elasticsearch.document.Ticket;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveTicketRepository extends ReactiveCrudRepository<Ticket, String> {


}