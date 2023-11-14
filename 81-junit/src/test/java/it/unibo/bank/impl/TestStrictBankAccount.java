package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        final int amount = 100;
        double expectedValue = 0;

        // Deposit 100 dollars and check it's all correct
        assertFalse(this.bankAccount.getTransactionsCount() > 0);
        this.bankAccount.deposit(this.mRossi.getUserID(), amount);
        assertEquals(amount + INITIAL_AMOUNT, this.bankAccount.getBalance(), "Assert that it correctly deposited " + amount + " euros");
        assertTrue(this.bankAccount.getTransactionsCount() > 0);

        // Calculate the expectedValue for my next assertion.
        expectedValue = StrictBankAccount.MANAGEMENT_FEE + this.bankAccount.getTransactionsCount() * StrictBankAccount.TRANSACTION_FEE;
        expectedValue = amount + INITIAL_AMOUNT - expectedValue;

        // Charge management fees and assert the balance is equal to the expected value and that it resetted the internal transactioncount.
        this.bankAccount.chargeManagementFees(this.mRossi.getUserID());
        assertEquals(expectedValue, this.bankAccount.getBalance());
        assertEquals(0, this.bankAccount.getTransactionsCount());

    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        try {
            this.bankAccount.withdraw(this.mRossi.getUserID(), -10000);
            fail("Withdraw of a negative amount didn't throw and exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot withdraw a negative amount", e.getMessage());
        }
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        try {
            this.bankAccount.deposit(this.mRossi.getUserID(), INITIAL_AMOUNT);
            this.bankAccount.withdraw(this.mRossi.getUserID(), INITIAL_AMOUNT * 10);
            fail("withdrawing more money than it is in the account didn't throw an exception");
        } catch (Exception e) {
            assertEquals("Insufficient balance", e.getMessage());
        }
    }
}
