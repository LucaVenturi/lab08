package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

class TestDeathNote {

    private DeathNote deathNote;

    @BeforeEach
    public void setUp() {
        this.deathNote = new DeathNoteImpl();
    }

    @Test
    public void testRulesNumbers() {
        try {
            this.deathNote.getRule(0);
            fail("Didn't throw an exception when asked the rule number 0");
        } catch (Exception e) {
            assertEquals("Rule number must be >= 1", e.getMessage());
        }

        try {
            this.deathNote.getRule(-1);
            fail("Didn't throw an exception when asked a negative rule");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Rule number must be >= 1", e.getMessage());
        }
    }

    @Test
    public void TestNoEmptyRule() {
        for (String rule : DeathNote.RULES) {
            assertNotNull(rule);
            assertTrue(rule != "");
        }
    }

    @Test void TestHumanWillDie() {
        final String human1 = "Mario Giordano";
        final String human2 = "Cristiano Ronaldo";
        assertFalse(this.deathNote.isNameWritten(human1));
        this.deathNote.writeName(human1);
        assertTrue(this.deathNote.isNameWritten(human1));
        assertFalse(this.deathNote.isNameWritten(human2));
        assertFalse(this.deathNote.isNameWritten(""));
    }

    @Test
    public void TestTimeLimitForDeathCause() {
        final String human1 = "Mario Giordano";
        final String human2 = "Virginio Scotti";
        final String deathCause = "karting accident";

        try {
            this.deathNote.writeDeathCause(deathCause);
            fail("Writing a Death Cause without writing a name first " +
                "didn't throw and exception");
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
            assertEquals("Cannot write a death cause without writing a name first", e.getMessage());
        }
        this.deathNote.writeName(human1);
        assertEquals("heart attack", this.deathNote.getDeathCause(human1));
        this.deathNote.writeName(human2);
        assertTrue(this.deathNote.writeDeathCause(deathCause));
        assertEquals(deathCause, this.deathNote.getDeathCause(human2));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(this.deathNote.writeDeathCause(deathCause + " modified."));
        assertEquals(deathCause, this.deathNote.getDeathCause(human2));
    }

    @Test
    public void testTimeLimitForDeathDetails() {
        final String human1 = "Mario Giordano";
        final String human2 = "Will Smith";
        final String details = "ran for too long";
        // try {
        //     this.deathNote.writeDetails(details);
        //     fail("Writing death details before writing the name didn't cause an exception.");
        // } catch (Exception e) {
        //     assertEquals(IllegalStateException.class, e.getClass());
        //     assertEquals("Cannot Write death details without writing a name first", e.getMessage());
        // }
        assertThrows(IllegalStateException.class, () -> deathNote.writeDetails(details));
        this.deathNote.writeName(human1);
        assertEquals("", this.deathNote.getDeathDetails(human1));
        assertTrue(this.deathNote.writeDetails(details));
        assertEquals(details, this.deathNote.getDeathDetails(human1));
        this.deathNote.writeName(human2);
        try {
            Thread.sleep(6100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(this.deathNote.writeDeathCause(details + " modified."));
        assertEquals("", this.deathNote.getDeathDetails(human2));
    }
}