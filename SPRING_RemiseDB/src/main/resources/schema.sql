CREATE TABLE REMISE (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    montant_min DOUBLE NOT NULL,
    montant_max DOUBLE NOT NULL,
    taux        DOUBLE NOT NULL
);

CREATE TABLE TRANSACTION (
    id            BIGINT    PRIMARY KEY AUTO_INCREMENT,
    date          TIMESTAMP NOT NULL,
    montant_avant DOUBLE    NOT NULL,
    montant_apres DOUBLE    NOT NULL,
    remise_id     BIGINT    REFERENCES REMISE(id)
);