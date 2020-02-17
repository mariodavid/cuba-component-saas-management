package de.diedavids.cuba.ccsm.limit;

public interface Limitations {

    String NAME = "ccsm_Limitations";

    /**
     * checks if a limit has been reached for a given value
     *
     * @param limitCode the limit code to check against
     * @param value the value to check
     * @return true if limit has been reached, otherwise false
     */
    boolean reachedLimit(String limitCode, int value);

    /**
     * returns the limit value for a given limit code
     *
     * @param limitCode the limit code of the limit
     * @return the value of the limit
     */
    Integer getLimit(String limitCode);

    /**
     * checks if the limit is defined for this user session
     *
     * @param limitCode the limit code to check
     * @return true if a limit is available for this user session, otherwise false
     */
    boolean hasLimit(String limitCode);
}
