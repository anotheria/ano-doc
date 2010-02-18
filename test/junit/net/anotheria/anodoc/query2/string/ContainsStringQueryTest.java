package net.anotheria.anodoc.query2.string;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.Property;
import net.anotheria.anodoc.data.StringProperty;
import net.anotheria.anodoc.query2.QueryResultEntry;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.data.ObjectInfo;

import org.junit.Test;

public class ContainsStringQueryTest {

	private class DocumentStub extends Document implements DataObject {

		private static final long serialVersionUID = -1050732326635139652L;

		private List<Property> properties = new ArrayList<Property>();

		public DocumentStub(String id) {
			super(id);
		}

		public void addProperty(String name, String value) {
			properties.add(new StringProperty(name, value));
		}

		@Override
		public List<Property> getProperties() {
			return properties;
		}

		@Override
		public String getDefinedName() {
			return null;
		}

		@Override
		public String getDefinedParentName() {
			return null;
		}

		@Override
		public String getFootprint() {
			return null;
		}

		@Override
		public ObjectInfo getObjectInfo() {
			return null;
		}

	}

	@Test
	public void testMatch() {

		DocumentStub dataObject;

		dataObject = new DocumentStub("2010");
		dataObject.addProperty("testProperty", "This is Anotheria software");

		String criteria = "Anotheria";
		ContainsStringQuery instance = new ContainsStringQuery(criteria);
		List<QueryResultEntry> result = instance.match((DataObject) dataObject);
		assertTrue(result.size() == 0);

		criteria = "%Anotheria%";
		instance = new ContainsStringQuery(criteria);
		result = instance.match((DataObject) dataObject);
		assertTrue(result.size() == 1);
		StringMatchingInfo matchingInfo = (StringMatchingInfo) result.get(0).getInfo();
		assertEquals(matchingInfo.getPre(), "This is ");
		assertEquals(matchingInfo.getMatch(), "Anotheria");
		assertEquals(matchingInfo.getPost(), " software");

		criteria = "2010"; // search by id
		instance = new ContainsStringQuery(criteria);
		result = instance.match((DataObject) dataObject);
		assertTrue(result.size() == 1);
		matchingInfo = (StringMatchingInfo) result.get(0).getInfo();
		assertEquals(matchingInfo.getPre(), "");
		assertEquals(matchingInfo.getMatch(), "2010");
		assertEquals(matchingInfo.getPost(), "");

		criteria = "20%"; // search by id
		instance = new ContainsStringQuery(criteria);
		result = instance.match((DataObject) dataObject);
		assertTrue(result.size() == 1);
		matchingInfo = (StringMatchingInfo) result.get(0).getInfo();
		assertEquals(matchingInfo.getPre(), "");
		assertEquals(matchingInfo.getMatch(), "20");
		assertEquals(matchingInfo.getPost(), "10");

		criteria = "%Ano%ria%";
		instance = new ContainsStringQuery(criteria);
		result = instance.match((DataObject) dataObject);
		assertTrue(result.size() == 1);
		matchingInfo = (StringMatchingInfo) result.get(0).getInfo();
		assertEquals(matchingInfo.getPre(), "This is ");
		assertEquals(matchingInfo.getMatch(), "Anotheria");
		assertEquals(matchingInfo.getPost(), " software");

		criteria = "%Ano__eria%";
		instance = new ContainsStringQuery(criteria);
		result = instance.match((DataObject) dataObject);
		assertTrue(result.size() == 1);
		matchingInfo = (StringMatchingInfo) result.get(0).getInfo();
		assertEquals(matchingInfo.getPre(), "This is ");
		assertEquals(matchingInfo.getMatch(), "Anotheria");
		assertEquals(matchingInfo.getPost(), " software");

		criteria = "This%";
		instance = new ContainsStringQuery(criteria);
		result = instance.match((DataObject) dataObject);
		assertTrue(result.size() == 1);
		matchingInfo = (StringMatchingInfo) result.get(0).getInfo();
		assertEquals(matchingInfo.getPre(), "");
		assertEquals(matchingInfo.getMatch(), "This");
		assertEquals(matchingInfo.getPost(), " is Anotheria software");

		criteria = "%software";
		instance = new ContainsStringQuery(criteria);
		result = instance.match((DataObject) dataObject);
		assertTrue(result.size() == 1);
		matchingInfo = (StringMatchingInfo) result.get(0).getInfo();
		assertEquals(matchingInfo.getPre(), "This is Anotheria ");
		assertEquals(matchingInfo.getMatch(), "software");
		assertEquals(matchingInfo.getPost(), "");

	}

}
