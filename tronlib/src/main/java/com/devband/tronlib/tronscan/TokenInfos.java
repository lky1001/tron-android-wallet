package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenInfos {

    private int total;
    private List<TokenInfo> data;
    private int totalAll;
}
