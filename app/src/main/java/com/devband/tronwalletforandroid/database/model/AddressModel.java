package com.devband.tronwalletforandroid.database.model;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressModel {

    private String name;

    private String wallet;

    private Date created;

    private Date updated;
}
