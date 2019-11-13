package au.com.booker.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link au.com.booker.domain.BInventory} entity.
 */
public class BInventoryDTO implements Serializable {

    private String id;

    @NotNull
    @Size(min = 4, max = 200)
    private String name;

    @NotNull
    private String category;

    @NotNull
    private Double purchasePrice;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double sellingPrice;

    private String notes;

    private Double discounts;

    @NotNull
    private Integer unitsInStore;

    private LocalDate lastDateOfPurchase;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Double discounts) {
        this.discounts = discounts;
    }

    public Integer getUnitsInStore() {
        return unitsInStore;
    }

    public void setUnitsInStore(Integer unitsInStore) {
        this.unitsInStore = unitsInStore;
    }

    public LocalDate getLastDateOfPurchase() {
        return lastDateOfPurchase;
    }

    public void setLastDateOfPurchase(LocalDate lastDateOfPurchase) {
        this.lastDateOfPurchase = lastDateOfPurchase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BInventoryDTO bInventoryDTO = (BInventoryDTO) o;
        if (bInventoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bInventoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BInventoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", category='" + getCategory() + "'" +
            ", purchasePrice=" + getPurchasePrice() +
            ", sellingPrice=" + getSellingPrice() +
            ", notes='" + getNotes() + "'" +
            ", discounts=" + getDiscounts() +
            ", unitsInStore=" + getUnitsInStore() +
            ", lastDateOfPurchase='" + getLastDateOfPurchase() + "'" +
            "}";
    }
}
