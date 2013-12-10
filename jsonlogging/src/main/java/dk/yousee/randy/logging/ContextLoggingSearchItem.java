package dk.yousee.randy.logging;

/**
 * Items to search for in method, class, annnotations and arguments
 *
 * @author jablo
 */
public class ContextLoggingSearchItem {
    private String itemKey;
    private String[] searchItems;
    private ValueFormatter format = new DefaultFormatter();

    public ContextLoggingSearchItem() {
    }

    public ContextLoggingSearchItem(String itemKey, String[] searchItems) {
        this.itemKey = itemKey;
        this.searchItems = searchItems;
    }

    public ContextLoggingSearchItem(String itemKey, String[] searchItems, ValueFormatter format) {
        this.itemKey = itemKey;
        this.searchItems = searchItems;
        this.format = format;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String[] getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(String[] searchItems) {
        this.searchItems = searchItems;
    }

    public ValueFormatter getFormat() {
        return format;
    }

    public void setFormat(ValueFormatter format) {
        this.format = format;
    }
}
