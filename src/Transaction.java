public class Transaction {
    private String transactionCode;
    private String nis;
    private String bookCode;
    private String borrowDate;
    private String returnDate;
    private int status; // 0 = belum kembali, 1 = sudah kembali

    public Transaction(String transactionCode, String nis, String bookCode, String borrowDate, String returnDate, int status) {
        this.transactionCode = transactionCode;
        this.nis = nis;
        this.bookCode = bookCode;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toLine() {
        return transactionCode + "|" + nis + "|" + bookCode + "|" + borrowDate + "|" + returnDate + "|" + status;
    }

    public static Transaction fromLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) {
            return null;
        }

        int statusValue = 0;
        try {
            statusValue = Integer.parseInt(parts[5].trim());
        } catch (NumberFormatException e) {
            statusValue = 0;
        }

        return new Transaction(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim(),
                statusValue
        );
    }
}
