package scrapper.mavenproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Product {

	 @JsonIgnore
	private String name;
	private String desc;
	private String imagelink;
	private String price;
	private String rating;
	private String merchant;
	
	@Override
	public String toString() {
		return "Product [name=" + name + ", desc=" + desc + ", imagelink=" + imagelink + ", price=" + price
				+ ", rating=" + rating + ", merchant=" + merchant + "]";
	}
	
	public abstract static class ProductFormat {

        @JsonProperty("Product Name")
        abstract String getName();

        @JsonProperty("Description")
        abstract String getDescription();

        @JsonProperty("Image Link")
        abstract String getImageLink();

        @JsonProperty("Price")
        abstract String getPrice();

        @JsonProperty("Rating")
        abstract String getRating();

        @JsonProperty("Merchant Name")
        abstract String getMerchant();

        @JsonProperty("Product Link")
        abstract String getLink();
    }
	
	
}
