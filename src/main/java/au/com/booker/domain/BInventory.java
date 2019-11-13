package au.com.booker.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A BInventory.
 */
@Document(collection = "b_inventory")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "binventory")
public class BInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Size(min = 4, max = 200)
    @Field("name")
    private String name;

    @NotNull
    @Field("category")
    private String category;

    @NotNull
    @Field("purchase_price")
    private Double purchasePrice;

    @NotNull
    @DecimalMin(value = "0.0")
    @Field("selling_price")
    private Double sellingPrice;

    @Field("notes")
    private String notes;

    @Field("discounts")
    private Double discounts;

    @NotNull
    @Field("units_in_store")
    private Integer unitsInStore;

    @Field("last_date_of_purchase")
    private LocalDate lastDateOfPurchase;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BInventory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public BInventory category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public BInventory purchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
        return this;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public BInventory sellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getNotes() {
        return notes;
    }

    public BInventory notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getDiscounts() {
        return discounts;
    }

    public BInventory discounts(Double discounts) {
        this.discounts = discounts;
        return this;
    }

    public void setDiscounts(Double discounts) {
        this.discounts = discounts;
    }

    public Integer getUnitsInStore() {
        return unitsInStore;
    }

    public BInventory unitsInStore(Integer unitsInStore) {
        this.unitsInStore = unitsInStore;
        return this;
    }

    public void setUnitsInStore(Integer unitsInStore) {
        this.unitsInStore = unitsInStore;
    }

    public LocalDate getLastDateOfPurchase() {
        return lastDateOfPurchase;
    }

    public BInventory lastDateOfPurchase(LocalDate lastDateOfPurchase) {
        this.lastDateOfPurchase = lastDateOfPurchase;
        return this;
    }

    public void setLastDateOfPurchase(LocalDate lastDateOfPurchase) {
        this.lastDateOfPurchase = lastDateOfPurchase;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BInventory)) {
            return false;
        }
        return id != null && id.equals(((BInventory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BInventory{" +
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
