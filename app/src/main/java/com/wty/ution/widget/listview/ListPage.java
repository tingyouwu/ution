package com.wty.ution.widget.listview;

/**
 * 分页控制
 * @author wty
 */
public class ListPage {

    private int pageIndex;//目前列表加载到第几分页
    private int pagePiece;//每页数据的数量
    private boolean isLastPage = false;

    public ListPage(int pagePiece){
        this.pagePiece = pagePiece;
    }

    public int getPagePiece() {
        return pagePiece;
    }

    public void setPagePiece(int pagePiece) {
        this.pagePiece = pagePiece;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void pageNext(){
        this.pageIndex = pageIndex+1;
    }

    public void pageReset(){
        this.pageIndex = 0;
        this.isLastPage = false;
    }

    public void setIsLastPage(boolean isLastPage){
        this.isLastPage = isLastPage;
    }

    public boolean isLastPage(){
        return this.isLastPage;
    }

    public int getnextPageStartIndex(){
        int index = getPageIndex();//已经加载到第几页
        int piece = getPagePiece();//每页大小
        return index * piece;
    }

    /**
     * 功能描述：通过入参判断当前是最后一页还是下一页
     **/
    public void setPage(int resultsize){
        if(resultsize<getPagePiece()){
            //返回的数据不够一页，证明已经是最后一页
            setIsLastPage(true);
        }else if(resultsize==getPagePiece()){
            //下一页
            pageNext();
        }
    }

}
