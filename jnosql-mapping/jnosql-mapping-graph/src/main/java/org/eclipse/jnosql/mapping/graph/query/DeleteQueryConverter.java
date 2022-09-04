/*
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.graph.query;

import jakarta.nosql.query.DeleteQuery;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.query.method.DeleteMethodProvider;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;

import java.util.List;
import java.util.function.Function;

final class DeleteQueryConverter extends AbstractQueryConvert implements Function<GraphQueryMethod, List<Vertex>> {

    private static final DeleteQueryConverter INSTANCE = new DeleteQueryConverter();

    private DeleteQueryConverter() {
    }

    @Override
    public List<Vertex> apply(GraphQueryMethod graphQuery) {
        DeleteMethodProvider provider = DeleteMethodProvider.get();
        DeleteQuery deleteQuery = provider.apply(graphQuery.getMethod(), graphQuery.getEntityName());
        EntityMetadata mapping = graphQuery.getMapping();
        GraphTraversal<Vertex, Vertex> traversal = getGraphTraversal(graphQuery, deleteQuery::getWhere, mapping);
        traversal.hasLabel(mapping.getName());
        return traversal.toList();
    }
}
