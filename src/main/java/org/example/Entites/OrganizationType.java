package org.example.Entites;

import java.io.Serial;
import java.io.Serializable;

public enum OrganizationType  implements Serializable {
    PUBLIC,
    TRUST,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;
    @Serial
    private static final long serialVersionUID = 1L;
}
