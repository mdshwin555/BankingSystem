package transactions;

public class LegacySystemAdapter implements ExternalTransferGateway {

    // Reference to the incompatible external system (Adaptee)
    private LegacyWireTransferAPI legacyAPI;

    public LegacySystemAdapter(LegacyWireTransferAPI legacyAPI) {
        this.legacyAPI = legacyAPI;
    }

    /**
     * This method implements the standard interface (Target)
     * but internally translates the call to the incompatible legacy method (Adaptee).
     */
    @Override
    public boolean processStandardTransfer(String sourceAccountNum, String destinationAccountNum, double amount) {
        System.out.println("\n[Adapter] Translating standard transfer request for legacy system...");

        // 1. Translation: Map standard parameters to legacy parameters
        String legacySourceId = "ID-" + sourceAccountNum; // Custom mapping logic
        String legacyDestId = "ID-" + destinationAccountNum; // Custom mapping logic

        // 2. Delegation: Call the incompatible method on the Adaptee
        boolean success = legacyAPI.executeLegacyTransfer(legacySourceId, legacyDestId, amount);

        if (success) {
            System.out.println("[Adapter] Transfer successfully executed via Legacy API.");
        } else {
            System.out.println("[Adapter] Failed to execute transfer via Legacy API.");
        }
        return success;
    }
}
