package com.ubibot.temperaturedata.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.POST, params = "transactionId")
    private void testWithTransactionId(@RequestParam String transactionId) {
        log.info("\nTest with transactionId: {}", transactionId);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST, headers = "TestRequest=referenceNumber")
    private void testWithReferenceNumber(@RequestBody ReferenceRequest reqBody) {
        log.info("\nTest with referenceNumber: {}", reqBody.getReferenceNumber());
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST, params = "accountNumber")
    private void testWithAccountNumberAndDates(@RequestBody AccountRequest reqBody) {
        log.info("\nTest with accountNumber: {}, startDate: {}, endDate: {}",
                reqBody.getAccountNumber(),
                reqBody.getStartDate(),
                reqBody.getEndDate());
    }
}
