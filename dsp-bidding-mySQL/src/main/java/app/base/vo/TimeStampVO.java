package app.base.vo;

import java.util.Date;

/**
 * 一个封装了 时戳的VO
 * Created by ralf.cao on 2015/1/20.
 */
public class TimeStampVO<T> {
    private T entity;
    private Date timeStamp;
    public TimeStampVO(T entity,Date timeStamp){
        this.entity = entity;
        this.timeStamp = timeStamp;
    }

    public TimeStampVO(T entity,long timeStamp){
        this.entity = entity;
        this.timeStamp = new Date(timeStamp);
    }

    public T getEntity() {
        return entity;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
