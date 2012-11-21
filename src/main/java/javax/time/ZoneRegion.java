/*
 * Copyright (c) 2007-2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import javax.time.zone.ZoneRules;
import javax.time.zone.ZoneRulesProvider;

/**
 * A geographical region where the same time-zone rules apply.
 * <p>
 * Time-zone information is categorized as a set of rules defining when and
 * how the offset from UTC/Greenwich changes. These rules are accessed using
 * identifiers based on geographical regions, such as countries or states.
 * The most common region classification is the Time Zone Database (TZDB),
 * which defines regions such as 'Europe/Paris' and 'Asia/Tokyo'.
 * <p>
 * The region identifier, modeled by this class, is distinct from the
 * underlying rules, modeled by {@link ZoneRules}.
 * The rules are defined by governments and change frequently.
 * By contrast, the region identifier is well-defined and long-lived.
 * This separation also allows rules to be shared between regions if appropriate.
 *
 * <h4>Implementation notes</h4>
 * This class is immutable and thread-safe.
 */
public final class ZoneRegion extends ZoneId implements Serializable {

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 8386373296231747096L;

    /**
     * The time-zone ID, not null. */
    private final String id;
    /**
     * The time-zone group provider, null if zone ID is unchecked.
     */
    private final transient ZoneRulesProvider provider;

    /**
     * Obtains an instance of {@code ZoneRegion} from an identifier without checking
     * if the time-zone has available rules.
     * <p>
     * This method parses the ID and applies any appropriate normalization.
     * Unlike {@link #of(String)}, it does not validate the ID against the known
     * set of IDsfor which rules are available.
     * <p>
     * This method is intended for advanced use cases.
     * For example, consider a system that always retrieves time-zone rules from a remote server.
     * Using this factory would allow a {@code ZoneRegion}, and thus a {@code ZonedDateTime},
     * to be created without loading the rules from the remote server.
     *
     * @param zoneId  the time-zone ID, not null
     * @return the zone ID, not null
     * @throws TimeZoneException if the ID is malformed
     */
    public static ZoneRegion ofUnchecked(String zoneId) {
        return ofId(zoneId, false);  // TODO: move to ZoneRegion?
    }

    //-------------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param id  the time-zone ID, not null
     * @param provider  the provider, null if zone is unchecked
     */
    ZoneRegion(String id, ZoneRulesProvider provider) {
        this.id = id;
        this.provider = provider;
    }

    //-----------------------------------------------------------------------
    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isValid() {
        return getProvider().isValid(id, null);
    }

    @Override
    public ZoneRules getRules() {
        return getProvider().getRules(id, null);
    }

    private ZoneRulesProvider getProvider() {
        // additional query for group provider when null allows for possibility
        // that the provider was added after the ZoneId was created
        return (provider != null ? provider : ZoneRulesProvider.getProvider(id));
    }

    //-----------------------------------------------------------------------
    void writeExternal(DataOutput out) throws IOException {
        out.writeUTF(getId());
    }

    static ZoneId readExternal(DataInput in) throws IOException {
        String id = in.readUTF();
        return ofUnchecked(id);
    }

    private Object writeReplace() {
        return new Ser(Ser.ZONE_REGION_TYPE, this);
    }

}