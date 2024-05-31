package io.andrelucas.toiletnearme.toilet.business;

public record Item(ItemId id, String description) {

    public static Item newItem(String description) {
        return new Item(ItemId.newId(), description);
    }
}
