/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.reflection;

import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.mapping.AttributeConverter;
import jakarta.nosql.Column;
import org.eclipse.jnosql.mapping.metadata.MappingType;
import org.eclipse.jnosql.mapping.test.entities.Animal;
import org.eclipse.jnosql.mapping.test.entities.Money;
import org.eclipse.jnosql.mapping.test.entities.MoneyConverter;
import org.eclipse.jnosql.mapping.test.entities.constructor.Computer;
import org.eclipse.jnosql.mapping.test.entities.constructor.PetOwner;
import org.eclipse.jnosql.mapping.test.entities.constructor.BookUser;
import org.eclipse.jnosql.mapping.test.entities.constructor.Smartphone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParameterMetaDataBuilderTest {

    @Test
    public void shouldConvertIdParameter() {
        Constructor<Computer> constructor = (Constructor<Computer>) Computer.class.getDeclaredConstructors()[0];
        ParameterMetaData id = ParameterMetaDataBuilder.of(constructor.getParameters()[0]);
        Assertions.assertNotNull(id);
        Assertions.assertTrue(id.isId());
        Assertions.assertEquals("_id", id.name());
        Assertions.assertEquals(Long.class, id.type());
        Assertions.assertEquals(MappingType.DEFAULT, id.paramType());
        Assertions.assertTrue(id.converter().isEmpty());
    }

    @Test
    public void shouldConvertDefaultParameterWithoutDefinedName() {
        Constructor<Smartphone> constructor = (Constructor<Smartphone>) Smartphone.class.getDeclaredConstructors()[0];
        ParameterMetaData name = ParameterMetaDataBuilder.of(constructor.getParameters()[1]);
        Assertions.assertNotNull(name);
        Assertions.assertFalse(name.isId());
        Assertions.assertEquals("owner", name.name());
        Assertions.assertEquals(String.class, name.type());
        Assertions.assertEquals(MappingType.DEFAULT, name.paramType());
        Assertions.assertTrue(name.converter().isEmpty());
    }

    @Test
    public void shouldConvertDefaultParameter() {
        Constructor<Computer> constructor = (Constructor<Computer>) Computer.class.getDeclaredConstructors()[0];
        ParameterMetaData name = ParameterMetaDataBuilder.of(constructor.getParameters()[1]);
        Assertions.assertNotNull(name);
        Assertions.assertFalse(name.isId());
        Assertions.assertEquals("name", name.name());
        Assertions.assertEquals(String.class, name.type());
        Assertions.assertEquals(MappingType.DEFAULT, name.paramType());
        Assertions.assertTrue(name.converter().isEmpty());
    }

    @Test
    public void shouldConvertConverterParameter() {
        Constructor<Computer> constructor = (Constructor<Computer>) Computer.class.getDeclaredConstructors()[0];
        ParameterMetaData price = ParameterMetaDataBuilder.of(constructor.getParameters()[4]);
        Assertions.assertNotNull(price);
        Assertions.assertFalse(price.isId());
        Assertions.assertEquals("price", price.name());
        Assertions.assertEquals(Money.class, price.type());
        Assertions.assertEquals(MappingType.DEFAULT, price.paramType());
        Assertions.assertFalse(price.converter().isEmpty());
        Class<? extends AttributeConverter<Object, Object>> converter = price.converter().orElseThrow();
        assertEquals(MoneyConverter.class, converter);
    }
    //parameter wit collection
    //parameter with EMBEDDED
    //parameter with map

    @Test
    public void shouldConvertEntityParameter() {
        Constructor<PetOwner> constructor = (Constructor<PetOwner>) PetOwner.class.getDeclaredConstructors()[0];
        ParameterMetaData animal = ParameterMetaDataBuilder.of(constructor.getParameters()[2]);
        Assertions.assertNotNull(animal);
        Assertions.assertFalse(animal.isId());
        Assertions.assertEquals("animal", animal.name());
        Assertions.assertEquals(Animal.class, animal.type());
        Assertions.assertEquals(MappingType.ENTITY, animal.paramType());
        Assertions.assertTrue(animal.converter().isEmpty());
    }

    @Test
    public void shouldConvertCollectionParameter() {
        Constructor<BookUser> constructor = (Constructor<BookUser>) BookUser.class.getDeclaredConstructors()[0];
        ParameterMetaData books = ParameterMetaDataBuilder.of(constructor.getParameters()[2]);
        Assertions.assertNotNull(books);
        Assertions.assertFalse(books.isId());
        Assertions.assertEquals("books", books.name());
        Assertions.assertEquals(List.class, books.type());
        Assertions.assertEquals(MappingType.COLLECTION, books.paramType());
        Assertions.assertTrue(books.converter().isEmpty());
        assertEquals(GenericParameterMetaData.class, books.getClass());
        GenericParameterMetaData generic = (GenericParameterMetaData) books;
        TypeSupplier<?> typeSupplier = generic.getTypeSupplier();
        Assertions.assertNotNull(typeSupplier);
    }

    @Test
    public void shouldConvertMapParameter() {
        Constructor<Foo> constructor = (Constructor<Foo>) Foo.class.getDeclaredConstructors()[0];
        ParameterMetaData map = ParameterMetaDataBuilder.of(constructor.getParameters()[0]);
        Assertions.assertNotNull(map);
        Assertions.assertFalse(map.isId());
        Assertions.assertEquals("map", map.name());
        Assertions.assertEquals(Map.class, map.type());
        Assertions.assertEquals(MappingType.MAP, map.paramType());
        Assertions.assertTrue(map.converter().isEmpty());
        assertEquals(GenericParameterMetaData.class, map.getClass());
        GenericParameterMetaData generic = (GenericParameterMetaData) map;
        TypeSupplier<?> typeSupplier = generic.getTypeSupplier();
        Assertions.assertNotNull(typeSupplier);
    }

    static class Foo{
        private Map<String, String> map;

        public Foo(@Column("map") Map<String, String> map) {
            this.map = map;
        }
    }


}