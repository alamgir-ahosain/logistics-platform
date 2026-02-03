package csembstu.alamgir.server.controller;

import org.springframework.web.bind.annotation.RestController;

import csembstu.alamgir.server.dto.response.LogisticsSummaryResponse;
import csembstu.alamgir.server.service.LogisticsSummaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class LogisticsSummaryController {

    @Autowired
    private LogisticsSummaryService logisticsSummaryService;

    @GetMapping("api//network/summary")
    public LogisticsSummaryResponse getNetworkSummary() {
        return logisticsSummaryService.logisticsSummaryResponse();

    }

}
