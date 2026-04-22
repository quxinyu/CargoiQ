package com.efreight.base.module.one.record.neone.model.onerecord;

import com.efreight.base.module.one.record.neone.iata.onerecord.ACL;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.rdf4j.model.IRI;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessDelegation implements Referencable {

    private IRI iri;
    private Optional<String> description;
    private Set<Permission> permissions;
    private Set<IRI> isRequestedFor;
    private Set<IRI> hasLogisticsObject;
    private Optional<Boolean> notifyRequestStatusChange;
    @Override
    public IRI iri() {
        return iri;
    }
    public enum Permission {
        GET_LOGISTICS_EVENT(API.GET_LOGISTICS_EVENT, ACL.Read),
        GET_LOGISTICS_OBJECT(API.GET_LOGISTICS_OBJECT, ACL.Read),
        PATCH_LOGISTICS_OBJECT(API.PATCH_LOGISTICS_OBJECT, ACL.Read), // "Write" not required.
        POST_LOGISTICS_EVENT(API.POST_LOGISTICS_EVENT, ACL.Write);

        private static final Map<IRI, AccessDelegation.Permission> reverseLookup = new HashMap<>();

        private final IRI iri;

        private final IRI mode;

        Permission(IRI iri, IRI mode) {
            this.iri = iri;
            this.mode = mode;
        }

        public IRI iri() {
            return iri;
        }

        public IRI mode() {
            return mode;
        }

        public static AccessDelegation.Permission from(IRI value) {
            AccessDelegation.Permission state = reverseLookup.get(value);
            if (state == null) {
                throw new NoSuchElementException(value.stringValue());
            }

            return state;
        }

        static {
            for (AccessDelegation.Permission p : AccessDelegation.Permission.values()) {
                reverseLookup.put(p.iri(), p);
            }
        }
    }
}
