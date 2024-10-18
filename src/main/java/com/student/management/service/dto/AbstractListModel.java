package com.student.management.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AbstractListModel<T> implements Serializable {

    private List<T> data = new ArrayList<>();
    private long total = 0;

    public AbstractListModel() {
    }

    public AbstractListModel(List<T> data) {
        this.data = data;
    }

    public AbstractListModel(List<T> data, long total) {
        this(data);
        this.total = total;
    }


}
