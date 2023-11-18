package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote{

    private final Map<String,Death> names;
    private String lastWritten;

    public DeathNoteImpl() {
        this.names = new HashMap<>();
    }

    @Override
    public String getRule(int ruleNumber) {
        if (ruleNumber < 1) {
            throw new IllegalArgumentException("Rule number must be >= 1");
        } else if (ruleNumber > RULES.size()) {
            throw new IndexOutOfBoundsException("Rule number must be < " + RULES.size());
        }
        return DeathNote.RULES.get(ruleNumber - 1);
    }

    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name, "Name must not be null");
        this.names.put(name, new Death());
        this.lastWritten = name; 
    }

    @Override
    public boolean writeDeathCause(final String cause) {
        if (this.lastWritten == null) {
            throw new IllegalStateException("Cannot write a death cause without writing a name first");
        }
        return getDeath(this.lastWritten).writeCause(cause);
    }

    @Override
    public boolean writeDetails(final String details) {
        if (this.lastWritten == null) {
            throw new IllegalStateException("Cannot write a death cause without writing a name first");
        }
        return getDeath(this.lastWritten).writeDetails(details);
    }

    @Override
    public String getDeathCause(final String name) {
        return getDeath(name).getCause();
    }

    @Override
    public String getDeathDetails(final String name) {
        return getDeath(name).getDetails();
    }

    @Override
    public boolean isNameWritten(String name) {
        return this.names.containsKey(name);
    }

    private Death getDeath(final String name) {
        return Objects.requireNonNull(this.names.get(Objects.requireNonNull(name, "This name has not been written in the death note before.")));
    }
    

    private static final class Death {

        private static String DEFAULT_CAUSE = "heart attack";
        private static String DEFAULT_DETAILS = "";
        private static long MAX_ELAPSED_TIME_DEATHCAUSE = 40;
        private static long MAX_ELAPSED_TIME_DEATHDETAILS = 6000 + MAX_ELAPSED_TIME_DEATHCAUSE;

        private String cause;
        private String details;
        private final long timeOfDeath;

        public Death(final String cause, final String details) {
            this.cause = cause;
            this.details = details;
            this.timeOfDeath = System.currentTimeMillis();
        }

        public Death() {
            this(DEFAULT_CAUSE, DEFAULT_DETAILS);
        }

        public String getCause() {
            return this.cause;
        }

        public String getDetails() {
            return this.details;
        }

        public long getTimeOfDeath() {
            return this.timeOfDeath;
        }

        public boolean writeCause(final String cause) {
            Objects.requireNonNull(cause);
            if (System.currentTimeMillis() - this.getTimeOfDeath() <= MAX_ELAPSED_TIME_DEATHCAUSE) {
                this.cause = cause;
                return true;
            }
            return false;
        }
        
        public boolean writeDetails(final String details) {
            Objects.requireNonNull(details);
            if (System.currentTimeMillis() - this.getTimeOfDeath() <= MAX_ELAPSED_TIME_DEATHDETAILS) {
                this.details = details;
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cause == null) ? 0 : cause.hashCode());
            result = prime * result + ((details == null) ? 0 : details.hashCode());
            result = prime * result + (int) (timeOfDeath ^ (timeOfDeath >>> 32));
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Death other = (Death) obj;
            return this.cause.equals(other.getCause()) && this.details.equals(other.getDetails()) && this.timeOfDeath == other.getTimeOfDeath();
        }
    }
}
