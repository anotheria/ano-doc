package net.anotheria.asg.util.action;

import jakarta.servlet.http.HttpServletResponse;
import net.anotheria.anoplass.api.util.paging.PagingControl;
import net.anotheria.util.datatable.DataRow;
import net.anotheria.util.datatable.DataTable;
import net.anotheria.util.slicer.Segment;
import net.anotheria.util.slicer.Slice;
import net.anotheria.util.slicer.Slicer;

import jakarta.servlet.http.HttpServletRequest;
import net.anotheria.util.xml.XMLNode;
import net.anotheria.util.xml.XMLTree;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Utils for common code in actions.
 *
 * @author lrosenberg
 * @since 2019-09-25 16:35
 */
public class ActionUtils {

	private static final List<String> ITEMS_ON_PAGE_SELECTOR = java.util.Arrays.asList(new String[]{"20","50","100","500","1000", "2000"});

	public static final <T> List<T> sliceDataAndSavePagingInformation(HttpServletRequest req, List<T> incomingData){
		// paging
		int pageNumber = 1;
		try{
			pageNumber = Integer.parseInt(req.getParameter("pageNumber"));
		}catch(Exception ignored){}
		Integer lastItemsOnPage = (Integer)req.getSession().getAttribute("currentItemsOnPage");
        //default is now 100.
		int itemsOnPage = lastItemsOnPage == null ? 100 : lastItemsOnPage;
		try{
			itemsOnPage = Integer.parseInt(req.getParameter("itemsOnPage"));
		}catch(Exception ignored){}
		Slice<T> slice = Slicer.slice(new Segment(pageNumber, itemsOnPage), incomingData);

		// prepare paging control
		PagingControl pagingControl = new PagingControl(slice.getCurrentSlice(), slice.getElementsPerSlice(), slice.getTotalNumberOfItems());
		// end paging control
		req.setAttribute("pagingControl", pagingControl);
		req.setAttribute("currentpage", pageNumber);
		req.setAttribute("currentItemsOnPage", itemsOnPage);
		req.getSession().setAttribute("currentItemsOnPage", itemsOnPage);
		req.setAttribute("PagingSelector", ITEMS_ON_PAGE_SELECTOR);

		return slice.getSliceData();
	}

    public static final void writeXMLExportToStream(HttpServletResponse resp, XMLNode xmlNode) throws IOException {
        resp.setContentType("text/xml");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + xmlNode.getName().toLowerCase() +".xml"+ "\"");
        XMLTree xmlTree = new XMLTree();
        xmlTree.setRoot(xmlNode);
        xmlTree.write(new OutputStreamWriter(resp.getOutputStream()));
    }
    public static final void writeCSVExportToStream(HttpServletResponse resp, DataTable dataTable, String documentNameMultiple) throws IOException {
        resp.setContentType("text/csv; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + documentNameMultiple.toLowerCase()+".csv" + "\"");
        resp.getOutputStream().write((dataTable.getHeader().toCSV()+"\n").getBytes(StandardCharsets.UTF_8));
        for (DataRow dataRow : dataTable) {
            String csv = dataRow.toCSV()+"\n";
            resp.getOutputStream().write(csv.getBytes(StandardCharsets.UTF_8));
        }
        resp.getOutputStream().flush();
    }
}
