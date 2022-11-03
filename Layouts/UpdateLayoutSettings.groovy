/**
 * Actualiza una property para todas las páginas de un site.
 */
import java.util.*;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import java.util.*;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;

####### VARIABLES #########
long groupId = 45480L;
String propertyName = "lfr-theme:regular:id-script-onetrust";
String propertyValue = "a2e3bfb2-84a2-4311-936f-c91d91e971c2-test";
String locale = "it_IT"
####### VARIABLES #########

List<Layout> listLayouts = LayoutLocalServiceUtil.getLayouts(groupId, false);
for(Layout layout : listLayouts) {
    out.println("#############################################");
    out.println(layout.getTitle(locale));
    UnicodeProperties props = layout.getTypeSettingsProperties();
    out.println(layout.getTypeSettings());
    props.setProperty(propertyName, propertyValue);
    layout.setTypeSettingsProperties(props);
    //Layout layoutUpdated = LayoutLocalServiceUtil.updateLayout(layout);
    out.println("####################AFTER UPDATE#########################");
    out.println(layout.getTypeSettings());
}
