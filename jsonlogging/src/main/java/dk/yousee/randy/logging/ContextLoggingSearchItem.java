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
    
    /**
     * If true will print eight stars (********), if field exists and is not null
     */
    private boolean obscure = false;

    public ContextLoggingSearchItem() {
    }

    public ContextLoggingSearchItem(boolean obscure) {
    	this(null, null, obscure);
    	
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

    public ContextLoggingSearchItem(String key, String[] searchKeys, boolean obscure) {
    	this(key, searchKeys);
    	setObscure(obscure);
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

	public boolean isObscure() {
		return obscure;
	}
	
	public void setObscure(boolean obscure) {
		this.obscure = obscure;

    	if (obscure)
    		format = new ObscureFormatter();

	}

}
