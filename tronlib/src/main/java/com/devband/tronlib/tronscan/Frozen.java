package com.devband.tronlib.tronscan;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Frozen {

    private long total;
    private List<FrozenTrx> balances;
}
