package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class Error extends Context implements Transportable  {

    private long timestamp = new Date().getTime();
    private TransactionIdentifier transaction;
    private Throwable error;
    private boolean handled;


    public Error(Throwable error, TransactionIdentifier transaction){
        this.error = error;
        this.transaction = transaction;
    }

    public Error setHandled(boolean handled){
        this.handled = handled;
        return this;
    }


    @Override
    public JSONObject toTransport() {

        //TODO: What is the error code?

        return new JsonBuilder()
                .put("model", "error")
                .put("timestamp", timestamp)
                .put("message", error.getMessage())
                .put("class", error.getClass())
                .put("file", "file_test")
                .put("line", 12)
                .put("handled", false)
                .put("stack", stackTraceToJson())
                .put("transaction", transaction.toObject())
                .build();
    }

    private JSONArray stackTraceToJson(){
        JSONArray elements = new JSONArray();
        StackTraceElement[] stackTrace = error.getStackTrace();

        for(int i = 0; i < stackTrace.length; i++ ){
            StackTraceElement traceElement = stackTrace[i];
            elements.put(new JsonBuilder()
                    .put("class", traceElement.getClass())
                    .put("file", traceElement.getFileName())
                    .put("line", traceElement.getLineNumber())
                    .build());
        }
        return elements;
    }
}
