package logen.experimental.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Pool<E> {
    private final List<E> entities;
    private final Consumer<E> removalAction;
    private final Predicate<E> removalCondition;

    public Pool(
            List<E> entities,
            Consumer<E> removalAction,
            Predicate<E> removalCondition
    ) {
        this.entities = entities;
        this.removalAction = removalAction;
        this.removalCondition = removalCondition;
    }

    public E get() {
        E entity = RandomChooser.chooseFrom(entities);
        removalAction.accept(entity);
        if (removalCondition.test(entity)) {
            entities.remove(entity);
        }
        return entity;
    }
}
