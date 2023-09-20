import com.fasterxml.jackson.annotation.JsonProperty;

class Response {
    @JsonProperty("response")
    boolean response;
    @JsonProperty("message")
    String message;
    @JsonProperty("data")
    Object[] data;
    @JsonProperty("metadata")
    Object metadata;
    @JsonProperty("subMetadata")
    Object subMetaData;

    @Override
    public String toString() {
        return "[RESPONSE] response=" + response + ", message=" + message + ", available=" + (data.length!=0);
    }
}