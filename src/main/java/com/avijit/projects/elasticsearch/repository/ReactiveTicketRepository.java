package com.avijit.projects.elasticsearch.repository;

import com.avijit.projects.elasticsearch.document.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ReactiveTicketRepository extends ReactiveCrudRepository<Ticket, String> {

    Flux<Ticket> findTicketByStatus(String status, final Pageable page);


}