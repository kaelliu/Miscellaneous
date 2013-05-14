package lib.kael;

import java.util.HashMap;

public class CaseInsensitiveMap extends HashMap<String, Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Object put(String key, Object value) {
       return super.put(key.toLowerCase(), value);
    }

    // not @Override because that would require the key parameter to be of type Object
//    public Object get(String key) {
//       return super.get(key.toLowerCase());
//    }
}
