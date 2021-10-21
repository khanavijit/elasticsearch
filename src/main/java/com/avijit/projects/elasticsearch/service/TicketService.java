package com.avijit.projects.elasticsearch.service;

import org.avijit.projects.generated.model.Ticket;

import java.util.List;

public interface TicketService {

    public List<Ticket> getTickets(String attribute, String value);
    public void bulkInsert();
}
