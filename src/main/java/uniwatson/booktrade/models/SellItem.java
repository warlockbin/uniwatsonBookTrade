package uniwatson.booktrade.models;

public class SellItem {
    private String sellerId;
    private String isbn;
    private int price;
    private String bookName;
    private String author;

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
