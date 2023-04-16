package dev.inspector.agent.model;

import dev.inspector.agent.utility.JsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Date;

public class IError extends Context implements Transportable{

    private long timestamp =  Math.round(new Date().getTime() / 1000.0);
    private TransactionIdentifier transaction;
    private Throwable error;
    private boolean handled;

    public IError(Throwable error, TransactionIdentifier transaction){
        this.error = error;
        this.transaction = transaction;
    }

    public IError setHandled(boolean handled){
        this.handled = handled;
        return this;
    }

    @Override
    public JSONObject toTransport() {
        StackTraceElement[] stackTrace = error.getStackTrace();
        StackTraceElement trace = stackTrace[0];

        Integer line = trace.getLineNumber();
        String fileName = trace.getFileName();

        return new JsonBuilder()
                .put("model", "error")
                .put("timestamp", timestamp)
                .put("message", error.toString())
                .put("class", error.getClass())
                .put("file", fileName)
                .put("line", line)
                .put("handled", this.handled)
                .put("context", super.context)
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
