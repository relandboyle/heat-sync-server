package com.ubibot.temperaturedata.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountRequest {

    private String accountNumber;

    private String startDate;

    private String endDate;
}
