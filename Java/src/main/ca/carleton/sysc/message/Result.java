package main.ca.carleton.sysc.message;

import main.ca.carleton.sysc.types.ResultType;

/**
 * Result bean used to create a return message back to the user
 */
public class Result {

    private ResultType resultType;

    private String description;

    public Result(ResultType resultType, String description) {
        this.resultType = resultType;
        this.description = description;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
