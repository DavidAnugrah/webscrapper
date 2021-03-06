package scrapper.mavenproject.scrapper;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import scrapper.mavenproject.model.Product;
import scrapper.mavenproject.scrapper.scrapper.Category;
import scrapper.mavenproject.webdriver.webdriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class scrapper {

	 private static final String BASE_URL = "https://www.tokopedia.com";
	    private static final String TOP_ADS_URL = "https://ta.tokopedia.com/promo";
	    private static final String HANDPHONE_PATH = "/p/handphone-tablet/handphone";
	    private static final String LAPTOP_PATH = "/p/komputer-laptop/laptop";
	    private static final String PAGE = "?page=";
	    private static final String CATEGORY_SEARCH =  BASE_URL + "/search?sc=%d";
	 

	    private static final String XPATH_PRODUCT_LIST = "//div[@data-testid='lstCL2ProductList']/div";
	    private static final String XPATH_PRODUCT_LINK = "a[@data-testid='lnkProductContainer']";
	    private static final String XPATH_FIRST_TIME_OVERLAY = "//div[@aria-label='unf-overlay']";
	    private static final String XPATH_PRODUCT_NAME = "//h1[@data-testid='lblPDPDetailProductName']";
	    private static final String XPATH_PRODUCT_DESCRIPTION = "//*[@data-testid='lblPDPDescriptionProduk']";
	    private static final String XPATH_PRODUCT_IMG_LINK = "//*[@data-testid='PDPImageMain']//img";
	    private static final String XPATH_PRODUCT_PRICE = "//*[@data-testid='lblPDPDetailProductPrice']";
	    private static final String XPATH_PRODUCT_RATING = "//*[@data-testid='lblPDPDetailProductRatingNumber']";
	    private static final String XPATH_MERCHANT_NAME = "//*[@data-testid='llbPDPFooterShopName']//h2";

	    private static final String DOM_FIRST_TIME_OVERLAY = "div[aria-label=unf-overlay]";

	    private static final String HREF = "href";
	    private static final String SRC = "src";
	    private static final String AMP = "&";
	    private static final String PARAM_R = "r=";
	    private static final String EMPTY = "";
	    private static final String DOT = ".";
	    private static final String RUPIAH_SIGN = "Rp";

	    @AllArgsConstructor
	    public enum Category {
	    	 HANDPHONE;

	        @Getter
	        int category;

	        public String getName() {
	            switch (category) {
	                case 24:
	                    return "Handphone";
	             
	                default:
	                    return EMPTY;
	            }
	        }
	    }

	    /**
	     * Extracting product data from Tokopedia's category section.
	     *
	     * @param category category of product
	     * @param count max number of product returned
	     * @return list containing product based on category
	     */
	    public List<Product> extractProductList(Category category, int count) {
	        final webdriver webDriver = new webdriver();
	        final List<Product> products = new ArrayList<>(count);
	        final String baseUrl = getUrl(category);

	        try {
	            List<String> tabs = webDriver.prepareTwoTabs();
	            for (int page = 1; products.size() < count; page++) {
	                String url = baseUrl + PAGE + page;
	                final List<WebElement> items = webDriver.getElementListByScrollingDown(url,
	                        XPATH_PRODUCT_LIST, tabs.get(0)); // switch to main tab

	                for (WebElement item : items) {
	                    String path = item.findElement(By.xpath(XPATH_PRODUCT_LINK)).getAttribute(HREF);
	                    if (isTopAdsLink(path)) {
	                        path = extractTopAdsLink(path);
	                    }

	                    webDriver.getWebpage(path, tabs.get(1)); //switch to new tab

	                    // removing overlay on first access
	                    if (products.isEmpty()) {
	                        webDriver.waitOnElement(XPATH_FIRST_TIME_OVERLAY);
	                        webDriver.removeElement(DOM_FIRST_TIME_OVERLAY);
	                    }
	                    // trigger lazy load
	                    webDriver.scrollDownSmall();
	                    webDriver.waitOnElement(XPATH_MERCHANT_NAME);

	                    products.add(extractProduct(webDriver, category, path));

	                    if (products.size() == count) {
	                        break;
	                    }
	                    webDriver.switchTab(tabs.get(0)); //switches to main tab
	                }

	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            webDriver.quit();
	        }

	        return products;
	    }

	    private Product extractProduct(webdriver webDriver, Category category, String path) {
	        String name = webDriver.getText(XPATH_PRODUCT_NAME);
	        String desc = webDriver.getText(XPATH_PRODUCT_DESCRIPTION);
	        String imageLink = webDriver.getText(XPATH_PRODUCT_IMG_LINK, SRC);
	        String price = webDriver.getText(XPATH_PRODUCT_PRICE)
	                .split(RUPIAH_SIGN)[1].replace(DOT, EMPTY);
	        String merchant = webDriver.getText(XPATH_MERCHANT_NAME);
	        String rating = webDriver.getText(XPATH_PRODUCT_RATING);

	        return Product.builder()
	                .type(category.getName())
	                .name(name)
	                .description(desc)
	                .imageLink(imageLink)
	                .merchant(merchant)
	                .price(Double.parseDouble(price))
	                .rating(rating == null || rating.isEmpty() ? 0.0 : Double.parseDouble(rating))
	                .link(path)
	                .build();
	    }

	    private String getUrl(Category category) {
	        String url = "";

	        switch (category) {
	            case HANDPHONE:
	                url = BASE_URL + HANDPHONE_PATH;
	                break;
	            default:
	                break;
	        }

	        return url;
	    }

	    private boolean isTopAdsLink(String path) {
	        return path.contains(TOP_ADS_URL);
	    }

	    private String extractTopAdsLink(String path) throws IOException {
	        return URLDecoder.decode(path.substring(path.indexOf(PARAM_R) + 2).split(AMP)[0],
	                StandardCharsets.UTF_8.name());
	    }
}
