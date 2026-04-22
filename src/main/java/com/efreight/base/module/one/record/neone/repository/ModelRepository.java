// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.Referencable;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelCollector;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


public abstract class ModelRepository<Entity extends Referencable> {

    private final static Logger log = LoggerFactory.getLogger(ModelRepository.class);

    protected Repository repository;

    protected ModelHandler<Entity> modelHandler;

    protected IdProvider idProvider;

    ModelRepository() {
        // CDI dummy constructor
    }

    public ModelRepository(Repository repository, ModelHandler<Entity> modelHandler, IdProvider idProvider) {
        this.repository = repository;
        this.modelHandler = modelHandler;
        this.idProvider = idProvider;
    }

    public Repository getRepository() {
        return repository;
    }

    public void persist(IRI iri, Entity entity, RepositoryConnection connection) {
        Model model = addIdToRootStatements(modelHandler.fromJava(entity), iri);
        persist(model, connection);
    }

    public void persist(Entity entity, RepositoryConnection connection) {
        log.debug("Add entity to repository [{}]", entity);
        Model model = modelHandler.fromJava(entity);
        connection.add(model);
    }

    public void persist(Model model, RepositoryConnection connection) {
        log.debug("Add model to repository");
        connection.add(model);
    }

    public Optional<Entity> findByIri(IRI iri, RepositoryConnection connection) {
        Predicate<IRI> continueRecursionIf = i -> idProvider.parse(i).isInternal();
        return findRecursively(iri, continueRecursionIf, connection);
    }

    public Optional<Entity> findGraphByIri(IRI iri, RepositoryConnection connection) {
        // Continue recursion down to the lowest level.
        Predicate<IRI> continueRecursionIf = subject -> true;
        return findRecursively(iri, continueRecursionIf, connection);
    }

    protected Optional<Entity> findRecursively(IRI iri,
                                               Predicate<IRI> continueRecursionIf,
                                               RepositoryConnection connection) {
        log.debug("Fetching statements for subject [{}]", iri.stringValue());
        Entity entity = null;
        Model baseStatements = QueryResults.asModel(connection.getStatements(
                iri,
                null,
                null
        ));
        if (!baseStatements.isEmpty()) {
            Model baseModel = new DynamicModelFactory().createEmptyModel();
            baseModel.addAll(baseStatements);

            List<Statement> list = traverse(baseStatements, new ArrayList<>(), continueRecursionIf, connection);
            baseModel.addAll(list);
            entity = modelHandler.fromModel(iri, baseModel);
        }
        return Optional.ofNullable(entity);
    }

    public boolean exists(IRI subject, RepositoryConnection connection) {
        return connection.hasStatement(subject, null, null, true);
    }

    public void delete(IRI subject, IRI predicate, RepositoryConnection connection) {
        connection.remove(subject, predicate, null);
    }

    public void delete(IRI subject, IRI predicate, Literal object, RepositoryConnection connection) {
        connection.remove(subject, predicate, object);
    }

    public int delete(Resource subject, IRI predicate, Value object, RepositoryConnection connection) {
        int affectedTriples = QueryResults.asList(
                connection.getStatements(subject, predicate, object, true)
        ).size();
        connection.remove(subject, predicate, object);
        return affectedTriples;
    }

    // Delete recursively.
    public void deleteAll(IRI subject, RepositoryConnection connection) {
        Model rootStatements = QueryResults.asModel(connection.getStatements(subject, null, null));
        List<Statement> graphStatements = new ArrayList<Statement>();
        traverse(rootStatements, graphStatements, iri -> true, connection);
        graphStatements.forEach(connection::remove);
    }

    public void add(IRI iri, IRI predicate, String value, RepositoryConnection connection) {
        this.add(iri, predicate, Values.literal(value), connection);
    }

    public void add(Resource subject, IRI predicate, Value object, RepositoryConnection connection) {
        connection.add(subject, predicate, object);
    }

    /**
     * Resolves all {@link org.eclipse.rdf4j.model.BNode} objects in the statements recursively.
     *
     * @param statements    list of statements
     * @param newStatements new list of statements, mostly initially an empty list
     * @param conn          connection to the repo
     * @return a list of all statements reachable from the initial list of statements with expanded BNodes
     */
    private List<Statement> traverse(Model statements, List<Statement> newStatements,
                                     Predicate<IRI> continueRecursion, RepositoryConnection conn) {

        statements.forEach(statement -> {
            // Avoid infinite recursions.
            if (newStatements.contains(statement)) {
                return;
            }
            newStatements.add(statement);

            // If the object is a resource and not a literal...
            if (statement.getObject().isIRI() && continueRecursion.test((IRI) statement.getObject())) {
                // ... fetch all statements where this object is the subject ...
                Model bNodeStatements = QueryResults.asModel(conn.getStatements(
                        (Resource) statement.getObject(), null, null
                ));
                // ... and recursively apply this for all statements of the found statements
                traverse(bNodeStatements, newStatements, continueRecursion, conn);
            }
        });
        return newStatements;
    }

    /**
     * If the IRI of some business object is not referenced by any other object, then it is on root level.
     * As objects may be anonymous, they can be represented by a blank node with a corresponding id as well.
     *
     * @param model of the entire graph to analyse
     * @param id    the new {@link IRI} for the root statements
     * @return model with added ids to root statement
     */
    public Model addIdToRootStatements(Model model, IRI id) {

        return model.subjects().stream().map(subject -> {
            List<Statement> newStatements = new ArrayList<>();
            Iterable<Statement> subjectIsObjectStatements = model.getStatements(null, null, subject);
            Iterable<Statement> subjectStatements = model.getStatements(subject, null, null);
            if (subjectIsObjectStatements.iterator().hasNext()) {
                // subject is referenced => not root
                subjectStatements.forEach(newStatements::add);
            } else {
                // subject is not referenced => is root => add id
                subjectStatements.iterator()
                        .forEachRemaining(statement -> {
                            Statement newStatement = SimpleValueFactory.getInstance().createStatement(
                                    id,
                                    statement.getPredicate(),
                                    statement.getObject()
                            );
                            newStatements.add(newStatement);
                        });
            }
            return newStatements;
        }).flatMap(List::stream).collect(ModelCollector.toModel());
    }
}
