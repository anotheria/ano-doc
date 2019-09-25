package net.anotheria.asg.util.action;

import net.anotheria.anoplass.api.util.paging.PagingControl;
import net.anotheria.util.slicer.Segment;
import net.anotheria.util.slicer.Slice;
import net.anotheria.util.slicer.Slicer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Utils for common code in actions.
 *
 * @author lrosenberg
 * @since 2019-09-25 16:35
 */
public class ActionUtils {

	private static final List<String> ITEMS_ON_PAGE_SELECTOR = java.util.Arrays.asList(new String[]{"5","10","20","25","50","100","500","1000", "2000"});

	public static final <T> List<T> sliceDataAndSavePagingInformation(HttpServletRequest req, List<T> incomingData){
		// paging
		int pageNumber = 1;
		try{
			pageNumber = Integer.parseInt(req.getParameter("pageNumber"));
		}catch(Exception ignored){}
		Integer lastItemsOnPage = (Integer)req.getSession().getAttribute("currentItemsOnPage");
		int itemsOnPage = lastItemsOnPage == null ? 20 : lastItemsOnPage;
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
}
