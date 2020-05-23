package logen.experimental.generation.fluid;

import logen.experimental.util.RandomChooser;

import java.util.List;

public class Pool<E> {
    private final List<E> entities;

    public Pool(List<E> entities) {
        this.entities = entities;
    }

    public E get() {
        return RandomChooser.chooseFrom(entities);
    }
}
