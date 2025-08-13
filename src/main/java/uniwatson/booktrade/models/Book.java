package uniwatson.booktrade.models;


import java.math.BigDecimal;

public class Book {
    private String isbn;
    private String coverImg;
    private String name;
    private String author;
    private int publishYear;
    private BigDecimal originalPrice;
    private String category;
    private String description;
    private String imageUrl;
    
    public Book() {}
    
    public Book(String isbn, String coverImg, String name, String author, int publishYear, 
                BigDecimal originalPrice, String category, String description, String imageUrl) {
        this.isbn = isbn;
        this.coverImg = coverImg;
        this.name = name;
        this.author = author;
        this.publishYear = publishYear;
        this.originalPrice = originalPrice;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
    }
    
    // Getters and Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getCoverImg() { return coverImg; }
    public void setCoverImg(String coverImg) { this.coverImg = coverImg; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getPublishYear() { return publishYear; }
    public void setPublishYear(int publishYear) { this.publishYear = publishYear; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}