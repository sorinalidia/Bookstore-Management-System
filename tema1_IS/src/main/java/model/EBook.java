package model;

public class EBook extends PhysicalBook {

    private String format;
    @Override
    public void setId(Long id){
        super.setId(id);
    }
    @Override
    public Long getId(){
        return super.getId();
    }
    public String getFormat() {
        return format;
    }

    public Book setFormat(String format) {
        this.format = format;
        return this;
    }

    @Override
    public String toString() {
        return String.format("EBook author: %s | title: %s | Format: %s | Published Date: %s.", getAuthor(), getTitle(), format, getPublishedDate());
    }

    public EBook getBook() {
        return this;
    }


}
