/**
* Consulta si las páginas de un determinado site tienen activada la herencia a partir de alguna plantilla
*/

package com.liferay.everis.index;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.LocaleUtil;

def long groupId = 22029;
def boolean privateLayout = false;
def execute = true;

class GetLayoutPropertiesClass {
	
	private static Log _log = LogFactoryUtil.getLog("GROOVY TO GET LAYOUT PROTOTYPE VALUE");
	
	public static void getLayouts(long groupId, boolean privateLayout) {
		_log.info("Getting layouts...");
		List<Layout> layouts = null;
		try {
			layouts = LayoutLocalServiceUtil.getLayouts(groupId, privateLayout);
			for(Layout layout : layouts) {
				String nombre = layout.getName(LocaleUtil.getDefault());
				boolean herencia = layout.isLayoutPrototypeLinkActive();
				_log.info("Página: " + nombre + " ---- Herencia: " + herencia);
			}
		}catch(Exception exception) {
			_log.error("No se ha podido obtener la lista de layouts", exception);
		}
	}

}

Thread.start GetLayoutPropertiesClass.getLayouts(groupId, privateLayout);