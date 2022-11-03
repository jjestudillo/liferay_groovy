/** 
* Permite reindexar los journal articles de un site determinado.
*/

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil
import com.liferay.journal.model.JournalArticle
import com.liferay.journal.service.JournalArticleLocalServiceUtil
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

def long _initGroupId = 20142;
def long _initStructureId = 0;
def boolean execute = false;
def boolean allStructures = true;
def int increment = 1000;

class ReindexJournalArticleClass {

	private static int _init = 0;
	
	private static Log _log = LogFactoryUtil.getLog("GROOVY TO REINDEX JOURNALS");

	public static DDMStructure getStructure (long structureId) {
		_log.info("Getting DDMStructure from " + structureId + " structureId");
		DDMStructure structure = null;
		try {
			structure = DDMStructureLocalServiceUtil.getStructure(structureId);
		}catch(Exception exception) {
			_log.error("No se ha podido obtener la estructura con id " + structureId + ", ", exception);
		}
		return structure;
	}

	public static List<JournalArticle> getJournalArticles (long groupId, DDMStructure structure, int start, int end) {
		_log.info("Getting articles from " + groupId + " groupId, " + structure.getStructureId()+ " structureId, from " + start + " to " + end);
		List<JournalArticle> larticles = null;
		larticles = JournalArticleLocalServiceUtil.getArticlesByStructureId(groupId, structure.getStructureKey(), start, end, null);
		_log.info(larticles.size() + " articles found");
		return larticles;
	}
	
	public static List<JournalArticle> getAllJournalArticles (long groupId, int start, int end) {
		_log.info("Getting articles from " + groupId + " groupId, from " + start + " to " + end);
		List<JournalArticle> larticles = null;
		larticles = JournalArticleLocalServiceUtil.getArticles(groupId, start, end);
		_log.info(larticles.size() + " articles found");
		return larticles;
	}

	public static void reindexJournalArticles (List<JournalArticle> larticles, int start, int end, boolean execute) {
		int size = larticles.size();
		int i = 0;
		int total = (size < end - start) ? size + start: end;
		_log.info("STARTING WITH " + size + " ARTICLES...");
		for(JournalArticle article: larticles){
			try{
				int current = start + i; 
				_log.info("["+ current +"/"+ total +"] Se ha obtenido el journalArticle: "+article.getArticleId()+ " resourcePrimKey: "+article.getResourcePrimKey())
				//Obtenemos el assetEntry asociado
				AssetEntry ae = AssetEntryLocalServiceUtil.getEntry(JournalArticle.class.getName(), article.getResourcePrimKey());
				_log.info("["+ current +"/"+ total +"] Se ha obtenido el AssetEntry: "+ae.getEntryId()+StringPool.SEMICOLON+ae.getGroupId()+StringPool.SEMICOLON+ae.getCompanyId()+StringPool.SEMICOLON+StringPool.APOSTROPHE+ae.getTitleCurrentValue()+StringPool.APOSTROPHE+StringPool.SEMICOLON)
				List<AssetEntry> assetEntries = new ArrayList<AssetEntry>();
				assetEntries.add(ae);
				//forzamos la reindexacion del assetEntry
				if(execute) {
					AssetEntryLocalServiceUtil.reindex(assetEntries);
				}else {
					_log.info("Esto es una prueba y no se esta reindexando el contenido");
				}
				_log.info("["+ current +"/"+ total +"] Se ha indexado el asset "+ae.getTitle())
			}catch(Exception e){
				_log.error("No se podido aprobar el contenido... se ha producido el errror ", e);
			}finally{
				i++;
			}
		}
	}

	public static void run(long groupId, long structureId, boolean execute, boolean allStructures, inc) {
		_log.info("Starting reindexing Journal Articles");
		DDMStructure structure = null;
		if(!allStructures) {
			structure = getStructure(structureId);
		}
		int start = _init;
		int end = start + inc;
		
		List<JournalArticle> larticles = null;
		if(structure != null) {
			larticles = getJournalArticles(groupId, structure, start, end);
		}else {
			larticles = getAllJournalArticles(groupId, start, end);
		}

		while(!larticles.isEmpty()) {
			reindexJournalArticles(larticles, start, end, execute);
			start = end;
			end = start + inc;
			if(structure != null) {
				larticles = getJournalArticles(groupId, structure, start, end);
			}else {
				larticles = getAllJournalArticles(groupId, start, end);
			}
			
		}
	
	}
}

Thread.start ReindexJournalArticleClass.run(_initGroupId, _initStructureId, execute, allStructures, increment);