// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.iata.onerecord.CARGO;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.stream.StreamSupport;

@Component
@ApplicationScoped
public class CargoOntologyService extends OntologyService {

    public CargoOntologyService() {
        super("IATA-1R-DM-Ontology.ttl");
    }

    /**
     * Checks whether any of the given types super class is a {@link CARGO#LogisticsObject}.
     *
     * @param types the type to check
     * @return {@code true} if it is a {@link CARGO#LogisticsObject}, otherwise {@code false}
     */
    public boolean isLogisticsObject(Iterable<String> types) {
        return isLogisticsObject(StreamSupport.stream(types.spliterator(), false).toArray(String[]::new));
    }

    /**
     * Checks whether any of the given types super class is a {@link CARGO#LogisticsObject}.
     *
     * @param types the type to check
     * @return {@code true} if it is a {@link CARGO#LogisticsObject}, otherwise {@code false}
     */
    public boolean isLogisticsObject(String... types) {
        return Arrays.asList(types).contains(CARGO.LogisticsObject.stringValue()) || Arrays.stream(types)
                .flatMap(type -> getReasoner().superClasses(getDataFactory().getOWLClass(type)))
                .distinct()
                .anyMatch(owlClass -> getDataFactory().getOWLClass(CARGO.LogisticsObject.stringValue()).equals(owlClass));
    }
}
