package logen.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A collection of entities from which one entity is returned on request.
 * Neither the number of entities nor the state of any of the entities is
 * guaranteed to be constant.
 * @param <E> The element type of the entities
 */
public class Pool<E> {
    private final List<E> entities;
    /**
     * The action to be performed immediately after an entity is randomly
     * chosen from the entities.
     */
    private final Consumer<E> action;
    /**
     * The check to be performed immediately before an entity is to be
     * removed from the entities.
     * If the check succeeds, the entity is removed. Else, the entity remains.
     */
    private final Predicate<E> removalCondition;

    /**
     * Constructs a pool from {@code entities}, {@code action} and
     * {@code removalCondition}.
     *
     * @param entities The entities from which any can be returned
     *                 on request
     * @param action The action to perform immediately after an entity
     *               is randomly chosen from {@code entities}
     * @param removalCondition The check to perform immediately before
     *                         an entity is to be removed from
     *                         {@code entities}
     * @throws NullPointerException if {@code action} or {@code removalCondition}
     *   is null
     */
    public Pool(
            List<E> entities,
            Consumer<E> action,
            Predicate<E> removalCondition
    ) {
        Validations.requireNonNull(action, removalCondition);

        this.entities = entities;
        this.action = action;
        this.removalCondition = removalCondition;
    }

    /**
     * Returns a randomly chosen entity after performing {@code action}
     * and {@code removalAction} sequentially.
     *
     * If the entities is null or have a size of 0, null is returned.
     *
     * @return A randomly chosen entity
     */
    public E get() {
        if (entities == null || entities.isEmpty()) {
            return null;
        }

        E entity = RandomUtil.chooseFrom(entities);
        action.accept(entity);
        if (removalCondition.test(entity)) {
            entities.remove(entity);
        }
        return entity;
    }
}
