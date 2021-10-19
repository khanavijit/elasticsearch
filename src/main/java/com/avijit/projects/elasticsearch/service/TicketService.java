package com.avijit.projects.elasticsearch.service;

import com.avijit.projects.elasticsearch.document.Ticket;
import com.avijit.projects.elasticsearch.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final IndexLargeJsonFile indexLargeJsonFile;


    public Flux<Ticket> getAllTickets() {

        return ticketRepository.findAll();
    }


    public void bulkInsert()  {

        try {
           List<Ticket> tickets= indexLargeJsonFile.getTickets();
            tickets.forEach(ticket -> ticketRepository.save(ticket));
            log.info("Total Size " + tickets.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Mono<Ticket> addMovie(Ticket ticket) {
        return ticketRepository.save(ticket);
    }
}
