package utils;

public class TransactionStatistics {
    private double[] transactionAmounts;
    private int transactionCount;
    
    public TransactionStatistics(int maxTransactions) {
    	if (maxTransactions <= 0) {
            maxTransactions = 10;
        }
        transactionAmounts = new double[maxTransactions];
        transactionCount = 0;
    }
    
    public void setTransactionAmounts(double[] transactionAmounts) {
		this.transactionAmounts = transactionAmounts;
	}

	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}

	public double[] getTransactionAmounts() {
		return transactionAmounts;
	}

	public void addTransactionAmount(double amount) {
	    if (transactionCount >= transactionAmounts.length) {
	        int newSize = (transactionAmounts.length == 0) ? 10 : transactionAmounts.length * 2;
	        double[] newTransactionAmounts = new double[newSize];
	        System.arraycopy(transactionAmounts, 0, newTransactionAmounts, 0, transactionAmounts.length);
	        transactionAmounts = newTransactionAmounts;
	    }
	    transactionAmounts[transactionCount++] = amount;
	}

    
    public double getTotalAmount() {
        double total = 0;
        for (int i = 0; i < transactionCount; i++) {
            total += transactionAmounts[i];
        }
        return total;
    }
    
    public double getAverageAmount() {
        if (transactionCount == 0) return 0;
        return getTotalAmount() / transactionCount;
    }
    
    public double getMaxAmount() {
        if (transactionCount == 0) return 0;
        double max = transactionAmounts[0];
        for (int i = 1; i < transactionCount; i++) {
            if (transactionAmounts[i] > max) {
                max = transactionAmounts[i];
            }
        }
        return max;
    }
    
    public double getMinAmount() {
        if (transactionCount == 0) return 0;
        double min = transactionAmounts[0];
        for (int i = 1; i < transactionCount; i++) {
            if (transactionAmounts[i] < min) {
                min = transactionAmounts[i];
            }
        }
        return min;
    }
    
    public int getTransactionCount() {
        return transactionCount;
    }
    
}
