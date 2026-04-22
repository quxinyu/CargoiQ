// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.model.onerecord.Referencable;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.datatypes.XMLDatatypeUtil;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.util.Models;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public abstract class ModelHandler<M extends Referencable> {

    public abstract M fromModel(IRI subject, final Model model);

    public abstract Model fromJava(final M entity);

    protected IRI getSubject(IRI predicate, Value object, Model model) {
        return Models.subjectIRI(model.filter(null, predicate, object))
                .orElseThrow(NoSuchElementException::new);
    }

    protected Set<IRI> getSubjects(IRI predicate, Value object, Model model) {
        Set<IRI> iris = Models.subjectIRIs(model.filter(null, predicate, object));
        if (iris.isEmpty()) {
            throw new NoSuchElementException();
        }

        return iris;
    }

    protected IRI getObject(IRI subject, IRI predicate, Model model) {
        return findObject(subject, predicate, model).orElseThrow(NoSuchElementException::new);
    }

    protected Optional<IRI> findObject(IRI subject, IRI predicate, Model model) {
        return Models.objectIRI(model.filter(subject, predicate, null));
    }

    protected Set<IRI> findObjects(IRI subject, IRI predicate, Model model) {
        return Models.objectIRIs(model.filter(subject, predicate, null));
    }

    protected Set<IRI> getObjects(IRI subject, IRI predicate, Model model) {
        Set<IRI> iris = findObjects(subject, predicate, model);
        if (iris.isEmpty()) {
            throw new NoSuchElementException();
        }

        return iris;
    }

    protected Literal getObjectLiteral(IRI subject, IRI predicate, Model model) {
        return findObjectLiteral(subject, predicate, model).orElseThrow(NoSuchElementException::new);
    }

    protected Optional<Literal> findObjectLiteral(IRI subject, IRI predicate, Model model) {
        return Models.objectLiteral(model.filter(subject, predicate, null));
    }

    protected String getObjectString(IRI subject, IRI predicate, Model model) {
        return findObjectString(subject, predicate, model).orElseThrow(NoSuchElementException::new);
    }

    protected Optional<String> findObjectString(IRI subject, IRI predicate, Model model) {
        return Models.objectString(model.filter(subject, predicate, null));
    }

    protected Set<String> findObjectStrings(IRI subject, IRI predicate, Model model) {
        return Models.objectStrings(model.filter(subject, predicate, null));
    }

    protected String getObjectUri(IRI subject, IRI predicate, Model model) {
        return findObjectUri(subject, predicate, model).orElseThrow(NoSuchElementException::new);
    }

    protected Optional<String> findObjectUri(IRI subject, IRI predicate, Model model) {
        Optional<String> value = findObjectString(subject, predicate, model);
        if (value.isPresent() && !XMLDatatypeUtil.isValidAnyURI(value.get())) {
            throw new IllegalArgumentException("Invalid xsd:anyURI value for subject ["
                    + subject.stringValue() + "] and predicate [" + predicate.stringValue()
                    + "], was: [" + value.get());
        }

        return value;
    }

    public Model merge(Model first, Model... rest) {
        return merge(Stream.concat(Stream.of(first), Stream.of(rest)).toArray(Model[]::new));
    }

    private Model merge(Model... models) {
        Model merged = new DynamicModelFactory().createEmptyModel();
        for (Model model : models) {
            merged.addAll(model);
            model.getNamespaces().forEach(merged::setNamespace);
        }

        return merged;
    }
}
