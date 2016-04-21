package twice.repository.base.core;

import com.amazonaws.services.dynamodbv2.model.KeyType;

/**
 * Created by Gabriel on 13/04/2016.
 */
public enum FieldType {
    PRIMARY_KEY(KeyType.HASH),
    SECUNDARY_KEY(KeyType.RANGE),
    ATTRIBUTE(null);
    private KeyType type;

    FieldType(KeyType type) {
        this.type = type;
    }

    public KeyType getType() {
        return type;
    }
}
