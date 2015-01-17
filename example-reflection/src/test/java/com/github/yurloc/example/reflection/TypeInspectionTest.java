package com.github.yurloc.example.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.junit.Test;

/**
 * Shows the sequence of obtaining type parameters of a generic private field using reflection.
 */
public class TypeInspectionTest {

    private Map<String, Integer> map;

    @Test
    public void testSomeMethod() throws NoSuchFieldException {
        // no public accessible fields
        assertThat(this.getClass().getFields()).isEmpty();
        // private field can be obtained by getDeclaredFields()
        assertThat(this.getClass().getDeclaredFields()).hasSize(1);
        assertThat(this.getClass().getDeclaredFields()).extracting("name").contains("map");

        Field field = this.getClass().getDeclaredField("map");
        Class<?> type = field.getType();
        assertThat(type.getName()).isEqualTo("java.util.Map");

        // examine type parameters
        TypeVariable<?>[] p = type.getTypeParameters();
        assertThat(p).hasSize(2);
        assertThat(p).extracting("name").contains("K", "V");

        assertThat(field.getGenericType()).isInstanceOf(ParameterizedType.class);
        ParameterizedType pt = (ParameterizedType) field.getGenericType();
        System.out.println(pt);
        Type[] a = pt.getActualTypeArguments();
        assertThat(a).hasSize(2);
        assertThat(a).isEqualTo(new Class<?>[]{String.class, Integer.class});
    }
}
