
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.gen.JavaInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lenz
 */
public class Action implements Comparable<Action> {

    public String path = "";
    public String description = "";
    public List<Operation> operations = new ArrayList<>();
    public String javaFile = "";
    public Apis api;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Operation o : operations) {
            sb.append(o.toJava());
        }
        return sb.toString();
    }

    void registerInterfaces(JavaInterface j, String apiVersion) {
        for (Operation o : operations) {
            String javaSignature = o.getSignature();
            String definition = o.getDefinition();
            j.iKnow(javaSignature, definition, o.getComment(), apiVersion);
        }
    }

    @Override
    public int compareTo(Action o) {
        return path.compareToIgnoreCase(o.path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(path, action.path) &&
                Objects.equals(description, action.description) &&
                Objects.equals(operations, action.operations) &&
                Objects.equals(javaFile, action.javaFile) &&
                Objects.equals(api, action.api);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, description, operations, javaFile, api);
    }

}

