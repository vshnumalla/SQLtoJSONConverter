package converter.sql2json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "primaryType",
        "mixinTypes",
        "properties",
        "nodes"
})
public class Node {

    @JsonProperty("name")
    private String name;
    @JsonProperty("primaryType")
    private String primaryType;
    @JsonProperty("mixinTypes")
    private List<Object> mixinTypes = null;
    @JsonProperty("properties")
    private List<Property_> properties = null;
    @JsonProperty("nodes")
    private List<Object> nodes = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("primaryType")
    public String getPrimaryType() {
        return primaryType;
    }

    @JsonProperty("primaryType")
    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    @JsonProperty("mixinTypes")
    public List<Object> getMixinTypes() {
        return mixinTypes;
    }

    @JsonProperty("mixinTypes")
    public void setMixinTypes(List<Object> mixinTypes) {
        this.mixinTypes = mixinTypes;
    }

    @JsonProperty("properties")
    public List<Property_> getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(List<Property_> properties) {
        this.properties = properties;
    }

    @JsonProperty("nodes")
    public List<Object> getNodes() {
        return nodes;
    }

    @JsonProperty("nodes")
    public void setNodes(List<Object> nodes) {
        this.nodes = nodes;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}