package twice.domain;

import twice.domain.base.TwicePojo;

import java.util.UUID;

public class TwiceUser extends TwicePojo {

    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
