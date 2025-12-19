package transactions;

/**
 * Target Interface: The interface the client (BankFacade) expects to use.
 */
public interface ExternalTransferGateway {

    /**
     * Executes a standardized wire transfer operation.
     * @param sourceAccountNum The account number to debit.
     * @param destinationAccountNum The account number to credit in the external system.
     * @param amount The amount to transfer.
     * @return true if the transfer was successfully processed.
     */
    boolean processStandardTransfer(String sourceAccountNum, String destinationAccountNum, double amount);
}