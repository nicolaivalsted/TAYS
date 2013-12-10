package dk.yousee.randy.logging;

/**
 * Items to search for in method, class, annnotations and arguments
 *
 * @author jablo
 */
public class ContextLoggingSearchItem {
    private String key;
    private String[] searchKeys;
    private ValueFormatter format = new DefaultFormatter();

    public ContextLoggingSearchItem() {
    }

    public ContextLoggingSearchItem(String key, String[] searchKeys) {
        this.key = key;
        this.searchKeys = searchKeys;
    }

    public ContextLoggingSearchItem(String key, String[] searchKeys, ValueFormatter format) {
        this.key = key;
        this.searchKeys = searchKeys;
        this.format = format;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String itemKey) {
        this.key = itemKey;
    }

    public String[] getSearchKeys() {
        return searchKeys;
    }

    public void setSearchKeys(String[] searchItems) {
        this.searchKeys = searchItems;
    }

    public ValueFormatter getFormat() {
        return format;
    }

    public void setFormat(ValueFormatter format) {
        this.format = format;
    }
}
