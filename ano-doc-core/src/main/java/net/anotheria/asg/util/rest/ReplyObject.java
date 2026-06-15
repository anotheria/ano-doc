package net.anotheria.asg.util.rest;

import java.util.HashMap;

/**
 * Wrapper for all REST API responses. Every resource method returns this object.
 * Clients check {@link #isSuccess()} first, then read from {@link #getResults()} on success
 * or {@link #getMessage()} / {@link #getErrorKey()} on failure.
 */
public class ReplyObject {

    private boolean success;
    private String message;
    private ErrorKey errorKey;
    private HashMap<String, Object> results = new HashMap<>();

    public ReplyObject() {}

    public ReplyObject(String name, Object result) {
        results.put(name, result);
    }

    public void addResult(String name, Object result) {
        results.put(name, result);
    }

    public static ReplyObject success(String name, Object result) {
        ReplyObject ret = new ReplyObject(name, result);
        ret.success = true;
        return ret;
    }

    public static ReplyObject success() {
        ReplyObject ret = new ReplyObject();
        ret.success = true;
        return ret;
    }

    public static ReplyObject error(String message) {
        ReplyObject ret = new ReplyObject();
        ret.success = false;
        ret.message = message;
        return ret;
    }

    public static ReplyObject error(Throwable cause) {
        ReplyObject ret = new ReplyObject();
        ret.success = false;
        ret.message = cause.getClass().getSimpleName() + ": " + cause.getMessage();
        return ret;
    }

    public static ReplyObject error(ErrorKey errorKey) {
        return error(errorKey, null);
    }

    public static ReplyObject error(ErrorKey errorKey, String message) {
        ReplyObject ret = new ReplyObject();
        ret.success = false;
        ret.errorKey = errorKey;
        ret.message = message;
        return ret;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public ErrorKey getErrorKey() { return errorKey; }
    public void setErrorKey(ErrorKey errorKey) { this.errorKey = errorKey; }

    public HashMap<String, Object> getResults() { return results; }
    public void setResults(HashMap<String, Object> results) { this.results = results; }

    @Override
    public String toString() {
        return "ReplyObject{success=" + success + ", message=" + message + ", results=" + results + "}";
    }
}
