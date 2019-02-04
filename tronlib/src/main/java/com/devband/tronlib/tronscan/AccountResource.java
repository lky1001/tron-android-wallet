package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResource {

    @JsonProperty("frozen_balance_for_energy")
    FrozenBalanceForEnergy frozenBalanceForEnergy;
}
