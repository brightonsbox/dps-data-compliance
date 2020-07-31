DROP TABLE IF EXISTS RETENTION_CHECK;

CREATE TABLE RETENTION_CHECK
(
  RETENTION_CHECK_ID     BIGSERIAL       NOT NULL,
  RESOLUTION_ID          BIGINT          NOT NULL,
  CHECK_TYPE             VARCHAR(255)    NOT NULL,
  CHECK_STATUS           VARCHAR(255)    NOT NULL,

  CONSTRAINT RET_CHECK_PK       PRIMARY KEY (RETENTION_CHECK_ID),
  CONSTRAINT RET_CHECK_RES_FK   FOREIGN KEY (RESOLUTION_ID) REFERENCES REFERRAL_RESOLUTION(RESOLUTION_ID)
);

COMMENT ON TABLE RETENTION_CHECK IS 'Represents the reason(s) why an offender record should be retained';

COMMENT ON COLUMN RETENTION_CHECK.RETENTION_CHECK_ID IS 'Primary key id';
COMMENT ON COLUMN RETENTION_CHECK.RESOLUTION_ID IS 'The id of the referral resolution';
COMMENT ON COLUMN RETENTION_CHECK.CHECK_TYPE IS 'An enumerated retention check type';
COMMENT ON COLUMN RETENTION_CHECK.CHECK_STATUS IS 'An enumerated status for the retention check';

CREATE INDEX RET_CHECK_RI_IDX ON RETENTION_CHECK(RESOLUTION_ID);