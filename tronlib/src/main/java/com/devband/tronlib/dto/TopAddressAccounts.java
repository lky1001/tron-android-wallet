package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 28..
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopAddressAccounts {

    private int total;
    private List<TopAddressAccount> data;
}
