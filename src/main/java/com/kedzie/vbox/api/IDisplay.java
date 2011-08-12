package com.kedzie.vbox.api;

import java.util.List;
import java.util.Map;
import com.kedzie.vbox.KSOAP;

public interface IDisplay extends IRemoteObject {
	public Map<String, List<String>> getScreenResolution(@KSOAP(type="unsignedInt", value="screenId") int screenId);
}
