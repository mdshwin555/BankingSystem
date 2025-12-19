package transactions;

/**
 * Adaptee: The external, incompatible class/API.
 * It uses different method names and parameter types (e.g., uses long for amount ID).
 */
public class LegacyWireTransferAPI {

    // The method signature is incompatible with the Target interface (processStandardTransfer)
    public boolean executeLegacyTransfer(String sourceId, String destId, double transactionValue) {
        if (transactionValue > 0) {
            // Simulate complex legacy processing...
            System.out.println("📠 [Legacy API] Starting complex transfer from ID: " + sourceId);
            System.out.println("📠 [Legacy API] Transaction value: " + transactionValue);
            // Assume success if amount is positive
            System.out.println("📠 [Legacy API] Legacy transfer completed successfully.");
            return true;
        }
        System.out.println("📠 [Legacy API] Legacy transfer failed (invalid value).");
        return false;
    }
}