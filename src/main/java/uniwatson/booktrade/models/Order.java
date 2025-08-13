package uniwatson.booktrade.models;

import java.sql.Timestamp;

public class Order {
    private int id;
    private String sellerId;
    private String buyerId;
    private String book;              // ISBN
    private Timestamp establishDate;
    private int status;               // 1=處理中, 2=已完成…（依你定義）
    private String phoneNum;
    private Timestamp finishDate;

    // === 新增：成交金額（若資料庫沒有 Price 欄位，預設為 0） ===
    private int price;

    // --- getters / setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }

    public String getBook() { return book; }
    public void setBook(String book) { this.book = book; }

    public Timestamp getEstablishDate() { return establishDate; }
    public void setEstablishDate(Timestamp establishDate) { this.establishDate = establishDate; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getPhoneNum() { return phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    public Timestamp getFinishDate() { return finishDate; }
    public void setFinishDate(Timestamp finishDate) { this.finishDate = finishDate; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}
