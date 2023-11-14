package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("Mario", "Rossi", 0);
        this.bankAccount = new StrictBankAccount(mRossi, INITIAL_AMOUNT);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        //Assert that the AccountHolder got correctly initialized.
        assertEquals("Mario", this.mRossi.getName(), "Assert his name is Mario");
        assertEquals("Rossi", this.mRossi.getSurname(), "Assert surname is correct");
        assertEquals(0, this.mRossi.getUserID(), "Assert mRossi ID is 0");

        //Assert that the StrictBankAccount got correctly initialized.
        assertEquals(this.mRossi, this.bankAccount.getAccountHolder(), "Assert mRossi is the Account Holder");
        assertEquals(INITIAL_AMOUNT, this.bankAccount.getBalance(), "Assert his balance is INITIAL_AMOUNT");
        assertEquals(0.0, this.bankAccount.getTransactionsCount(), "Assert a new strict bank account starts with 0 transactions");
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        fail("To be implemented");
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        fail("To be implemented");
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        fail("To be implemented");
    }
}
