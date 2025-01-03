package com.maxwellnie.velox.sql.core.natives.database.sql.row;

/**
 * @author Maxwell Nie
 */
public class QueryRowSql extends RowSql {
    private boolean paging;
    private long start;
    private long offset;

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
