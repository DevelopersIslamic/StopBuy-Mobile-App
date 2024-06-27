package mohaben.com.stopbuy;

public class Product {
    private String name;
    private String barcode;
    private boolean isBoycotted;

    public Product(String name, String barcode, boolean isBoycotted) {
        this.name = name;
        this.barcode = barcode;
        this.isBoycotted = isBoycotted;
    }

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }

    public boolean isBoycotted() {
        return isBoycotted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setBoycotted(boolean boycotted) {
        isBoycotted = boycotted;
    }
}